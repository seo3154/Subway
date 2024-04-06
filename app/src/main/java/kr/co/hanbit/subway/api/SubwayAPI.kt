package kr.co.hanbit.subway.api

import kr.co.hanbit.subway.model.CongestionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 서버로부터 지하철 혼잡도 데이터를 가져오기 위한 Retrofit API 인터페이스
interface SubwayAPI {
    // GET HTTP 요청을 사용하여 지하철 혼잡도 데이터를 가져오는 엔드포인트를 정의
    @GET("/transit/subway/congestion")
    fun getCongestionData(
        // API 요청에 필요한 앱 키를 헤더에 포함
        @Header("appKey") appKey: String,
        // 노선(호선) 정보
        @Query("line") line: String,
        // 역 이름
        @Query("stationName") stationName: String,
        // 요일 정보
        @Query("dayOfWeek") dayOfWeek: String?,
        // 시간 정보
        @Query("hour") hour: String?
    ): Call<CongestionResponse>
}
