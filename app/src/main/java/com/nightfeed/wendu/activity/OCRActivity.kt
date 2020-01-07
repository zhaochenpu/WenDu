package com.nightfeed.wendu.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.sdk.model.GeneralParams
import com.baidu.ocr.sdk.model.GeneralResult
import com.baidu.ocr.sdk.model.Word
import com.baidu.ocr.ui.camera.CameraActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightfeed.wendu.MyApplication
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.OCRResult
import com.nightfeed.wendu.net.*
import com.nightfeed.wendu.utils.PermissionUtil
import com.nightfeed.wendu.utils.SystemUtil
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_ocr.*
import java.io.File
import java.io.FileInputStream


class OCRActivity : AppCompatActivity() {

    val instance by lazy { this }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)
        setSupportActionBar(toolbar)

        OCR.getInstance(instance).initAccessTokenWithAkSk(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                // 调用成功，返回AccessToken对象
                val token = accessToken.accessToken
            }

            override fun onError(error: OCRError) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, instance, "9Ecc1h034wnTRyA5DXKmT3pH", "4PCcay5FPOTXbR0UwcayL81zT4FLNzbG")


        if(!getSendImage()){
            if (Build.VERSION.SDK_INT >= 23) run {
                //6.0以后系统对敏感权限进行动态权限申请
                var permissionUtil = PermissionUtil(instance)
                val lackPermissions = permissionUtil.getLacksPermissions(PermissionUtil.PERMISSION_CAMERA)

                if (lackPermissions != null && lackPermissions!!.size > 0) {
                    // 缺少权限时, 进入权限配置页面
                    ActivityCompat.requestPermissions(this, lackPermissions!!, PermissionUtil.REQUEST_PERMISSION_CODE)
                }else{
                    startCameraActivity()
                }
            }else{
                startCameraActivity()
            }
        }

        toolbar.setNavigationOnClickListener { finish()}
    }

    private fun startCameraActivity() {
        val intent = Intent(instance, CameraActivity::class.java)
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                SystemUtil.getImageTemporaryFile(instance).absolutePath)

        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL)


        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL)
        startActivityForResult(intent, 133)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     //接受图片识别
        if (requestCode==133&&resultCode == RESULT_OK) {
            // 通过临时文件获取拍摄的图片
            recognition(SystemUtil.getImageTemporaryFile(instance).getAbsolutePath())
        }
    }

    private fun recognition(filePath:String) {
        original_image.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(filePath)))

        val param = GeneralParams()
        param.setDetectDirection(true)
        param.setVertexesLocation(true)
        param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL)
        param.imageFile = File(filePath)

        OCR.getInstance(instance).recognizeAccurateBasic(param, object : OnResultListener<GeneralResult> {
            override fun onResult(result: GeneralResult) {
                val r = MyJSON.getString(result.jsonRes, "words_result")
                val list = Gson().fromJson<List<OCRResult>>(r, object : TypeToken<List<OCRResult>>() {

                }.type)

                var resultBuffer = StringBuffer()
                for (i in 0 until list.size) {
                    resultBuffer.append(list.get(i).words + "\n")
                }
                ocr_result.text = resultBuffer.toString()
            }

            override fun onError(error: OCRError) {
                runOnUiThread(object : Runnable {
                    override fun run() {
                        if (TextUtils.isEmpty(error.message)) {
                            ocr_result.text = "图像识别错误"
                        } else {
                            ocr_result.text = error.message
                        }
                    }
                })
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionUtil.REQUEST_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity()
                } else {

                }
                return
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)//设置新的intent

        getSendImage()
    }

    private fun getSendImage() :Boolean {
        val fileUri = getIntent().clipData ?: return false
        recognition(SystemUtil.getSelectFilePath(instance, fileUri.getItemAt(0).uri) ?: return false)
        return true
    }
}


