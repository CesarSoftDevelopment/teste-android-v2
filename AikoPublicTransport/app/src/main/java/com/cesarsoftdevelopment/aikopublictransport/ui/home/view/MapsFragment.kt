package com.cesarsoftdevelopment.aikopublictransport.ui.home.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.cesarsoftdevelopment.aikopublictransport.R
import com.cesarsoftdevelopment.aikopublictransport.data.model.Objects
import com.cesarsoftdevelopment.aikopublictransport.databinding.FragmentMapsBinding
import com.cesarsoftdevelopment.aikopublictransport.ui.home.viewmodel.MapViewModel
import com.cesarsoftdevelopment.aikopublictransport.utils.ObjectConverter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding : FragmentMapsBinding
    private lateinit var mapViewModel: MapViewModel
    private lateinit var  mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var args : Objects? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_maps,
            container,
            false
        )
        initMap(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel = (activity as HomeActivity).mapViewModel
        args = MapsFragmentArgs.fromBundle(requireArguments()).objects

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            enableUserLocation()
//        } else {
//            requestPermissions(
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }

        if(args != null) {
            addMarkers(args)
        }


    }

    private fun addMarkers(args: Objects?) {
        if (args != null) {
            val vehiclePositions = args.vehicle?.vehicles
            val stopItems = args.stops
            val boundsBuilder = LatLngBounds.Builder()

            vehiclePositions?.forEach { vehicle ->
                val iconBitmapBus = BitmapDescriptorFactory.fromBitmap(
                    ObjectConverter.getBitmapFromVectorDrawable(
                        requireContext(),
                        R.drawable.ic_bus_location
                    )
                )

                if (vehicle?.vehicleLatitude != null && vehicle.vehicleLongitude != null) {
                    val position = LatLng(vehicle.vehicleLatitude, vehicle.vehicleLongitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title("Veículo ${vehicle.vehiclePrefix}")
                            .icon(iconBitmapBus)
                    )
                    boundsBuilder.include(position)
                }
            }

            stopItems?.forEach { stop ->
                val iconBitmapStops = BitmapDescriptorFactory.fromBitmap(
                    ObjectConverter.getBitmapFromVectorDrawable(
                        requireContext(),
                        R.drawable.ic_stops_location
                    )
                )

                if (stop?.stopLatitude != null && stop.stopLongitude != null) {
                    val position = LatLng(stop.stopLatitude, stop.stopLongitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(stop.stopName)
                            .snippet(stop.stopAddress)
                            .icon(iconBitmapStops)
                    )
                    boundsBuilder.include(position)
                }
            }

            if (vehiclePositions?.isNotEmpty() == true || stopItems?.isNotEmpty() == true) {
                val bounds = boundsBuilder.build()
                val padding = 100
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.animateCamera(cameraUpdate)
            }



        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //enableUserLocation()
            } else {
                hideProgressBar()
                Toast.makeText(
                    requireContext(),
                    "Não será possível localizar sua localização",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                hideProgressBar()
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                }
            }.addOnFailureListener {
                hideProgressBar()
                Toast.makeText(
                    requireContext(),
                    "Falha ao obter a localização",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }

    private fun initMap(callback: OnMapReadyCallback) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


}