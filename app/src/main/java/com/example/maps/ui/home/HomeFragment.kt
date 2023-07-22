package com.example.maps.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.maps.R
import com.example.maps.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var editTextLatitude: EditText
    private lateinit var editTextLongitude: EditText

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        // Get the SupportMapFragment and obtain the GoogleMap
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map

            // Set initial map location (e.g., San Francisco)
            val initialLocation = LatLng(37.7749, -122.4194)
            val zoomLevel = 12.0f
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, zoomLevel))

            // Add a marker at the initial location (optional)
            googleMap.addMarker(
                MarkerOptions().position(initialLocation).title("Marker at San Francisco")
            )

            // Add an OnMapClickListener to the GoogleMap instance
            googleMap.setOnMapClickListener { latLng ->
                // When the map is clicked, update the EditText fields and recenter the map
                editTextLatitude.setText(latLng.latitude.toString())
                editTextLongitude.setText(latLng.longitude.toString())
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
                googleMap.clear() // Clear previous marker
                googleMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
            }
        }

        editTextLatitude = root.findViewById(R.id.editTextLatitude)
        editTextLongitude = root.findViewById(R.id.editTextLongitude)

        // Listen for changes in the EditText fields and update the marker accordingly
        editTextLatitude.addTextChangedListener(textWatcher)
        editTextLongitude.addTextChangedListener(textWatcher)

        return root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not used
        }

        override fun afterTextChanged(s: Editable?) {
            onTextChanged()
        }
    }

    private fun onTextChanged() {
        val latitude = editTextLatitude.text.toString().toDoubleOrNull()
        val longitude = editTextLongitude.text.toString().toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val newLocation = LatLng(latitude, longitude)
            googleMap.clear() // Clear previous marker
            googleMap.addMarker(MarkerOptions().position(newLocation).title("New Marker"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12.0f))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
