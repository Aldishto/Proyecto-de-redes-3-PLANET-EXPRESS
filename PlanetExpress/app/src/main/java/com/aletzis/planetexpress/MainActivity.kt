package com.aletzis.planetexpress

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlin.jvm.Throws
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val ip = "10.0.2.2"
    val coords = mutableMapOf(
//      Estado      to        Longitud,Latitud
        "Aguascalientes" to "-102.278865,21.90666",
        "Baja California" to "-116.970966,32.51367",
        "Baja California Sur" to "-110.319565,24.121315",
        "Campeche" to "-90.523493,19.802695",
        "Chiapas" to "-93.102625,16.741424",
        "Chihuahua" to "-106.100408,28.629100",
        "Coahuila" to "-103.411382,25.521242",
        "Colima" to "-103.723079,19.227304",
        "Distrito Federal" to "-99.133313,19.414799",
        "Durango" to "-104.596357,23.986869",
        "Guanajuato" to "-101.665481,21.132846",
        "Guerrero" to "-99.500856,17.546218",
        "Hidalgo" to "-98.733725,20.081053",
        "Jalisco" to "-103.319027,20.588412",
        "Mexico" to "-99.031185,19.600222",
        "Michoacan" to "-101.199868,19.699546",
        "Morelos" to "-99.233139,18.930675",
        "Nayarit" to "-104.899775,21.495395",
        "Nuevo Leon" to "-100.308767,25.687356",
        "Oaxaca" to "-96.700656,17.056437",
        "Puebla" to "-98.187838,19.02858",
        "Queretaro" to "-100.402356,20.573567",
        "Quintana Roo" to "-86.836580,21.168005",
        "San Luis Potosi" to "-100.993896,22.130970",
        "Sinaloa" to "-107.398603,24.747746",
        "Sonora" to "-110.941028,29.073134",
        "Tabasco" to "-92.920340,17.981369",
        "Tamaulipas" to "-99.151634,23.722377",
        "Tlaxcala" to "-98.226562,19.320106",
        "Veracruz" to "-96.139917,19.178090",
        "Yucatan" to "-89.616465,20.971374",
        "Zacatecas" to "-102.577559,22.770366")


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapView: MapView = findViewById(R.id.mapView)
        val mapButton: Button = findViewById(R.id.button2)
        val searchButton: Button = findViewById(R.id.button)
        val origen = MutableLiveData<String>()
        val destino = MutableLiveData<String>()
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        mapView.visibility = View.GONE

        mapButton.setOnClickListener {
            mapView.visibility = View.VISIBLE
            it.visibility = View.INVISIBLE
            searchButton.visibility= View.INVISIBLE

        }

        searchButton.setOnClickListener {
            Volley.newRequestQueue(this).add(StringRequest(Request.Method.GET,"http://$ip/aletzis/envio.json",{
                val responseArray = JSONArray(JSONObject(it).getString("envios"))
                var check = false
                for (x in 0 until responseArray.length()){
                    val responseJson = responseArray.getJSONObject(x)
                    if (responseJson.getString("id_envio")==findViewById<EditText>(R.id.editTextNumber).text.toString()){
                        origen.value = responseJson.getString("origen")
                        destino.value = responseJson.getString("destino")
                        check = true
                    }
                }
                if (!check) {
                    Toast.makeText(this@MainActivity, "No se encontro", Toast.LENGTH_SHORT).show()
                    findViewById<TextView>(R.id.textView3).text = ""
                    findViewById<TextView>(R.id.textView4).text = ""
                    findViewById<TextView>(R.id.textView5).text = ""
                    findViewById<TextView>(R.id.textView6).text = ""
                }
                 },{
                    Toast.makeText(this,"que crees, fijate que no jalo, como la ves",Toast.LENGTH_SHORT).show()
                })
            )
        }

        origen.observe(this){
            findViewById<TextView>(R.id.textView3).text = it
            val coord = coords[it]?.split(',')
            Volley.newRequestQueue(this).add(StringRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?lat=${coord?.get(1)}&lon=${coord?.get(0)}&appid=3349b8e9fb6c3db751b3606d1cc054c0&lang=es&units=metric",
                {response->
                    findViewById<TextView>(R.id.textView5).text = buildString {
                        append("${JSONArray(JSONObject(response).getString("weather")).getJSONObject(0).getString("description").uppercase()}\n")
                        append("${JSONObject(JSONObject(response).getString("main")).getString("temp")} °C")
                    }
                },{
                    Toast.makeText(this,"que crees, fijate que no jalo, como la ves",Toast.LENGTH_SHORT).show()
                })
            )
        }
        destino.observe(this){
            findViewById<TextView>(R.id.textView4).text = it
            val coord = coords[it]?.split(',')
            Volley.newRequestQueue(this).add(StringRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?lat=${coord?.get(1)}&lon=${coord?.get(0)}&appid=3349b8e9fb6c3db751b3606d1cc054c0&lang=es&units=metric",
                {response->
                    findViewById<TextView>(R.id.textView6).text = buildString {
                        append("${JSONArray(JSONObject(response).getString("weather")).getJSONObject(0).getString("description").uppercase()}\n")
                        append("${JSONObject(JSONObject(response).getString("main")).getString("temp")} °C")
                    }
                },{
                    Toast.makeText(this,"que crees, fijate que no jalo, como la ves",Toast.LENGTH_SHORT).show()
                })
            )
        }

    }

    override fun onBackPressed() {

        val mapView: MapView = findViewById(R.id.mapView)
        val mapButton: Button = findViewById(R.id.button2)
        val searchButton: Button = findViewById(R.id.button)
        if (mapView.isVisible){
            mapView.visibility = View.INVISIBLE
            searchButton.visibility = View.VISIBLE
            mapButton.visibility = View.VISIBLE

        }else
            super.onBackPressed()

    }

}