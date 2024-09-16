package com.example.plantix.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.plantix.R
import com.example.plantix.databinding.FragmentProfileBinding
import com.example.plantix.ui.auth.login.LoginActivity
import com.example.plantix.ui.auth.login.LoginViewModel
import com.example.plantix.ui.main.MainViewModel
import com.example.plantix.ui.profile.edit.EditProfileActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _profileFragment: FragmentProfileBinding? = null

    private val profileFragment get() = _profileFragment!!

    private val profileViewModel: ProfileViewModel by viewModel()

    private val loginViewModel: LoginViewModel by viewModel()

    private var userId: Int? = null

    private var bottomDialog: BottomSheetDialog? = null

    private lateinit var editProfileResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _profileFragment = FragmentProfileBinding.inflate(inflater, container, false)
        return _profileFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editProfileResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    observeData()
                }
            }
        shimmerFrameLayout = profileFragment.shimmerLayout
        observeData()
        setupListener()
    }

    private fun observeData() {
        loginViewModel.getUserId()
        loginViewModel.userId.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    userId = it.data
                    profileViewModel.getDetailUser(it.data)
                }

                is Resource.Error -> {}
            }
        }
        detailUserResult()
    }

    private fun detailUserResult() {
        profileViewModel.detailUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {showLoading(true)}
                is Resource.Success -> {
                    showLoading(false)
                    val data = it.data.data?.first()
                    val name = data?.username?.split(" ")?.joinToString(separator = " ") { it.capitalize() }
                    val fullName = data?.fullName?.split(" ")?.joinToString(separator = " ") { it.capitalize() }
                    val email = data?.email
                    val profileImage = data?.profilePictureUrl

                    val imageUrl = "https://storage.googleapis.com/bucket-adoptify/plantixImagesUser/$profileImage"
                    profileFragment.name.text = fullName ?: name
                    profileFragment.email.text = email
                    profileFragment.cardDetail.username.text = name
                    profileFragment.cardDetail.email.text = email

                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.profile_dummy_user)
                        .into(profileFragment.profileDummy)
                }

                is Resource.Error -> {
                    showLoading(false)
                    Log.e("ProfileFragment", "error: ${it.message}")
                }
            }
        }
    }

    private fun setupListener() {
        profileFragment.icLogout.setOnClickListener {
            bottomDialog = BottomSheetDialog(requireContext()).apply {
                val view = layoutInflater.inflate(R.layout.bottom_dialog_logout, null)
                val btnClose = view.findViewById<Button>(R.id.btnBack)
                val btnLogout = view.findViewById<Button>(R.id.btnLogout)
                btnClose.setOnClickListener { dismiss() }
                btnLogout.setOnClickListener {
                    profileViewModel.deleteSession()
                    val optionsLogout = ActivityOptionsCompat.makeCustomAnimation(
                        requireContext(),
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent, optionsLogout.toBundle())
                    activity?.finish()
                }
                setCancelable(false)
                setContentView(view)
                show()
            }
        }
        profileFragment.profile.setOnClickListener {
            val options = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(),
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("USER_ID", userId)
            editProfileResultLauncher.launch(intent, options)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            shimmerFrameLayout.startShimmer()
            shimmerFrameLayout.visibility = View.VISIBLE
            profileFragment.contentProfile.visibility = View.GONE
        } else {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            profileFragment.contentProfile.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _profileFragment = null
        bottomDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        bottomDialog?.dismiss()
    }
}