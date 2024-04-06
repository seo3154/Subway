package kr.co.hanbit.subway.api

import kr.co.hanbit.subway.model.CongestionData
import kr.co.hanbit.subway.model.CongestionResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SubwayApiClient {
    // 서버의 기본 URL
    private val baseUrl = "https://localhost:55969/"

    // Retrofit 객체를 초기화합니다. Gson 컨버터를 사용하여 JSON 응답을 자동으로 파싱
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Retrofit을 사용하여 kr.co.hanbit.subway.api.SubwayAPI 인터페이스의 구현체를 생성
    private val subwayApi = retrofit.create(SubwayAPI::class.java)

    // 지하철 혼잡도 데이터를 비동기적으로 가져오는 함수
    fun getCongestionData(
        appKey: String, line: String, stationName: String,
        dayOfWeek: String?, hour: String?,
        onSuccess: (List<CongestionData>?) -> Unit, onFailure: () -> Unit
    ) {
        // kr.co.hanbit.subway.api.SubwayAPI 인터페이스의 getCongestionData 메소드를 호출하여 네트워크 요청을 준비
        val call = subwayApi.getCongestionData(appKey, line, stationName, dayOfWeek, hour)
        // 네트워크 요청을 비동기적으로 실행
        call.enqueue(object : retrofit2.Callback<CongestionResponse> {
            // 요청이 성공적으로 응답을 받았을 때 호출
            override fun onResponse(
                call: retrofit2.Call<CongestionResponse>,
                response: retrofit2.Response<CongestionResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body()?.contents?.stat?.firstOrNull()?.data)
                } else {
                    onFailure()
                }
            }

            override fun onFailure(call: retrofit2.Call<CongestionResponse>, t: Throwable) {
                onFailure()
            }

        })
    }
}
