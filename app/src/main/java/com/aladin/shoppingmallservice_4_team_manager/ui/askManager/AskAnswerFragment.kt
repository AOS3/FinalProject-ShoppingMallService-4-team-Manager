package com.aladin.shoppingmallservice_4_team_manager.ui.askManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentAskAnswerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskAnswerFragment : Fragment() {

    private var _fragmentAskAnswerBinding: FragmentAskAnswerBinding? = null
    private val fragmentAskAnswerBinding get() = _fragmentAskAnswerBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentAskAnswerBinding =
            FragmentAskAnswerBinding.inflate(layoutInflater, container, false)
        return fragmentAskAnswerBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentAskAnswerBinding = null
    }

}