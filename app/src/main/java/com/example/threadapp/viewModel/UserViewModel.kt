package com.example.threadapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserViewModel : ViewModel() {


    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")
    val threadRef = db.getReference("threads")


    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val threads: LiveData<List<ThreadModel>> get() = _threads

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList: LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList: LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users: LiveData<UserModel> = _users


    fun fetchUser(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchThread(uid: String) {
        threadRef.orderByChild("uid").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val threadList = snapshot.children.mapNotNull {
                        it.getValue(ThreadModel::class.java)
                    }
                    _threads.postValue(threadList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    val firestoreDb = Firebase.firestore

    fun followUsers(userId: String, currentUserId: String) {

        val ref = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
    }

    fun unfollowUsers(userId: String, currentUserId: String) {

        val ref = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayRemove(userId))
        followerRef.update("followerIds", FieldValue.arrayRemove(currentUserId))
    }


    fun getFollowers(userId: String) {
        firestoreDb.collection("followers").document(userId).addSnapshotListener {
            snapshot, error ->

            val followerIds= snapshot?.get("followerIds") as? List<String>  ?: emptyList()
            _followerList.value = followerIds

        }
    }

    fun getFollowing(userId: String) {
        firestoreDb.collection("following").document(userId).addSnapshotListener {
                snapshot, error ->

            val followerIds= snapshot?.get("followingIds") as? List<String>  ?: emptyList()
            _followingList.value = followerIds

        }
    }
}