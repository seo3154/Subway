package kr.co.hanbit.subway

import SubwayAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SubwayApiClient {
    private val baseUrl = "https://api.example.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val subwayApi = retrofit.create(SubwayAPI::class.java)

    fun getCongestionData(
        appKey: String, line: String, stationName: String,
        dayOfWeek: String?, hour: String?,
        onSuccess: (List<CongestionData>?) -> Unit, onFailure: () -> Unit
    ) {
        val call = subwayApi.getCongestionData(appKey, line, stationName, dayOfWeek, hour)
        call.enqueue(object : retrofit2.Callback<CongestionResponse> {
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
