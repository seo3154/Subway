package kr.co.hanbit.subway.model

import com.google.gson.annotations.SerializedName

data class CongestionResponse(
    val status: Status,
    val contents: Contents
)

data class Status(
    val code: String,
    val message: String
)

data class Contents(
    @SerializedName("subwayLine") val subwayLine: String,
    @SerializedName("stationName") val stationName: String,
    @SerializedName("stationCode") val stationCode: String,
    @SerializedName("stat") val stat: List<Stat>,
    @SerializedName("statStartDate") val statStartDate: String,
    @SerializedName("statEndDate") val statEndDate: String
)

data class Stat(
    @SerializedName("startStationCode") val startStationCode: String,
    @SerializedName("startStationName") val startStationName: String,
    @SerializedName("endStationCode") val endStationCode: String,
    @SerializedName("endStationName") val endStationName: String,
    @SerializedName("prevStationCode") val prevStationCode: String,
    @SerializedName("prevStationName") val prevStationName: String,
    @SerializedName("updnLine") val updnLine: Int,
    @SerializedName("directAt") val directAt: Int,
    @SerializedName("data") val data: List<CongestionData>
)

data class CongestionData(
    @SerializedName("dow") val dayOfWeek: String,
    @SerializedName("hh") val hour: String,
    @SerializedName("mm") val minute: String,
    @SerializedName("congestionCar") val congestionCar: List<Int>
)