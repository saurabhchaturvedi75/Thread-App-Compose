package com.example.threadapp.item_model

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.model.UserModel
import com.example.threadapp.navigation.Routes

@Composable
fun UserItem(

    user: UserModel,
    navHostController: NavHostController

) {


    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary


    Column {

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
                .clickable {

                    val route = Routes.OtherUsers.route.replace("{data}", user.uid)
                    navHostController.navigate(route)
                }, verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = user.url),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = user.name, style = TextStyle(
                        fontSize = 16.sp,
                        color = primaryColor
                    ), modifier = Modifier.padding(start = 8.dp)
                )


                Text(text = user.userName, style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryColor
                ),modifier = Modifier.padding(start = 8.dp))
            }


        }

        Spacer(modifier = Modifier.height(9.dp))

        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        Spacer(modifier = Modifier.height(5.dp))
    }

}