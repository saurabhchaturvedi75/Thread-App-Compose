package com.example.threadapp.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import com.example.threadapp.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import java.util.UUID

class AddThreadViewModel : ViewModel() {


    private val db = FirebaseDatabase.getInstance()
    val usersRef = db.getReference("threads")


    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted


      fun saveImage(
        thread: String,
        imageUri: Uri,
        uid: String

    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thread, it.toString(), uid)
            }
        }
    }

      fun saveData(
        thread: String,
        url: String,
        uid: String

    ) {
        val threadData =  ThreadModel(thread,url,uid,System.currentTimeMillis().toString())
        usersRef.child(usersRef.push().key!!).setValue(threadData).addOnSuccessListener {
            _isPosted.postValue(true)

        }
            .addOnFailureListener {
                _isPosted.postValue(false)

            }
    }
}