package com.example.plantix.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.BuildConfig
import com.example.core.data.Resource
import com.example.core.domain.model.ArticlesItem
import com.example.core.ui.BannerNewsAdapter
import com.example.core.ui.NewsItemAdapter
import com.example.plantix.R
import com.example.plantix.databinding.FragmentHomeBinding
import com.example.plantix.ui.main.MainViewModel
import com.example.plantix.ui.news.DetailNewsActivity
import com.example.plantix.ui.news.NewsViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _homeFragment: FragmentHomeBinding? = null

    private val newsViewModel: NewsViewModel by viewModel()

    private val mainViewModel: MainViewModel by viewModel()

    private var isErrorDialogShown = false

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private val homeFragment get() = _homeFragment!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        return _homeFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerFrameLayout = homeFragment.shimmerLayout

        observeData()
        setupView()
        setupListener()
    }

    private fun observeData() {
        newsViewModel.getNews("plants", "en", BuildConfig.API_KEY)
        newsViewModel.getBannerNews("plants awareness", "en", BuildConfig.API_KEY)
        mainViewModel.getUsername()
        getUsernameResult()
        getNewsResult()
        getBannerNewsResult()
    }

    private fun setupView() {
        homeFragment.apply {
            radioFilter.check(R.id.radio_for_you)
        }
    }

    private fun getUsernameResult() {
        mainViewModel.username.observe(viewLifecycleOwner) { username ->
            when (username) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val name = username.data.split(" ").joinToString(separator = " ") { it.capitalize() }
                    homeFragment.name.text = name
                }

                is Resource.Error -> {
                    showLoading(false)
                    Log.e("HomeFragment", "error: ${username.message}")
                }
            }
        }
    }

    private fun getNewsResult() {
        newsViewModel.news.observe(viewLifecycleOwner) { news ->
            when (news) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val data = news.data.articles
                    showRecyclerView(data)
                    Log.d("HomeFragment", "result: $news")
                }

                is Resource.Error -> {
                    showLoading(false)
                    if (news.message.contains("Tidak ada koneksi internet.", ignoreCase = true)) {
                        showError(news.message)
                    }
                    Log.e("HomeFragment", "error: ${news.message}")
                }
            }
        }
    }

    private fun getBannerNewsResult() {
        newsViewModel.bannerNews.observe(viewLifecycleOwner) { bannerNews ->
            when (bannerNews) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val data = bannerNews.data.articles
                    showBannerNews(data)
                    Log.d("HomeFragment", "Banner result: $bannerNews")
                }

                is Resource.Error -> {
                    showLoading(false)
                    Log.e("HomeFragment", "Banner error: ${bannerNews.message}")
                }
            }
        }
    }

    private fun showRecyclerView(data: List<ArticlesItem>) {
        homeFragment.rvNews.apply {
            adapter = NewsItemAdapter(data) {
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                startActivity(
                    Intent(requireContext(), DetailNewsActivity::class.java),
                    options.toBundle()
                )
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showBannerNews(data: List<ArticlesItem>) {
        homeFragment.apply {
            val indicator = dotsIndicator
            val viewPager = newsRecommended
            val adapter = BannerNewsAdapter(data.take(3)) {}
            viewPager.adapter = adapter
            indicator.attachTo(viewPager)
        }
    }

    private fun setupListener() {
        homeFragment.radioFilter.setOnCheckedChangeListener { group, checkedId ->
            val category = when (checkedId) {
                R.id.radio_for_you -> "plants"
                R.id.radio_plants -> "herbal plants"
                R.id.radio_health -> "plants health"
                R.id.radio_care -> "plants care"
                R.id.radio_disease -> "plants disease"
                else -> "plants"
            }

            newsViewModel.getNews(category, "en", BuildConfig.API_KEY)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        homeFragment.apply {
            if (isLoading) {
                shimmerFrameLayout.startShimmer()
                shimmerFrameLayout.visibility = View.VISIBLE
                contentLayout.visibility = View.GONE
            } else {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun showError(message: String) {
        if (isErrorDialogShown) return
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_notification)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)
            val descText = dialog.findViewById<TextView>(R.id.desc)
            val btnClose = dialog.findViewById<Button>(R.id.btnClose)
            btnClose.text = "Retry"
            descText.text = message
            btnClose.setOnClickListener {
                dialog.dismiss()
                isErrorDialogShown = false
                retryFetchingData()
            }
            show()
        }
        isErrorDialogShown = true
    }

    private fun retryFetchingData() {
        showLoading(true)
        newsViewModel.getNews("plants", "en", BuildConfig.API_KEY)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragment = null
    }
}