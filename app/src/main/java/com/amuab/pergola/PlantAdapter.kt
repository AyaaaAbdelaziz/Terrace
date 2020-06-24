package com.amuab.pergola

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.amuab.pergola.notify.NotificationHelper
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class PlantAdapter(val mctx:Context,val layoutResId:Int,val plantlist:List<Plants>)
    :ArrayAdapter<Plants>(mctx,layoutResId,plantlist){
    private lateinit var auth: FirebaseAuth

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val textView = view.findViewById<TextView>(R.id.textView6)
        val watertim = view.findViewById<TextView>(R.id.watertime)
        val imgView = view.findViewById<ImageView>(R.id.ReadimgView)

        auth = FirebaseAuth.getInstance()

        val plants = plantlist[position]
            textView.text = plants.pname
        Glide.with(imgView).load(plants.pimg) .override(100, 200)
            .centerCrop().into(imgView)
             watertim.text=plants.pwater.toString()

      //  NotificationHelper.createNotificationForPet(context, reminderData)


        return view

    }}