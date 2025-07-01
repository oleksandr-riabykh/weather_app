package com.life.totally.great.utils.factories

import com.life.totally.great.data.models.City
import com.life.totally.great.data.models.Clouds
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.models.ForecastWeatherItem
import com.life.totally.great.data.models.Main
import com.life.totally.great.data.models.Sys
import com.life.totally.great.data.models.Weather
import com.life.totally.great.data.models.Wind

object ForecastResponseFactory {

    fun create(
        cod: String = "200",
        message: Int = 0,
        cnt: Int = 1,
        forecastList: List<ForecastWeatherItem> = listOf(createForecastItem()),
        city: City = createCity("Testopolis")
    ): ForecastResponse {
        return ForecastResponse(
            cod = cod,
            message = message,
            cnt = cnt,
            forecastList = forecastList,
            city = city
        )
    }

    fun createForecastItem(
        dt: Long = System.currentTimeMillis() / 1000,
        temp: Double = 23.5,
        description: String = "clear sky",
        icon: String = "01d"
    ): ForecastWeatherItem {
        return ForecastWeatherItem(
            dt = dt,
            main = Main(
                temp = temp,
                feelsLike = temp - 1,
                tempMin = temp - 2,
                tempMax = temp + 2,
                pressure = 1013,
                humidity = 60,
                seaLevel = 1013,
                grndLevel = 1005,
                tempKf = 0.0
            ),
            weather = listOf(
                Weather(id = 800, main = "Clear", description = description, icon = icon)
            ),
            clouds = Clouds(all = 10),
            wind = Wind(speed = 3.5, deg = 270, gust = 5.2),
            visibility = 10000,
            pop = 0.0,
            rain = null,
            sys = Sys(1, 1, "", 1, 1),
            dtTxt = "date"
        )
    }

    fun createCity(name: String): City {
        return City(
            id = 123456,
            name = name,
            coord = Coordinates(lat = 50.0, lon = 8.0),
            country = "TS",
            population = 1000000,
            timezone = 7200,
            sunrise = System.currentTimeMillis() / 1000,
            sunset = System.currentTimeMillis() / 1000 + 36000
        )
    }
}
