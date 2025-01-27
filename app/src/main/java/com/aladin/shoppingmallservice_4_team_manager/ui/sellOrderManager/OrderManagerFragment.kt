package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentOrderManagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderManagerFragment : Fragment() {

    private var _fragmentOrderManagerBinding: FragmentOrderManagerBinding? = null
    private val fragmentOrderManagerBinding get() = _fragmentOrderManagerBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentOrderManagerBinding =
            FragmentOrderManagerBinding.inflate(layoutInflater, container, false)
        return fragmentOrderManagerBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentOrderManagerBinding = null
    }

}