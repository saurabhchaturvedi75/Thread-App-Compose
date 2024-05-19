package com.example.threadapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.R
import com.example.threadapp.navigation.Routes
import com.example.threadapp.utils.SharedPref
import com.example.threadapp.viewModel.AddThreadViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreads(navHostController: NavHostController) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val addThreadViewModel: AddThreadViewModel = viewModel()
    val isPosted by addThreadViewModel.isPosted.observeAsState(false)

    val context = LocalContext.current

    var addThread by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(isPosted) {
        if (isPosted) {
            addThread = ""
            imageUri = null

            Toast.makeText(context, "thread has added", Toast.LENGTH_SHORT).show()
            navHostController.navigate(Routes.Home.route) {
                popUpTo(Routes.AddThreads.route) {
                    inclusive = true
                }
            }
        }
    }

    val permissionToRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri

        }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (column, bottomRow) = createRefs()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(column) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomRow.top)
                }
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            navHostController.navigate(Routes.Home.route) {
                                popUpTo(Routes.AddThreads.route) {
                                    inclusive = true
                                }
                            }
                        }
                        .padding(horizontal = 10.dp)
                )



                Text(
                    text = "Add Thread", style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryColor
                    )
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        ,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(5.dp))

                SharedPref.getUserName(context)?.let {
                    Text(
                        text = it, style = TextStyle(
                            fontSize = 20.sp,   color = secondaryColor
                        )
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))


            OutlinedTextField(value = addThread, onValueChange = {
                addThread = it
            },   label = { Text(text = "Add Thread") }, modifier = Modifier
                .fillMaxWidth()
                 ,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                )


            )

            Spacer(modifier = Modifier.height(5.dp))

            if (imageUri == null) {

                Image(painter = painterResource(id = R.drawable.baseline_attachment_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            val isGranted = ContextCompat.checkSelfPermission(
                                context,
                                permissionToRequest
                            ) == PackageManager.PERMISSION_GRANTED
                            if (isGranted) {
                                launcher.launch("image/*")
                            } else {
                                permissionLauncher.launch(permissionToRequest)
                            }

                        }
                        .size(30.dp))
            } else {
                Box(
                    modifier = Modifier
                        .height(250.dp)
                        .background(Color.Gray)
                        .padding(1.dp)
                ) {


                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                imageUri = null
                            }
                            .align(Alignment.TopEnd)
                    )
                }

            }


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomRow) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Anyone can message", style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            )

            TextButton(
                onClick = {
                    if (imageUri == null) {
                        addThreadViewModel.saveData(
                            addThread,
                            "",
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )
                    } else {
                        addThreadViewModel.saveImage(
                            addThread,
                            imageUri!!,
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(secondaryColor),
                modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
            ) {
                Text(text = "Post")

            }

        }
    }
}
