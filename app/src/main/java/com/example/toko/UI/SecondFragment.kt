package com.example.toko.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.toko.Application.ShopApplication
import com.example.toko.R
import com.example.toko.databinding.FragmentSecondBinding
import com.example.toko.model.Shop
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val shopViewModel: ShopViewModel by viewModels {
        ShopViewModelFactory((applicationContext as ShopApplication).repository)
    }
    private val args: SecondFragmentArgs by navArgs()
    private var shop:Shop? = null
    private lateinit var mMap:GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cameraRequestCode=2

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext=requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        shop=args.shop

        if (shop != null){
            binding.delete.visibility=View.VISIBLE
            binding.simpan.text="Ubah"
            binding.namesosistext.setText(shop?.name)
            binding.editprice.setText(shop?.price)
            binding.notetextedit.setText(shop?.note)
        }

        //binding googlemap
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()

        val name = binding.namesosistext.text
        val price = binding.editprice.text
        val note = binding.notetextedit.text
        binding.simpan.setOnClickListener {

            if (name.isEmpty()){
                Toast.makeText(context,"Nama Tidak Boleh Kosong",Toast.LENGTH_SHORT).show()
            }else if (price.isEmpty()){
                Toast.makeText(context,"Harga Tidak Boleh Kosong",Toast.LENGTH_SHORT).show()
            }else if (note.isEmpty()){
                Toast.makeText(context,"Keterangan Tidak Boleh Kosong",Toast.LENGTH_SHORT).show()
            }
            else{
                if (shop==null){
                    val shop =Shop(0,name.toString(),price.toString(),note.toString(), currentLatLang?.latitude,currentLatLang?.longitude)
                    shopViewModel.insert(shop)
                }else{
                    val shop =Shop(shop?.id!!,name.toString(),price.toString(),note.toString(),currentLatLang?.latitude,currentLatLang?.longitude)
                    shopViewModel.update(shop)
                }

                findNavController().popBackStack()//untuk dissmiss halaman ini

            }
        }
        binding.delete.setOnClickListener {
            shop?.let { shopViewModel.delete(it) }
            findNavController().popBackStack()//untuk dissmiss halaman ini
        }
        binding.kamera.setOnClickListener{
            checkCameraPermission()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap =googleMap
        //implementasi drag marker


        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled=true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
       val newPosition = marker.position
        currentLatLang= LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {}

    private fun checkPermission(){
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            )   == PackageManager.PERMISSION_GRANTED
        ){
        getCurrentLocation()
        }else{
            Toast.makeText(applicationContext, "Akses lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCurrentLocation(){
        //cek jika permision ditolak maka akan berhenti di kondisi if
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            )   != PackageManager.PERMISSION_GRANTED
        ){
        return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location!=null){
                    var latLng=LatLng(location.latitude, location.longitude)
                    currentLatLang=latLng
                    var title ="Marker"
        //menampilkan lokasi sesuai koordinat yang sudah disimpan / diupdate

        if (shop!=null){
            title =shop?.name.toString()
            val newCurrentLocation = LatLng(shop?.latitude!!, shop?.longitude!!)
            latLng=newCurrentLocation
        }
        val markerOption = MarkerOptions()
            .position(latLng)
            .title(title)
            .draggable(true)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.guitar))
        mMap.addMarker(markerOption)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.CAMERA),
                    cameraRequestCode
                )
            }
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestCode){
            val photo : Bitmap = data?.extras?.get("data") as Bitmap
            binding.photoview.setImageBitmap(photo)
        }
    }
}