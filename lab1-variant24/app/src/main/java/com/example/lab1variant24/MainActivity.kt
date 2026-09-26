package com.example.lab1variant24

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab1variant24.databinding.ActivityMainBinding
import com.example.lab1variant24.domain.CommonWordFinder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val basePadding = binding.main.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + basePadding,
                systemBars.top + basePadding,
                systemBars.right + basePadding,
                systemBars.bottom + basePadding,
            )
            insets
        }
        binding.sentenceOneInput.setText(CommonWordFinder.FIRST_SENTENCE)
        binding.sentenceTwoInput.setText(CommonWordFinder.SECOND_SENTENCE)

        binding.findButton.setOnClickListener {
            val firstSentence = binding.sentenceOneInput.text.toString()
            val secondSentence = binding.sentenceTwoInput.text.toString()
            val words = CommonWordFinder.findLongestCommonWords(firstSentence, secondSentence)
            binding.resultText.text = if (words.isEmpty()) {
                getString(R.string.result_not_found)
            } else {
                words.joinToString(", ")
            }
        }
    }
}