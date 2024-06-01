package android.me.keenanstory

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.me.keenanstory.adapter.*
import android.me.keenanstory.databinding.ActivityMainBinding
import android.me.keenanstory.factory.StoriesViewModelFactory
import android.me.keenanstory.adapter.LoadingStateAdapter
import android.me.keenanstory.adapter.TabStoriesAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingViewModel()
    }

    private fun settingViewModel() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this@MainActivity, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val factory: StoriesViewModelFactory = StoriesViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this@MainActivity, factory)[MainViewModel::class.java]

        mainViewModel.getToken().observe(this@MainActivity) { token ->
            if (token.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Watch Out: The Token is Empty,You should login again",
                    Toast.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                }, 2000)
            }
        }

        mainViewModel.getToken().observe(this@MainActivity) { token ->
            this.token = token
            Log.e("TagMain", token)
            if (token.isNotEmpty()) {
                val adapter = TabStoriesAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                   footer = LoadingStateAdapter {
                        adapter.retry()
                   }
                )
                mainViewModel.listStory(token).observe(this@MainActivity) { main ->
                 adapter.submitData(lifecycle, main)
                }
                showLoading(false)
            }//token
        }//mainview
    }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_app, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.map -> {
                    startActivity(Intent(this@MainActivity, MapActivity::class.java))
                    true
                }
                R.id.menu_setting -> {
                    mainViewModel.logout()
                    finish()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }

        private fun showLoading(isLoading: Boolean) {
            binding.progressBarLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
