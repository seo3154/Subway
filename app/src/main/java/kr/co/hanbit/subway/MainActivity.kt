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
import kr.co.hanbit.subway.api.SubwayApiClient
import kr.co.hanbit.subway.model.CongestionData

class MainActivity : AppCompatActivity() {
    // 확대/축소 제스처를 감지하기 위한 변수
    private var scaleGestureDetector: ScaleGestureDetector? = null
    // 현재 확대/축소 비율을 저장하는 변수
    private var scaleFactor = 1.0f
    // 확대/축소가 적용될 이미지 뷰를 참조하기 위한 변수
    private lateinit var imageView: ImageView
    // 지하철 혼잡도 데이터를 가져오기 위한 API 클라이언트 객체
    private lateinit var subwayApiClient: SubwayApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 시스템 바(상태 바, 네비게이션 바 등)의 크기에 따라 뷰의 패딩을 조정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 이미지 뷰와 스케일 제스처 디텍터를 초기화
        imageView = findViewById(R.id.img_subway)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // 지하철 API 클라이언트를 초기화하고 혼잡도 데이터를 요청
        subwayApiClient = SubwayApiClient()
        getCongestionData("강남역")
    }

    // 화면 터치 이벤트를 처리, 확대/축소 제스처가 감지될 경우 이를 처리
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event?.let {
            scaleGestureDetector?.onTouchEvent(it) ?: false
        } ?: false
    }

    // 지하철 혼잡도 데이터를 가져오는 함수,  성공 또는 실패 콜백을 통해 결과를 처리
    private fun getCongestionData(stationName: String) {
        val appKey = "EtTpHsgEB%2FZV1sGf3rvsBnLHdnT%2B%2FZDc6fKVsO%2FvtvR63DiwXpFn6M6YvDXJ1zBKIumHEZ1Z3540ru9djZXerA%3D%3D"
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

    // 혼잡도 데이터를 처리하는 함수, 로그에 혼잡도 정보를 출력
    private fun processCongestionData(congestionData: List<CongestionData>?) {
        val firstData = congestionData?.firstOrNull()
        val congestionCars = firstData?.congestionCar ?: emptyList()
        val congestionText = congestionCars.joinToString(", ")
        Log.d("Congestion", "강남역의 혼잡도: $congestionText")
    }


    // 데이터를 가져오는 데 실패했을 때 호출되는 함수, 오류 메시지를 로그에 출력
    private fun showError() {
        Log.e("Congestion", "혼잡도 데이터를 가져오지 못했습니다.")
    }

    // 확대/축소 제스처를 처리하는 리스너 클래스
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
