package com.example.plantix.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityOptionsCompat
import com.example.core.data.Resource
import com.example.plantix.R
import com.example.plantix.databinding.FragmentProfileBinding
import com.example.plantix.ui.auth.login.LoginActivity
import com.example.plantix.ui.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _profileFragment: FragmentProfileBinding? = null

    private val profileFragment get() = _profileFragment!!

    private val profileViewModel: ProfileViewModel by viewModel()

    private val mainViewModel: MainViewModel by viewModel()

    private var bottomDialog: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _profileFragment = FragmentProfileBinding.inflate(inflater, container, false)
        return _profileFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListener()
    }

    private fun setupView() {
        mainViewModel.getUsername()
        getUsernameResult()
    }

    private fun getUsernameResult() {
        mainViewModel.username.observe(viewLifecycleOwner) { username ->
            when (username) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val name = username.data.split(" ").joinToString(separator = " ") { it.capitalize() }
                    profileFragment.name.text = name
                    profileFragment.cardDetail.username.text = name
                }

                is Resource.Error -> {
                    Log.e("ProfileFragment", "error: ${username.message}")
                }
            }
        }
    }

    private fun setupListener() {
        profileFragment.icLogout.setOnClickListener {
            bottomDialog =  BottomSheetDialog(requireContext()).apply {
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