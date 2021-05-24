package com.example.covidvaccine.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidvaccine.R
import com.example.covidvaccine.models.Center

class CenterAdapter (private val centerList: List<Center>) :
    RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {

    class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val centerNameTv :TextView = itemView.findViewById(R.id.tvCenterName)
        val centerAddressTv :TextView = itemView.findViewById(R.id.tvCenterLocation)
        val centerTimingsTv :TextView = itemView.findViewById(R.id.tvCenterTimings)
        val vaccineNameTv :TextView = itemView.findViewById(R.id.tvVaccineName)
        val vaccineFeesTv :TextView = itemView.findViewById(R.id.tvVaccineFees)
        val ageLimitTv :TextView = itemView.findViewById(R.id.tvAgeLimit)
        val vaccineAvailabilityTv :TextView = itemView.findViewById(R.id.tvVaccineAvailability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return CenterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centerList[position]
        holder.centerNameTv.text = center.centerName
        holder.centerAddressTv.text = center.centerAddress
        holder.centerTimingsTv.text = ("From : "+center.centerFromTime+" To : "+center.centerToTime)
        holder.vaccineNameTv.text = center.vaccineName
        holder.vaccineFeesTv.text = center.feeType
        holder.ageLimitTv.text = ("Age Limit : "+center.ageLimit.toString())
        holder.vaccineAvailabilityTv.text = ("Availability : "+center.availableCapacity.toString())
    }

    override fun getItemCount(): Int {
        return centerList.size
    }
}