package com.learn.todolistkotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learn.todolistkotlin.databinding.ActivityEditBinding

class EditActivity : BaseActivity() {
    private lateinit var binding : ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}