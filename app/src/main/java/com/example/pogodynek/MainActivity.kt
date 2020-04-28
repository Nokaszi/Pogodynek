package com.example.pogodynek

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*

class MainActivity : AppCompatActivity() {
    var CITY: String = "Gliwice"
    val API: String = "23585510a58605be88aadb559a58707b"
    val format=SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formatSun=SimpleDateFormat("HH:mm", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.temp).text = "Start"
        var buton=findViewById<Button>(R.id.button)
        weatherTask().execute()
        button.setOnClickListener{
            CITY=findViewById<TextView>(R.id.name).text.toString()
            weatherTask().execute()
        }


    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API&lang=pl").readText(
                        Charsets.UTF_8

                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }
        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                format.timeZone= TimeZone.getDefault()
                formatSun.timeZone= TimeZone.getDefault()
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val temp = main.getString("temp") + "°C"
                val updatedAtText =format.format(Date(updatedAt*1000))
                val pressure = main.getString("pressure")
                val icon:String =weather.getString("icon")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")

                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.name).text = address
                findViewById<TextView>(R.id.temp).text = "Temperatura:"+temp
                findViewById<TextView>(R.id.pressure).text="Ciśnienie "+pressure+"hPa"
                findViewById<TextView>(R.id.description).text=weatherDescription.capitalize()
                findViewById<TextView>(R.id.sunrise).text= "Wschód "+formatSun.format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text="Zachód "+formatSun.format(Date(sunset*1000))
                findViewById<TextView>(R.id.time).text="Aktualizowano:"+updatedAtText
                when(icon){
                    "01n" -> Picasso.with(this@MainActivity).load(R.drawable.p01n).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "01d" -> Picasso.with(this@MainActivity).load(R.drawable.p01d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "02n" -> Picasso.with(this@MainActivity).load(R.drawable.p02n).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "02d" -> Picasso.with(this@MainActivity).load(R.drawable.p02d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "03d","03n" -> Picasso.with(this@MainActivity).load(R.drawable.p03d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "04d","04n" -> Picasso.with(this@MainActivity).load(R.drawable.p04d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "09d","09n" -> Picasso.with(this@MainActivity).load(R.drawable.p09d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "10d" -> Picasso.with(this@MainActivity).load(R.drawable.p10d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "10n" -> Picasso.with(this@MainActivity).load(R.drawable.p10n).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "11d","11n" -> Picasso.with(this@MainActivity).load(R.drawable.p11d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "13d","13n" -> Picasso.with(this@MainActivity).load(R.drawable.p13d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))
                    "50d","50n" -> Picasso.with(this@MainActivity).load(R.drawable.p50d).resize(300,300).into(findViewById<ImageView>(R.id.imageView))


                }




            }
            catch (e: Exception) {
                findViewById<TextView>(R.id.temp).text = "Bad"
            }
        }
    }
}

