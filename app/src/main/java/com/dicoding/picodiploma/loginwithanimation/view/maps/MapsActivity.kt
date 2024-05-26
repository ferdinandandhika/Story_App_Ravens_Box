package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var _binding: ActivityMapsBinding? = null
    private lateinit var mapsViewModel: MapsViewModel
    private var isMapLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.listStories.observe(this) { stories ->
            val builder = LatLngBounds.Builder()

            for (story in stories) {
                val location = LatLng(story.lat, story.lon)
                mMap.addMarker(
                    MarkerOptions().position(location)
                        .title(story.name)
                        .snippet(story.description)
                )
                builder.include(location)
            }

            if (stories.isNotEmpty()) {
                val bounds = builder.build()
                val padding = 100
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.animateCamera(cu)
            }
            isMapLoaded = true
        }
    }
}