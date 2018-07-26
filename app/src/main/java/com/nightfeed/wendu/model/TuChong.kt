package com.nightfeed.wendu.model

data class TuChong (var post_id:String,var title : String, var images:List<image>){
  public  class image(var user_id : String, var img_id:String,var width:Int,var height:Int)
}