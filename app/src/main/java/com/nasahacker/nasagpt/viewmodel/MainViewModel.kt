package com.nasahacker.nasagpt.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.nasahacker.nasagpt.R
import com.nasahacker.nasagpt.model.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
class MainViewModel(application: Application) : AndroidViewModel(application = application)
{
    private val _generativeModel = MutableLiveData<GenerativeModel>()
    private val _chats = MutableLiveData<MutableList<Chat>>()
    val chats: LiveData<MutableList<Chat>> get() = _chats
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    val API_KEY = "AIzaSyCmdvBTM4u8pVxvBYKpJHveuxhY_0g3ZTY"


    init
    {
        _generativeModel.value = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = API_KEY,
            systemInstruction = content { text(application.getString(R.string.system_instructions)) }
        )
        _chats.postValue(mutableListOf())
    }

    fun generateContent(prompt: String)
    {
        val list = _chats.value?.toMutableList() ?: mutableListOf()
        list.add(Chat(chat = prompt, isChatBot = false, getCurrentTimestamp()))
        _chats.postValue(list)
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = _generativeModel.value?.generateContent(prompt)?.text.orEmpty()


            withContext(Dispatchers.Main) {
                val updatedList = _chats.value?.toMutableList() ?: mutableListOf()
                updatedList.add(Chat(chat = response, isChatBot = true, getCurrentTimestamp()))
                _chats.postValue(updatedList)
                _isLoading.value = false
            }
        }
    }


    private fun getCurrentTimestamp(): String
    {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }


}