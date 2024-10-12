package com.gmat.ui.screen.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gmat.R
import com.gmat.ui.components.CenterBar
import com.gmat.ui.events.UserEvents
import com.gmat.ui.state.UserState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Composable
fun EditProfileDetails(
    modifier: Modifier = Modifier,
    navController: NavController,
    userState: UserState,
    onUserEvents: (UserEvents)->Unit
) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    // State to hold the download URL of the uploaded image
    var downloadUrl by remember { mutableStateOf<String?>(null) }

    // Activity Result Launcher for picking an image
    val imagePickerLauncher: ActivityResultLauncher<Intent> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Call the function to upload the image
                    uploadImageToFirebase(
                        uri,
                        storageRef,
                        onSuccess = { url ->
                            // Store the download URL in the state
                            downloadUrl = url
                            onUserEvents(UserEvents.OnProfileChange(downloadUrl!!))
                        },
                        onFailure = { exception ->
                            Log.e("Firebase", "Error uploading image: ${exception.message}")
                        }
                    )
                }
            }
        }
    )

    Scaffold(
        topBar = {
            CenterBar(
                onClick = { navController.navigateUp() },
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onUserEvents(UserEvents.UpdateUser)
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                    }
                })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(userState.user!!.profile.isNotBlank()){
                AsyncImage(
                    model = userState.user.profile,
                    contentDescription = null,
                    modifier = modifier
                        .padding(top = 10.dp)
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }
            else{
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    modifier = modifier.size(150.dp)
                )
            }

            Button(onClick = {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imagePickerLauncher.launch(intent)
            }) {
                Text(text = stringResource(id = R.string.upload_photo), fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }
            Spacer(modifier = modifier.height(20.dp))
            OutlinedTextField(
                value = userState.newName,
                onValueChange = {
                    onUserEvents(UserEvents.OnNameChange(it))
                },
                modifier = modifier.fillMaxWidth(),
                label = { Text("Name") }
            )
        }
    }
}

private fun uploadImageToFirebase(
    uri: Uri,
    storageRef: StorageReference,
    onSuccess: (String) -> Unit,
    onFailure: (Exception) -> Unit
) {
    //    val fileRef = storageRef.child("images/${uri.lastPathSegment}")
    val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "image"
    val newFileName = "${System.currentTimeMillis()}_$fileName"
    val fileRef = storageRef.child("images/$newFileName")
    val uploadTask = fileRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        // Get the download URL after a successful upload
        fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//            Log.d("Firebase", "Upload successful! Download URL: $downloadUrl")
            onSuccess(downloadUrl.toString())
        }.addOnFailureListener { exception ->
//            Log.e("Firebase", "Failed to get download URL: ${exception.message}")
            onFailure(exception)
        }
    }.addOnFailureListener { exception ->
        Log.e("Firebase", "Upload failed: ${exception.message}")
        onFailure(exception)
    }
}