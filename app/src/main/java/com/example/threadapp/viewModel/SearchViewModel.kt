package com.example.threadapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {


    private val db = FirebaseDatabase.getInstance()
    val users = db.getReference("users")


    private val _users = MutableLiveData<List <UserModel>>()
    val  userList: LiveData<List<UserModel>> = _users

init {
    fetchUsers {
        _users.value = it
    }
}
    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {

        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<UserModel>()

                for (threadSnapshot in snapshot.children) {

                    val thread = threadSnapshot.getValue(UserModel::class.java)

                    if (thread?.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                        result.add(thread!!)
                    }

                 //   result.add(thread!!)
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }




}