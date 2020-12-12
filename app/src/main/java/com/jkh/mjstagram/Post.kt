package com.jkh.mjstagram

import java.util.*

class Post {

    constructor(){}

    constructor(userId:String, imageUrl:String, text:String){
        this.userId=userId
        this.imageUrl=imageUrl
        this.text=text
        uploadDate=Date()
    }

    var userId:String=""
    var imageUrl:String=""
    var text:String=""
    var uploadDate:Date= Date()

}