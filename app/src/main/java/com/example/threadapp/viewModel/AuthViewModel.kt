package com.example.threadapp.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.UserModel
import com.example.threadapp.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val usersRef = db.getReference("users")


    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)

                    getData(auth.currentUser?.uid,context)
                } else {
                    _error.postValue(it.exception?.message)
                }
            }
    }

    private fun getData(uid: String?, context: Context) {

        usersRef.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(
                    userData!!.name,
                    userData!!.userName,
                    userData!!.bio,
                    userData!!.email,
                    userData!!.url,
                     context
                )
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun register(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        imageUri: Uri,
        context: Context
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(
                        name,
                        userName,
                        bio,
                        email,
                        password,
                        imageUri,
                        auth.currentUser!!.uid,
                        context
                    )
                } else {
                    _error.postValue("registration failed")
                }
            }
    }

    private fun saveImage(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        imageUri: Uri,
        uid: String,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(name, userName, bio, email, password, it.toString(), uid, context)
            }
        }
    }

    private fun saveData(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        url: String,
        uid: String?,
        context: Context
    ) {

        val firestoreDb = Firebase.firestore
        val followerRef = firestoreDb.collection("followers").document(uid!!)
        val followingRef = firestoreDb.collection("following").document(uid)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followerRef.set(mapOf("followerIds" to listOf<String>()))


        val userData = UserModel(name, userName, bio, email, password, url, uid)
        usersRef.child(uid).setValue(userData).addOnSuccessListener {

            SharedPref.storeData(name, userName, bio, email, url, context)
        }
            .addOnFailureListener {

            }

    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}