package com.example.threadapp.item_model


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import java.text.SimpleDateFormat


@Composable
fun ThreadItem(
    thread: ThreadModel,
    user: UserModel,
    navHostController: NavHostController,
    userId: String
) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary


    Column(modifier = Modifier.padding(15.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {


                Image(
                    painter = rememberAsyncImagePainter(model = user.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                 Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = user.userName, style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    ), modifier = Modifier.padding(start = 15.dp)
                )
            }

            Text(
                text = formatTimestamp(thread.timeStamp.toLong()), style = TextStyle(
                    fontSize = 15.sp,

                    color = tertiaryColor
                ), modifier = Modifier.padding(end = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = thread.thread, style = TextStyle(
                fontSize = 15.sp,

                color = primaryColor
            ), modifier = Modifier.padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        if (thread.url != "") {
            Spacer(modifier = Modifier.height(3.dp))
            Card {
                Image(
                    painter = rememberAsyncImagePainter(model = thread.url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
    }

}


fun formatTimestamp(timestamp: Long): String {
    // Define the desired date and time format
    val dateFormat = SimpleDateFormat("dd-MM-yyyy \nHH:mm:ss a")

    // Create a Date object from the timestamp
    val date = java.util.Date(timestamp)

    // Format the timestamp
    return dateFormat.format(date)
}