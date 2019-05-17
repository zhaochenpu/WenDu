package com.nightfeed.wendu.model

import java.io.Serializable

data  class JuDu(val content : String, val subheading:String, val image:Image){
    public  class Image(var url : String): Serializable
}


