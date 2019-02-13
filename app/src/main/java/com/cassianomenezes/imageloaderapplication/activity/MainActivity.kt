package com.cassianomenezes.imageloaderapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cassianomenezes.imageloaderapplication.BR

import com.cassianomenezes.imageloaderapplication.R
import com.cassianomenezes.imageloaderapplication.utils.bindingContentView
import com.cassianomenezes.imageloaderapplication.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        bindingContentView(R.layout.activity_main).apply {
            setVariable(BR.viewModel, MainViewModel(this@MainActivity))
        }
    }
}
