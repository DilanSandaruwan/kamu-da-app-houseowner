package com.dilan.kamuda.houseownerapp.feature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dilan.kamuda.houseownerapp.databinding.FragmentAddNewBinding
import com.dilan.kamuda.houseownerapp.feature.add.model.AddNewMenu
import com.dilan.kamuda.houseownerapp.feature.menu.HouseMenuAdapter
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddNewFragment : Fragment() {

    lateinit var binding: FragmentAddNewBinding
    private lateinit var viewModel: AddNewViewModel
    private lateinit var adapter: HouseMenuAdapter

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
            val body = FoodMenu(
                -1,
                binding.mtvNewItemName.text.toString(),
                binding.mtvNewItemUnitPrice.text.toString().toDoubleOrNull() ?: 0.00,
                if (binding.switchStatus.isChecked) {
                    "Y"
                } else {
                    "N"
                }
            )
            viewModel.saveNewItem(body)
        }
    }

}