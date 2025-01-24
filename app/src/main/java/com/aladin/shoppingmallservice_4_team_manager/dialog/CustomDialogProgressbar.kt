package com.aladin.shoppingmallservice_4_team_manager.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.aladin.shoppingmallservice_4_team_manager.databinding.DialogProgressbarBinding

class CustomDialogProgressbar(
    context: Context,
) : Dialog(context) {
    private lateinit var binding: DialogProgressbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogProgressbarBinding.inflate(LayoutInflater.from(context))

        initViews()
    }

    private fun initViews() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)
        setCancelable(false)
    }
}