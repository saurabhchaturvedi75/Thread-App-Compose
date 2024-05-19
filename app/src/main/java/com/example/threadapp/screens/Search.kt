package com.example.threadapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadapp.item_model.ThreadItem
import com.example.threadapp.item_model.UserItem
import com.example.threadapp.viewModel.HomeViewModel
import com.example.threadapp.viewModel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Search(navHostController: NavHostController) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState(null)
    var search by remember { mutableStateOf("") }


    Column {

        Text(
            text = "Search", style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            ),
            modifier = Modifier.padding(top = 24.dp, start = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))


        OutlinedTextField(value = search,
            onValueChange = { search = it },
            label = { Text(text = "Search Users") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))


        LazyColumn {
            if (userList != null && userList!!.isNotEmpty()) {

                val filterItems = userList?.filter { it.name.contains(search, ignoreCase = true) }
                items(filterItems ?: emptyList()) { pairs ->
                    UserItem(user = pairs, navHostController)

                }
            }
        }
    }

}