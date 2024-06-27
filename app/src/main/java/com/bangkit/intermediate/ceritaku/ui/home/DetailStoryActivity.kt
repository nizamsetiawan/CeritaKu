package com.bangkit.intermediate.ceritaku.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bangkit.intermediate.ceritaku.R
import com.bangkit.intermediate.ceritaku.databinding.ActivityDetailStoryBinding
import com.bangkit.intermediate.ceritaku.source.response.DetailStoryResponse
import com.bangkit.intermediate.ceritaku.ui.viewModels.HomeViewModel
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import com.bangkit.intermediate.ceritaku.utils.Prefs
import com.bangkit.intermediate.ceritaku.utils.ToastUtils
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailStoryActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeDetailStory()
        fetchDetailStory()
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.mToolbar);
        binding.mToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back);
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }



    private fun observeDetailStory() {
        homeViewModel.getDetailStory.observe(this, Observer { result ->
            when (result) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Success -> {
                    val detailStory = result.data
                    displayDetailStory(detailStory)
                    ToastUtils.informToast("Berhasil memuat..." , this)
                }
                is ApiResult.Error -> {
                    ToastUtils.errorToast(result.errorMessage , this)
                }
            }
        })
    }


    private fun fetchDetailStory() {
        val storyId = intent.getStringExtra("story_id") ?: ""
        val token = Prefs.getToken

        homeViewModel.fetchDetailStories(token, storyId)
    }

    private fun displayDetailStory(detailStory: DetailStoryResponse) {
        binding.apply {
            tvName.text = detailStory.story?.name
            tvDescription.text = detailStory.story?.description
            Glide.with(root.context)
                .load(detailStory.story?.photoUrl)
                .into(ivPhoto)
        }
    }
}
