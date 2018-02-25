package com.alexramsayer.smartcollar

import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import kotlinx.android.synthetic.main.activity_map.*

var continueToAdd = false
var firstClick = true
var poly : Polygon? = null
var firstAdd = true

var newShape : ArrayList<LatLng>? = null


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        mMap.moveCamera((CameraUpdateFactory.zoomTo(18.5f)))
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(43.6282384, -116.2585939)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        newShape = ArrayList<LatLng>()

        mMap.setOnMapClickListener { point : LatLng ->

            if(firstClick){
                newShape?.add(LatLng(point.latitude, point.longitude))
                firstClick = false
            }

            else if(continueToAdd){
                newShape?.add(LatLng(point.latitude, point.longitude))
                addBoundary(View(this@MapsActivity))
            }
        }

        setUpPolygons()

    }


    fun leashMode(view: View){

    }

    fun addBoundary(view: View){
        if(!firstClick){
            if(!firstAdd) {
               poly?.points = newShape
            }
            else {poly = mMap.addPolygon(PolygonOptions().addAll(newShape).fillColor(getColor(R.color.pRed)).strokeColor(getColor(R.color.lRed))); firstAdd = false}
        }
        else{
            continueToAdd = true
            confirm_bounds.visibility = View.VISIBLE
            multiple_actions.collapse()
            multiple_actions.visibility = View.GONE
        }
    }

    fun setUpPolygons(){

      //  val houseOptions = PolygonOptions().add(LatLng())
       // val housePoly = mMap.addPolygon(houseOptions)

       // val kitchenOptions = PolygonOptions().add(LatLng())
       // val kitchenPoly = mMap.addPolygon(kitchenOptions)

    }

    fun finishBoundary(view: View){
        firstClick = true
        continueToAdd = false
        firstAdd = true
        confirm_bounds.visibility = View.GONE
        newShape?.removeAll(newShape!!)
        val alert = AlertDialog.Builder(this@MapsActivity).create()
        alert.setTitle("Name your new GeoFence:")
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", { _, _ ->
            alert.cancel()
        })
        alert.show()
    }

}
