package com.example.covidvaccine

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.covidvaccine.adapters.CenterAdapter
import com.example.covidvaccine.databinding.ActivityMainBinding
import com.example.covidvaccine.models.Center
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var adapter:CenterAdapter
    private lateinit var centerList: List<Center>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        centerList = ArrayList()
        binding.btnSearch.setOnClickListener {
            val pinCode = binding.etPincode.text.toString()
            if(pinCode.length!=6){
                Toast.makeText(this@MainActivity,"Please Enter a valid pincode",Toast.LENGTH_LONG).show()
                binding.etPincode.text.clear()
            } else {
                (centerList as ArrayList<Center>).clear()
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this,
                    { _, year, monthOfYear, dayOfMonth ->
                        // after that we are making our progress bar as visible.
                        binding.progressBar.visibility = View.VISIBLE

                        // on below line we are creating a date string for our date
                        val dateStr = """$dayOfMonth - ${monthOfYear + 1} - $year"""
                        getAppointmentDetails(pinCode,dateStr)
                    },
                    year,
                    month,
                    day,
                )
                datePickerDialog.show()
            }
        }
    }

    private fun getAppointmentDetails(pinCode: String, date: String) {
        val url =
            "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=$pinCode&date=$date"
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
               try {
                   val centerArray = response.getJSONArray("centers")
                   binding.progressBar.visibility = View.GONE
                   if(centerArray.length() == 0){
                       Toast.makeText(this@MainActivity, "Sorry!! No Vaccination Centers Available", Toast.LENGTH_SHORT).show()
                   }
                   for(i in 0 until centerArray.length()){
                       val centerObj = centerArray.getJSONObject(i)
                       val centerName = centerObj.getString("name")
                       val centerAddress = centerObj.getString("address")
                       val centerFromTime = centerObj.getString("from")
                       val centerToTime = centerObj.getString("to")
                       val feeType = centerObj.getString("fee_type")
                       val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                       val availableCapacity = sessionObj.getInt("available_capacity")
                       val ageLimit = sessionObj.getInt("min_age_limit")
                       val vaccineName = sessionObj.getString("vaccine")
                       val center = Center(
                           centerName,
                           centerAddress,
                           centerFromTime,
                           centerToTime,
                           feeType,
                           ageLimit,
                           vaccineName,
                           availableCapacity
                       )
                       centerList = centerList+center
                   }
                   adapter = CenterAdapter(centerList)
                   binding.recyclerView.layoutManager = LinearLayoutManager(this)
                   binding.recyclerView.adapter = adapter
               } catch (e:JSONException){
                   e.printStackTrace()
               }
            },
            { error ->
                // this method is called when we get any
                // error while fetching data from our API
                Log.e("TAG", "RESPONSE IS $error")
                // in this case we are simply displaying a toast message.
                Toast.makeText(this@MainActivity, "Fail to get response", Toast.LENGTH_SHORT).show()
            })
        queue.add(request)
    }
}