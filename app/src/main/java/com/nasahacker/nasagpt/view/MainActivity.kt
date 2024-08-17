package com.nasahacker.nasagpt.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nasahacker.nasagpt.R
import com.nasahacker.nasagpt.adapter.ChatAdapter
import com.nasahacker.nasagpt.databinding.ActivityMainBinding
import com.nasahacker.nasagpt.viewmodel.MainViewModel

class MainActivity : AppCompatActivity()
{
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels()
    private val adapter by lazy {
        ChatAdapter(this, mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rvChats.adapter = adapter
        mainViewModel.chats.observe(this) {
            if (it.isNotEmpty())
            {
                binding.layoutSuggestion.visibility = View.GONE
                adapter.updateChats(it)
                binding.rvChats.smoothScrollToPosition(it.size - 1)
            }
        }
        mainViewModel.isLoading.observe(this) {
            if (it)
            {
                binding.arrowSend.setImageResource(R.drawable.baseline_stop_circle_24)
                binding.arrowSend.isClickable = false
            } else
            {
                binding.arrowSend.setImageResource(R.drawable.message_send)
                binding.arrowSend.isClickable = true
            }
        }


        binding.arrowSend.setOnClickListener {
            if (binding.edtPrompt.text.toString().isEmpty())
            {
                Toast.makeText(this, "Prompt cannot be empty !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mainViewModel.generateContent(binding.edtPrompt.text.toString())
            binding.edtPrompt.setText("")
            closeKeyboard()
        }


    }

    private fun closeKeyboard()
    {
        val ipmm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        ipmm.hideSoftInputFromWindow(null, 0)
    }
}