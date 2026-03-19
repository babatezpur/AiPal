package com.saptarshi.aipal.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.saptarshi.aipal.R


@Composable
fun ProfileScreen(
    profileViewModel : ProfileViewModel
) {

    val name by profileViewModel.name
    val email by profileViewModel.email
    val imgPath by profileViewModel.imgPath

    ProfileViewModelContent(
        name,
        email,
        imgPath
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewModelContent(
    name: String,
    email: String,
    imgPath: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            title = {
                Text("Profile")
            },
        )

        ProfilePicture(imgPath)
        ProfileInfo(name, email ) { newName ->
            onNameChange(newName)
        }

    }
}

fun onNameChange(newName: String) {
    //
}

@Composable
fun ProfileInfo(name: String, email: String, onNameChange: (newName : String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Name",
            style = MaterialTheme.typography.bodyLarge,
            // bold the text
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 30.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Change name",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onNameChange(name)
                }
            )
        }
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyLarge,

        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge ,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfilePicture(imgPath: String, onClick: () -> Unit = {}) {
    Box(contentAlignment = Alignment.BottomEnd) {
        AsyncImage(
            model = imgPath.ifEmpty { null },
            contentDescription = "Profile photo",
            placeholder = painterResource(R.drawable.ic_default_avatar),
            error = painterResource(R.drawable.ic_default_avatar),
            fallback = painterResource(R.drawable.ic_default_avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .clickable { onClick() }
        )
        Box(
            modifier = Modifier
                .offset(x = (-8).dp, y = (-8).dp)
                .size(50.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Change photo",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileViewModelContent(
        name = "Saptarshi",
        email = "william.harrison@example-pet-store.com",
        imgPath = ""
    )
}

