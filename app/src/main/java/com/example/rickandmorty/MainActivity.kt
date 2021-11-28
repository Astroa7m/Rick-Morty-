package com.example.rickandmorty

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rickandmorty.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import kotlin.random.Random

/**
 * Activities are preferable to only be for the UI logic,
 * so putting the business logic within the main activity is not a good practice due to
 * the activity lifecycle which might cause unnecessary network requests during configuration changes
 * consider putting it into a [ViewModel] class and observe it through observables objects.
 */

/**
 * [Coroutines] are lightweight threads which are exclusively made for kotlin.
 * Using a coroutine is much more cheap than using threads as creating a coroutine does not
 * create another thread but it utilize a predefined thread pools which are aware of how to schedule
 * the work of calling a function or delay it or calling it other time later.
 *
 * Here we are using a [lifecycleScope] which is a predefined coroutine scope which is a
 * lifeCycle-aware coroutine that is created for every object,
 * than means that the scope will be cancelled whenever the lifeCycle is destroyed.
 * The lifeCycleScope by nature runs on the main thread or (Main Dispatcher) so it's useful to have
 * a lifeCycle-aware coroutine and run suspend functions.
 * For our network call we changed the dispatcher or the thread to run on the IO Dispatchers
 * which is a useful dispatcher that takes the long running work into a background thread in
 * order to not block the Main UI.
 */


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // calling it when the app starts in order to see a character once we launch the app
        getCharacter(Random.nextInt(825) + 1)

        binding.generateButton.setOnClickListener {
            getCharacter(Random.nextInt(825) + 1)
        }

    }

    private fun getCharacter(randomId: Int) {
        lifecycleScope.launchWhenStarted {
            try {
                // sending the request and getting the response from the network
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCharacter(
                        randomId
                    )
                }
                if (response.isSuccessful) {
                    // checking if the body of the response is not null
                    // if it is null the .let block will not run
                    // in Kotlin `?` means that the variable might be nullable
                    response.body()?.let { character ->
                        // a scope function the gives a reference of the object specified
                        with(binding) {
                            //loading the image from a String URL into an image view
                            Glide.with(characterImage).load(character.image).circleCrop()
                                .into(characterImage)
                            characterName.text = character.name
                            characterGender.text = character.gender
                            characterLocation.text = character.location.name.replace(" ", "\n")
                            characterSpecies.text = character.species.replace(" ", "\n")
                            characterStar.text = character.episode.count().toString()
                            when(character.status){
                                "Alive" -> {
                                    characterStatus.text = "Alive"
                                    characterStatus.setTextColor(Color.GREEN)
                                }
                                "Dead"->{
                                    characterStatus.text = "Dead"
                                    characterStatus.setTextColor(Color.RED)
                                }
                                else -> {
                                    characterStatus.text = "Unknown"
                                    characterStatus.setTextColor(Color.GRAY)
                                }
                            }
                        }
                    }
                } else {
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