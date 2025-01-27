package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentSellManagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellManagerFragment : Fragment() {

    private var _fragmentSellManagerBinding: FragmentSellManagerBinding? = null
    private val fragmentSellManagerBinding get() = _fragmentSellManagerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentSellManagerBinding =
            FragmentSellManagerBinding.inflate(layoutInflater, container, false)
        return fragmentSellManagerBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentSellManagerBinding = null
    }

}