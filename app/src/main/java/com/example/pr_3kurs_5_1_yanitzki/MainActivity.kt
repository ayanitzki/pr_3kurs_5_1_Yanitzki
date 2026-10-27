package com.example.pr_3kurs_5_1_yanitzki

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var cityInput: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvWindSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация элементов интерфейса
        cityInput = findViewById(R.id.cityInput)
        btnGetWeather = findViewById(R.id.btnGetWeather)
        tvCity = findViewById(R.id.tvCity)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvPressure = findViewById(R.id.tvPressure)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)

        btnGetWeather.setOnClickListener {
            val cityName = cityInput.text.toString().trim()
            if (cityName.isNotEmpty()) {
                getResult(cityName)
            } else {
                val sn = Snackbar.make(findViewById(android.R.id.content), "Введите название города!", Snackbar.LENGTH_LONG)
                sn.setActionTextColor(Color.RED)
                sn.show()
            }
        }
    }

    private fun getResult(city: String) {
        val key = "691959e22c56e204cd2720e98465797d" // Ключ из методички
        val url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&units=metric&lang=ru"

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    // Парсинг корневого JSON-объекта
                    val obj = JSONObject(response)

                    // Извлечение данных объекта "main" (температура, давление)
                    val mainObj = obj.getJSONObject("main")
                    val temp = mainObj.getDouble("temp")
                    val pressure = mainObj.getInt("pressure")

                    // Извлечение данных объекта "wind" (скорость ветра)
                    val windObj = obj.getJSONObject("wind")
                    val windSpeed = windObj.getDouble("speed")

                    // Извлечение названия города из JSON
                    val nameCity = obj.getString("name")

                    // Вывод в Logcat для демонстрации (как требуется в задании)
                    Log.d("MyLog", "Город: $nameCity, Температура: $temp°C, Давление: $pressure гПа, Ветер: $windSpeed м/с")

                    // Отображение данных на экране телефона
                    tvCity.text = "Город: $nameCity"
                    tvTemperature.text = "Температура: $temp °C"
                    tvPressure.text = "Давление: $pressure гПа"
                    tvWindSpeed.text = "Скорость ветра: $windSpeed м/с"

                } catch (e: Exception) {
                    Log.d("MyLog", "Ошибка парсинга JSON: ${e.message}")
                }
            },
            { error ->
                Log.d("MyLog", "Ошибка Volley: $error")
                val sn = Snackbar.make(findViewById(android.R.id.content), "Ошибка сети или город не найден", Snackbar.LENGTH_LONG)
                sn.show()
            }
        )
        queue.add(stringRequest)
    }
}