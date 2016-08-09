package org.moe.kotlinmuseummap.android

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import org.moe.kotlinmuseummap.common.MuseumSearchEngine
import java.util.*

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.moe.kotlinmuseummap.android.db.AndroidSQLiteDatabaseHelper
import org.moe.kotlinmuseummap.common.model.Museum
import org.moe.kotlinmuseummap.common.model.db.DataSource

class MainActivity : AppCompatActivity() {

    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var nameMarker: EditText? = null
    private var source: DataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMap = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMap()

        val ctx = applicationContext
        source = DataSource(AndroidSQLiteDatabaseHelper(ctx, "local.db"))
        source!!.open()

        nameMarker = findViewById(R.id.editText) as EditText

        mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                currentMarker = marker
                nameMarker!!.setText(marker.getTitle())
                return false
            }
        })

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                currentMarker = null
            }
        })

        val addButton = findViewById(R.id.btnPlus) as Button
        val removeButton = findViewById(R.id.btnMinus) as Button
        val updateButton = findViewById(R.id.btnUpdate) as Button

        addButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (mMap == null)
                    return

                mMap!!.addMarker(MarkerOptions().position(mMap!!.getCameraPosition().target).title(nameMarker!!.text.toString()))

                source!!.createMuseum(Museum(nameMarker!!.text.toString(),
                        mMap!!.getCameraPosition().target.latitude,
                        mMap!!.getCameraPosition().target.longitude))
            }
        })

        removeButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (mMap == null || currentMarker == null)
                    return


                val museums = source!!.getMuseumsByAllParameters(currentMarker!!.title,
                        currentMarker!!.position.latitude, currentMarker!!.position.longitude)
                for (museum in museums) {
                    source!!.deleteMuseum(museum.id)
                }

                currentMarker!!.remove()
                currentMarker = null
            }
        })

        updateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val centerCoordinate = mMap!!.getCameraPosition().target

                val loadTaskParams = arrayOf<Double>(centerCoordinate.latitude, centerCoordinate.longitude)
                val loader = LoadTask()
                loader.execute(*loadTaskParams)
            }
        })

        val museums = source!!.allMuseum
        println(museums!!.size)
        for (museum in museums) {
            mMap!!.addMarker(MarkerOptions().position(LatLng(museum.latitude, museum.longitude)).title(museum.name))
        }
    }

    private inner class LoadTask : AsyncTask<Double, Void, Void>() {

        private var museums: ArrayList<Museum>? = null
        private var errorMessage :String? = ""


        protected override fun doInBackground(vararg params: Double?): Void? {
            museums = MuseumSearchEngine.find(params[0] as Double, params[1] as Double)
            if (museums == null) {
                errorMessage = MuseumSearchEngine.lastError
            } else {
                for(i in 0..museums!!.size - 1) {
                    var existMuseums = source!!.getMuseumsByAllParameters(museums!![i].name,
                            museums!![i].latitude,
                            museums!![i].longitude)
                    if (existMuseums.size !== 0) {
                        museums!![i].id = existMuseums[0].id
                    }
                }
            }

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            if (errorMessage == "") {
                for (museum in museums!!) {
                    if (museum.id == -1) {
                        mMap!!.addMarker(MarkerOptions().position(LatLng(museum.latitude, museum.longitude)).title(museum.name))
                    }
                    source!!.createMuseum(museum)
                }
            } else
                handleError(errorMessage)
        }
    }

    private fun handleError(errorMessage: String?) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Error!").setMessage("Cannot get museums: " + errorMessage).setCancelable(false).setNegativeButton("OK",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        dialog.cancel()
                    }
                })
        val alert = builder.create()
        alert.show()
    }
}