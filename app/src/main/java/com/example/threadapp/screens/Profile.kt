package com.example.threadapp.screens

import android.content.res.Resources.Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.R
import com.example.threadapp.item_model.ThreadItem
import com.example.threadapp.model.UserModel
import com.example.threadapp.navigation.Routes
import com.example.threadapp.utils.SharedPref
import com.example.threadapp.viewModel.AuthViewModel
import com.example.threadapp.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navHostController: NavHostController) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val context = LocalContext.current

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)


    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)

    if (firebaseUser != null) {
        userViewModel.fetchThread(firebaseUser!!.uid)
    }


    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    if (currentUserId != "") {
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }


    val user = SharedPref.getName(context)?.let {
        SharedPref.getImageUrl(context)?.let { it1 ->
            SharedPref.getUserName(context)?.let { it2 ->
                UserModel(
                    name = it,
                    url = it1,
                    userName = it2
                )
            }
        }
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.route) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }


    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = SharedPref.getName(context) ?: "",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor // Use your desired primary color
                            ),
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = SharedPref.getUserName(context) ?: "",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = secondaryColor // Use your desired secondary color
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = SharedPref.getBio(context) ?: "",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = tertiaryColor  // Use your desired secondary color
                            ),
                            modifier = Modifier.padding(top = 16.dp, end = 140.dp)
                        )
                    }
                  //  Spacer(modifier = Modifier.width(150.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column {

                        Text(
                            text = "${followerList?.size} followers",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = tertiaryColor// Use your desired secondary color
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "${followingList?.size} following",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = tertiaryColor // Use your desired secondary color
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Spacer(modifier = Modifier.width(150.dp))
                    ElevatedButton(
                        onClick = { authViewModel.logout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor, // Use your desired primary colo\
                        )// Use your desired primary color
                    ) {
                        Text(text = "Logout")
                    }

                }
            }
        }

        if (user != null) {
            items(threads ?: emptyList()) { pairs ->
                ThreadItem(
                    thread = pairs,
                    user = user,
                    navHostController = navHostController,
                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                )

            }
        }
    }


}