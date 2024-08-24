package com.example.plantix.ui.scan.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plantix.R
import com.example.plantix.databinding.FragmentListScanBinding
import com.example.plantix.ui.scan.detail.DetailScanActivity
import com.example.plantix.ui.scan.detection.ScanActivity
import org.koin.android.ext.android.bind


class ListScanFragment : Fragment() {

    private var _scanFragment: FragmentListScanBinding? = null

    private val scanFragment get() = _scanFragment!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _scanFragment = FragmentListScanBinding.inflate(inflater, container, false)
        return _scanFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
    }

    private fun setupListener() {
//        scanFragment.listCard.card.setOnClickListener { startActivity(Intent(requireContext(), DetailScanActivity::class.java)) }
//        scanFragment.btnScan.setOnClickListener { startActivity(Intent(requireContext(), ScanActivity::class.java)) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _scanFragment = null
    }

}