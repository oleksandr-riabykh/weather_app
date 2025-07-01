package com.life.totally.great.presentation.screens.models

import com.himanshoe.charty.line.model.LineData
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.models.ForecastWeatherItem
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.domain.models.WeatherDomainModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

const val DATE_FORMAT = "dd MMM yyyy"
const val TIME_FORMAT = "HH:mm"

fun WeatherDomainModel.toUiModel() = MainUIDataModel(
    weather = currentWeather.toUiModel(),
    forecastList = forecast.toUiModelList()
)

fun WeatherResponse.toUiModel(): WeatherUiModel {
    val weather = weather.firstOrNull()
    val date = Date(datetime * 1000)

    val formattedDate = runCatching {
        SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date)
    }.getOrNull()

    val formattedTime = runCatching {
        SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(date)
    }.getOrNull()

    return WeatherUiModel(
        coord = coord,
        main = weather?.main.orEmpty(),
        description = weather?.description.orEmpty(),
        iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}@2x.png",
        temp = main.temp,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        pressure = main.pressure,
        humidity = main.humidity,
        seaLevel = main.seaLevel,
        grndLevel = main.grndLevel,
        tempKf = main.tempKf,
        windSpeed = wind.speed,
        rainOneHourVolume = rain?.oneHourVolume ?: 0.0,
        cityName = cityName,
        dateString = formattedDate,
        timeString = formattedTime,
        datetime = datetime,
        hour = formattedTime?.split(":")?.first()?.toInt() ?: 1
    )
}

fun ForecastResponse.toUiModelList(): List<ForecastUiModel> {
    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    // Group items by date
    val groupedByDate = forecastList.groupBy { item ->
        dateFormatter.format(Date(item.dt * 1000))
    }
    return groupedByDate.map { (_, items) ->
        val timeGrouped = items.groupBy { item ->
            dateFormatter.format(Date(item.dt * 1000))
        }.mapValues { (_, timeItems) ->
            timeItems.map { it.toWeatherUiModel(city.name) }
        }

        val weatherForecastMap = timeGrouped.values.first()
        val chartData = weatherForecastMap.mapIndexed { index, item ->
            LineData(
                yValue = item.temp.roundToLong().toFloat(),
                xValue = index
            )
        }

        ForecastUiModel(
            date = timeGrouped.keys.first(),
            weatherForecastMap = weatherForecastMap,
            chartData = chartData
        )
    }
}


fun ForecastWeatherItem.toWeatherUiModel(cityName: String): WeatherUiModel {
    val weather = weather.firstOrNull()

    val date = Date(dt * 1000)
    val formattedDate = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date)
    val formattedTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(date)

    return WeatherUiModel(
        coord = Coordinates(0.0, 0.0), // Not present in forecast item
        main = weather?.main.orEmpty(),
        description = weather?.description.orEmpty(),
        iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}@2x.png",
        temp = main.temp,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        pressure = main.pressure,
        humidity = main.humidity,
        seaLevel = main.seaLevel,
        grndLevel = main.grndLevel,
        tempKf = main.tempKf,
        windSpeed = wind.speed,
        rainOneHourVolume = rain?.threeHourVolume ?: 0.0,
        cityName = cityName,
        dateString = formattedDate,
        timeString = formattedTime,
        datetime = dt,
        hour = formattedTime.split(":").first().toInt()
    )
}