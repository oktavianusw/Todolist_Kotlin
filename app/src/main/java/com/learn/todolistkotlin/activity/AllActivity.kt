package com.learn.todolistkotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learn.todolistkotlin.databinding.ActivityAllBinding

class AllActivity : BaseActivity() {

    private lateinit var binding: ActivityAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}