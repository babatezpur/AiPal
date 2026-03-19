package com.saptarshi.aipal.ui.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saptarshi.aipal.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _name = mutableStateOf<String>("")
    val name: State<String> = _name

    private val _email = mutableStateOf<String>("")
    val email: State<String> = _email

    private val _imgPath = mutableStateOf<String>("")
    val imgPath: State<String> = _imgPath


    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            val profile = profileRepository.getProfile()
            _name.value = profile?.name ?: ""
            _email.value = profile?.email ?: ""
            _imgPath.value = profile?.photoPath ?: ""

        }
    }

    fun updateName(newName: String) {
        _name.value = newName
        viewModelScope.launch {
            profileRepository.updateName(newName)
        }
    }

    fun copyImageToInternalStorage(context: Context, uri: Uri): String {
        // Delete old photo if it exists
        val oldPath = _imgPath.value
        if (oldPath.isNotEmpty()) {
            File(oldPath).delete()
        }

        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "profile_pic_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    fun onPhotoPicked(context: Context, uri: Uri) {
        viewModelScope.launch {
            val path = copyImageToInternalStorage(context, uri)
            profileRepository.updatePhoto(path)
            _imgPath.value = path
        }
    }



}