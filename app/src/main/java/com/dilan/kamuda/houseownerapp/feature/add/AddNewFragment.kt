package com.dilan.kamuda.houseownerapp.feature.add

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dilan.kamuda.houseownerapp.databinding.FragmentAddNewBinding
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class AddNewFragment : Fragment() {

    lateinit var binding: FragmentAddNewBinding
    private lateinit var viewModel: AddNewViewModel
    private var imageBytes: ByteArray? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val galleryUri = result.data?.data
                try {
                    galleryUri?.let {
                        binding.ivChooseItemImg.setImageURI(it)
                        // Convert the selected image to a byte array
                        imageBytes = convertImageUriToByteArray(it)
                        Log.e("IMAGE", "img-size: ${imageBytes?.size}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private fun convertImageUriToByteArray(imageUri: Uri): ByteArray? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            //inputStream?.readBytes()
            // Resize the image before converting to a byte array
            val options = BitmapFactory.Options()
            options.inSampleSize = 2 // Adjust this value as needed to control image size
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)

            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AddNewViewModel::class.java]
        binding.addNewVM = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchStatus.isChecked = false
        binding.btnAddNewMenu.setOnClickListener {
            val itemName = binding.mtvNewItemName.text.toString()
            val itemPrice = binding.mtvNewItemUnitPrice.text.toString().toDoubleOrNull() ?: 0.0
            val itemStatus = if (binding.switchStatus.isChecked) "Y" else "N"

            val body = FoodMenu(-1, itemName, itemPrice, itemStatus, imageBytes)
            try {
                viewModel.saveNewItem(body)
            } catch (e: Exception) {
                Toast.makeText(context, "Unable to save : $e", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnChooseItemImg.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

}