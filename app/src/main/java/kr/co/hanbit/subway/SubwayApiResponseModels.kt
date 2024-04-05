package kr.co.hanbit.subway

data class CongestionResponse(
    val status: Status,
    val contents: Contents
)

data class Status(
    val code: String,
    val message: String
)

data class Contents(
    val subwayLine: String,
    val stationName: String,
    val stationCode: String,
    val stat: List<Stat>,
    val statStartDate: String,
    val statEndDate: String
)

data class Stat(
    val startStationCode: String,
    val startStationName: String,
    val endStationCode: String,
    val endStationName: String,
    val prevStationCode: String,
    val prevStationName: String,
    val updnLine: Int,
    val directAt: Int,
    val data: List<CongestionData>
)

data class CongestionData(
    val dow: String,
    val hh: String,
    val mm: String,
    val congestionCar: List<Int>
)
