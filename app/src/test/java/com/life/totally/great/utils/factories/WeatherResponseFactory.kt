package com.life.totally.great.utils.factories

import com.life.totally.great.data.models.Clouds
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.Main
import com.life.totally.great.data.models.Rain
import com.life.totally.great.data.models.Sys
import com.life.totally.great.data.models.Weather
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.data.models.Wind

object WeatherResponseFactory {

    fun create(
        cityName: String = "Weatherville",
        coord: Coordinates = Coordinates(12.34, 56.78),
        weather: List<Weather> = listOf(createWeather()),
        base: String = "stations",
        main: Main = createMain(),
        visibility: Int = 10000,
        wind: Wind = createWind(),
        rain: Rain? = Rain(oneHourVolume = 0.5),
        clouds: Clouds = Clouds(all = 70),
        datetime: Long = System.currentTimeMillis() / 1000,
        sys: Sys = createSys(),
        timezone: Int = 7200,
        id: Int = 999999,
        cod: Int = 200
    ): WeatherResponse {
        return WeatherResponse(
            coord = coord,
            weather = weather,
            base = base,
            main = main,
            visibility = visibility,
            wind = wind,
            rain = rain,
            clouds = clouds,
            datetime = datetime,
            sys = sys,
            timezone = timezone,
            id = id,
            cityName = cityName,
            cod = cod
        )
    }

    fun createWeather(
        id: Int = 801,
        main: String = "Clouds",
        description: String = "few clouds",
        icon: String = "02d"
    ) = Weather(id, main, description, icon)

    fun createMain(
        temp: Double = 22.0,
        feelsLike: Double = 21.5,
        tempMin: Double = 20.0,
        tempMax: Double = 24.0,
        pressure: Int = 1015,
        humidity: Int = 60,
        seaLevel: Int = 1015,
        grndLevel: Int = 1005,
        tempKf: Double? = 0.0
    ) = Main(temp, feelsLike, tempMin, tempMax, pressure, humidity, seaLevel, grndLevel, tempKf)

    fun createWind(speed: Double = 3.5, deg: Int = 180, gust: Double = 5.0) =
        Wind(speed, deg, gust)

    fun createSys(
        type: Int = 1,
        id: Int = 1234,
        country: String = "TS",
        sunrise: Long = System.currentTimeMillis() / 1000,
        sunset: Long = System.currentTimeMillis() / 1000 + 36000
    ) = Sys(type, id, country, sunrise, sunset)
}