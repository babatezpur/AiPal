package com.saptarshi.aipal.ui.profile

import android.R.attr.top
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = Modifier.fillMaxSize(),
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
//        ProfileInfo(name, email)

    }
}

@Composable
fun ProfilePicture(imgPath: String) {
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
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileViewModelContent(
        name = "Saptarshi",
        email = "william.henry.harrison@example-pet-store.com",
        imgPath = ""
    )
}

