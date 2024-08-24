package com.example.plantix.ui.welcome.first

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plantix.R
import com.example.plantix.databinding.FragmentFirstWelcomeBinding


class FirstWelcomeFragment : Fragment() {

    private var _firstWelcomeFragment: FragmentFirstWelcomeBinding? = null

    private val firstWelcomeFragment get() = _firstWelcomeFragment!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _firstWelcomeFragment = FragmentFirstWelcomeBinding.inflate(inflater, container, false)
        return _firstWelcomeFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _firstWelcomeFragment = null
    }

}