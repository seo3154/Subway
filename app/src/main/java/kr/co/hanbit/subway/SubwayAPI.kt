

import kr.co.hanbit.subway.CongestionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SubwayAPI {
    @GET("/transit/subway/congestion")
    fun getCongestionData(
        @Header("appKey") appKey: String,
        @Query("line") line: String,
        @Query("stationName") stationName: String,
        @Query("dayOfWeek") dayOfWeek: String?,
        @Query("hour") hour: String?
    ): Call<CongestionResponse>
}
