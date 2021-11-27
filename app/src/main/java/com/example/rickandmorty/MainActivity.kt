package com.example.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rickandmorty.databinding.ActivityMainBinding
import java.io.IOException
import java.lang.Exception
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getCharacter(Random.nextInt(825) + 1)

        binding.generateButton.setOnClickListener {
            getCharacter(Random.nextInt(825) + 1)
        }

    }

    private fun getCharacter(randomId: Int) {
        lifecycleScope.launchWhenStarted {
            try {
                val response = RetrofitInstance.api.getCharacter(randomId)
                if (response.isSuccessful) {
                    response.body()?.let { character ->
                        with(binding) {
                            Glide.with(characterImage).load(character.image).circleCrop()
                                .into(characterImage)
                            characterName.text = character.name
                            characterGender.text = character.gender
                            characterLocation.text = character.location.name.replace(" ", "\n")
                            characterSpecies.text = character.species.replace(" ", "\n")
                            characterStar.text = character.episode.count().toString()
                        }
                    }
                }else{
                    Toast.makeText(this@MainActivity, "Error occurred", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@MainActivity, "No internet connection", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}