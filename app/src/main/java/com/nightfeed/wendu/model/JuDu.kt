package com.nightfeed.wendu.model

import java.io.Serializable

data  class JuDu(val content : String, val subheading:String, val pictures:List<Pictures>){
    public  class Pictures(var url : String): Serializable
}


