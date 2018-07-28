package com.nightfeed.wendu.model

import java.io.Serializable

 data class TuChong (var post_id:String,var title : String,var  images:List<image>): Serializable {

  public  class image(var user_id : String, var img_id:String,var width:Int,var height:Int): Serializable
}