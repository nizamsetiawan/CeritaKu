
package com.bangkit.intermediate.ceritaku.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.intermediate.ceritaku.R
import com.bangkit.intermediate.ceritaku.databinding.ActivityHomeBinding
import com.bangkit.intermediate.ceritaku.ui.adapter.StoryAdapter
import com.bangkit.intermediate.ceritaku.ui.auth.OnboardingActivity
import com.bangkit.intermediate.ceritaku.ui.story.AddStoryActivity
import com.bangkit.intermediate.ceritaku.ui.story.MapsActivity
import com.bangkit.intermediate.ceritaku.ui.viewModels.HomeViewModel
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import com.bangkit.intermediate.ceritaku.utils.Prefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mToolbar)

        initializeRecyclerView()
        observeViewModel()
        onButton()

        val token = Prefs.getToken
        homeViewModel.fetchAllStories(token)
    }

    private fun initializeRecyclerView() {
        storyAdapter = StoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = storyAdapter
        }
    }

    private fun onButton() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        homeViewModel.getAllStory.observe(this) { apiResult ->
            when (apiResult) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Success -> {
                    apiResult.data.let { pagingData ->
                        storyAdapter.submitData(lifecycle, pagingData)
                    }
                }
                is ApiResult.Error -> {
                    Toast.makeText(this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Prefs.clearAuthPrefs()
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
