package kr.co.hanbit.subway

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.co.hanbit.subway.api.SubwayApiClient
import kr.co.hanbit.subway.model.CongestionData

class SubwaySearchActivity : AppCompatActivity() {

    private lateinit var subwayApiClient: SubwayApiClient
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subway_search)

        textView = findViewById(R.id.congestionTextView)
        subwayApiClient = SubwayApiClient()

        val calendar = Calendar.getInstance()
        val dayOfWeek = convertDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString()

        // 예제: "강남역"의 혼잡도를 검색
        searchCongestionData("강남역")
    }
    private fun convertDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            Calendar.SUNDAY -> "일"
            else -> "월" // 기본값
        }
    }

    private fun searchCongestionData(stationName: String) {
        val appKey = "EtTpHsgEB%2FZV1sGf3rvsBnLHdnT%2B%2FZDc6fKVsO%2FvtvR63DiwXpFn6M6YvDXJ1zBKIumHEZ1Z3540ru9djZXerA%3D%3D" // 실제 API 키 사용
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

    private fun processCongestionData(congestionData: List<CongestionData>) {
        congestionData.firstOrNull()?.let { data ->
            val congestionText = data.congestionCar.joinToString(", ")
            runOnUiThread {
                textView.text = "혼잡도: $congestionText"
            }
            Log.d("Congestion", "혼잡도: $congestionText")
        } ?: showError()
    }


    private fun showError() {
        runOnUiThread {
            textView.text = "혼잡도 데이터를 가져오는 데 실패했습니다."
        }
        Log.e("Congestion", "혼잡도 데이터를 가져오는 데 실패했습니다.")
    }
}