package com.dilan.kamuda.houseownerapp.feature.edit

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.common.util.KamuDaPopup
import com.dilan.kamuda.houseownerapp.common.util.component.ResponseHandlingDialogFragment
import com.dilan.kamuda.houseownerapp.databinding.FragmentEditMealMenuBinding
import com.dilan.kamuda.houseownerapp.feature.main.MainActivity
import com.dilan.kamuda.houseownerapp.feature.main.MainActivity.Companion.kamuDaSecurePreference
import com.dilan.kamuda.houseownerapp.feature.menu.CustomDialogFragmentMenu
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class EditMealMenuFragment : Fragment() {

    lateinit var binding: FragmentEditMealMenuBinding
    private val viewModel: EditMealMenuViewModel by viewModels()
    private val args: EditMealMenuFragmentArgs by navArgs()
    private var imageBytes: ByteArray? = null
    private var selectedFoodMenu: FoodMenu? = null
    private lateinit var mainActivity: MainActivity

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
        selectedFoodMenu = args.foodMenuItem
        mainActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_meal_menu, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.menuEditVM = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mtvNewItemName.setText(selectedFoodMenu?.name ?: "")
        binding.mtvNewItemUnitPrice.setText(selectedFoodMenu?.price.toString())
        binding.switchStatus.isChecked = selectedFoodMenu?.status.equals("Y", ignoreCase = true)
        if (selectedFoodMenu?.image != null) {
            var imageBitmap =
                BitmapFactory.decodeByteArray(
                    selectedFoodMenu!!.image as ByteArray?,
                    0,
                    (selectedFoodMenu!!.image as ByteArray).size
                )
            context?.let {
                Glide.with(it)
                    .load(imageBitmap)
                    .diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    )
                    .into(binding.ivChooseItemImg)
            }
        }

        binding.btnSaveEditMenu.setOnClickListener {
            val dialogFragment = CustomDialogFragmentMenu.newInstance(
                title = "Menu Update Confirmation",
                message = "Please press Confirm if you sure to confirm the changes.",
                positiveButtonText = "Confirm",
                negativeButtonText = "Cancel",
            )
            dialogFragment.setPositiveActionListener { setSavingMenuItem() }
            dialogFragment.show(childFragmentManager, "custom_dialog_edit")
        }

        binding.btnChooseItemImg.setOnClickListener {
            openGallery()
        }

        binding.lytCommonErrorScreenIncluded.findViewById<MaterialButton>(R.id.mbtnCommonErrorScreen)
            .setOnClickListener {
                mainActivity.binding.navView.visibility = View.VISIBLE
                setSavingMenuItem()
                binding.lytCommonErrorScreenIncluded.visibility = View.GONE
            }

        viewModel.showLoader.observe(viewLifecycleOwner) {
            if (it) {
                mainActivity.binding.navView.visibility = View.GONE
            } else {
                mainActivity.binding.navView.visibility = View.VISIBLE
            }
            mainActivity.showProgress(it)
        }

        viewModel.showErrorPopup.observe(viewLifecycleOwner) {
            if (it != null) {
                showErrorPopup(it)
            }
        }

        viewModel.showErrorPage.observe(viewLifecycleOwner) {
            if (it) {
                showCommonErrorScreen()
            }
        }

    }

    private fun setSavingMenuItem() {
        // Retrieve UI Input
        val itemName = binding.mtvNewItemName.text.toString()
        val itemPrice = binding.mtvNewItemUnitPrice.text.toString().toDoubleOrNull() ?: 0.0
        val itemStatus = if (binding.switchStatus.isChecked) "Y" else "N"

        // Create a FoodMenu Object
        val body = selectedFoodMenu?.let { existingMenu ->
            if (imageBytes != null) {
                FoodMenu(existingMenu.id, itemName, itemPrice, itemStatus, imageBytes)
            } else {
                FoodMenu(existingMenu.id, itemName, itemPrice, itemStatus, existingMenu.image)
            }
        }

        // Invoke ViewModel to Save the Item
        body?.let { viewModel.saveNewItem(it) }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun showErrorPopup(kamuDaPopup: KamuDaPopup) {
        val dialogFragment = ResponseHandlingDialogFragment.newInstance(
            title = kamuDaPopup.title,
            message = kamuDaPopup.message,
            positiveButtonText = kamuDaPopup.positiveButtonText,
            negativeButtonText = kamuDaPopup.negativeButtonText,
            type = kamuDaPopup.type,
        ).apply {
            setNegativeActionListener {
                viewModel.resetErrorPopup()
                if (kamuDaPopup.type == 1) {
                    goToMenuListFragmentFromEditMenu()
                }
            }
        }

        dialogFragment.show(childFragmentManager, "custom_dialog")
    }

    private fun showCommonErrorScreen() {
        //mainActivity.binding.navView.visibility = View.GONE
        binding.lytCommonErrorScreenIncluded.visibility = View.VISIBLE
    }

    private fun goToMenuListFragmentFromEditMenu() {
        kamuDaSecurePreference.setLoadMenu(requireContext(), true)

        val navController = view?.findNavController()

        // Pop the back stack up to the specified destination (R.id.menuFragment)
        navController?.popBackStack(R.id.menuFragment, false)

        // Check if the current destination is not already the MenuFragment
        if (navController?.currentDestination?.id != R.id.menuFragment) {
            // Navigate to MenuFragment
            val action = EditMealMenuFragmentDirections.actionEditMealMenuFragmentToMenuFragment()
            navController?.navigate(action)
        }

    }

}