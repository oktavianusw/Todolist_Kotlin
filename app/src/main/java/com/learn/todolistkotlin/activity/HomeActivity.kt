package com.learn.todolistkotlin.activity

import android.os.Bundle
import android.widget.TextView
import com.learn.todolistkotlin.R
import com.learn.todolistkotlin.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}