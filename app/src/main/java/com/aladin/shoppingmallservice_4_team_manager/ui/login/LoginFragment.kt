package com.aladin.shoppingmallservice_4_team_manager.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentLoginBinding
import com.aladin.shoppingmallservice_4_team_manager.ui.main.MainFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceMainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val fragmentLoginBinding get() = _fragmentLoginBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        // 애니메이션
        animateImageView()
        // 로그인 버튼 클릭
        onClickLogin()
        return fragmentLoginBinding.root
    }

    // Splash
    private fun animateImageView() {
        // 초기 상태를 투명하게 설정
        fragmentLoginBinding.apply {
            imageViewMainLogo.alpha = 0f
            imageViewMainLogo.visibility = View.VISIBLE

            // 3초 동안 서서히 나타나게 하는 애니메이션 실행
            imageViewMainLogo.animate()
                .alpha(1f) // 완전히 나타나도록 alpha 값을 1로 설정
                .setDuration(3000) // 애니메이션 지속 시간: 3초
                .withEndAction {
                    imageViewMainLogo.visibility = View.GONE
                    linearLayoutLogin.visibility = View.VISIBLE
                }
                .start()
        }
    }

    // onClickLoginCode
    private fun onClickLogin() {
        fragmentLoginBinding.apply {
            buttonLoginManager.setOnClickListener {
                if (textFieldLoginManagerCode.editText?.text.toString() == "1234") {
                    textFieldLoginManagerCode.error = null
                    replaceMainFragment(MainFragment(), false)
                } else {
                    textFieldLoginManagerCode.error = "관리자 코드를 다시 한번 확인해주세요."
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentLoginBinding = null
    }
}