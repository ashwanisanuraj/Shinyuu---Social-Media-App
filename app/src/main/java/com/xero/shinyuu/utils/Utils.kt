package com.xero.shinyuu.utils

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String,callback:(String?)->Unit){
    var imageUrl: String? = null
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString()) //this will provide a random id everytime
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}