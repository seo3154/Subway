package kr.co.hanbit.subway

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView
    private lateinit var subwayApiClient: SubwayApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.img_subway)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        subwayApiClient = SubwayApiClient()
        getCongestionData("강남역")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event?.let {
            scaleGestureDetector?.onTouchEvent(it) ?: false
        } ?: false
    }

    private fun getCongestionData(stationName: String) {
        val appKey = "8VgP2UZ1S88ohut6rzj6h55mXzzpUP1i1Oqozwe8"
        subwayApiClient.getCongestionData(appKey, "1호선", stationName, null, null,
            onSuccess = { congestionData ->
                congestionData?.let {
                    processCongestionData(it)
                } ?: run {
                    showError()
                }
            },
            onFailure = {
                showError()
            }
        )
    }

    // 이 부분은 processCongestionData 함수가 List<CongestionData>? 타입을 처리할 수 있도록 보장해야 합니다.
// 예시:
    private fun processCongestionData(congestionData: List<CongestionData>) {
        congestionData?.let {
            // 처리 로직
        val firstData = congestionData.firstOrNull()
        val congestionCars = firstData?.congestionCar ?: emptyList()
        val congestionText = congestionCars.joinToString(", ")
        Log.d("Congestion", "강남역의 혼잡도: $congestionText")
        }
    }

    private fun showError() {
        Log.e("Congestion", "혼잡도 데이터를 가져오지 못했습니다.")
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(1.0f, 9.0f)
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }
}
