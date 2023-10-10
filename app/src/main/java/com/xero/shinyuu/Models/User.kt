package com.xero.shinyuu.Models

class User {
    var image: String?=null //we r using image links, so String
    var name: String?=null
    var email: String?=null
    var password: String?=null

    //we need to create a constructor for firebase, its complulsory
    //we need 2 extra constructors. one for all detail with image, and other for all details without any image


    constructor()

    constructor(image: String?, name: String?, email: String?, password: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }


}