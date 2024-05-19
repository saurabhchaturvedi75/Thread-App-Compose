package com.example.threadapp.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadapp.item_model.ThreadItem
import com.example.threadapp.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navHostController: NavHostController){

    val context = LocalContext.current
    val homeViewModel : HomeViewModel = viewModel()
    val threadAndUser by homeViewModel.threadsAndUsers.observeAsState(null)

    LazyColumn {

        items(threadAndUser?: emptyList()){pairs->
            ThreadItem(thread = pairs.first, user = pairs.second,navHostController,FirebaseAuth.getInstance().currentUser!!.uid)

        }
    }

}