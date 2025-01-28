package com.aladin.shoppingmallservice_4_team_manager

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aladin.shoppingmallservice_4_team_manager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    // 뒤로 가기 버튼 처리에 필요한 변수 선언
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
    //  ANR (Application Not Responding) 오류발생 하여 백그라운드에서 처리
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let { event ->
            currentFocus?.let { focusedView ->
                val rect = Rect()
                focusedView.getGlobalVisibleRect(rect)

                if (!rect.contains(event.x.toInt(), event.y.toInt())) {
                    // 키보드를 내리고 포커스를 해제하는 작업을 백그라운드에서 처리
                    Handler(Looper.getMainLooper()).post {
                        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
                        focusedView.clearFocus()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        // OnBackPressedDispatcher를 사용하여 뒤로 가기 버튼 처리
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                // 스택이 있을 경우, 스택 제거
                supportFragmentManager.popBackStack()
            } else {
                // 스택이 비어 있을 경우
                val currentTime = System.currentTimeMillis()
                if (currentTime - backPressedTime <= 3000) {
                    // 3초안에 또 누를시 앱 종료
                    exitProcess(0)
                } else {
                    backPressedTime = currentTime
                    Toast.makeText(this@MainActivity, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}