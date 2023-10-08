package com.dilan.kamuda.houseownerapp.feature.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.databinding.FragmentMenuBinding
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment(), HouseMenuAdapter.CheckedItemListener {

    lateinit var binding: FragmentMenuBinding
    private val viewModel: MenuViewModel by viewModels()
    private lateinit var adapter: HouseMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.menuVM = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
        adapter = HouseMenuAdapter(this, object :
            HouseMenuAdapter.OnItemClickListener {

            override fun itemClick(item: FoodMenu) {

            }
        }, this)

        binding.rvFoodMenu.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        binding.btnUpdateMenu.setOnClickListener {
            val changedItems = viewModel.menuList.value

            if (changedItems != null) {
                val dialogFragment = CustomDialogFragmentMenu.newInstance(
                    title = "Menu Update Confirmation",
                    message = "Please press Confirm if you sure to confirm the changes.",
                    positiveButtonText = "Confirm",
                    negativeButtonText = "Cancel",
                )
                dialogFragment.setPositiveActionListener { setUpdatedMenuDetails(changedItems) }
                dialogFragment.show(childFragmentManager, "custom_dialog")
            } else {
                Toast.makeText(context, "Nothing in the changed list", Toast.LENGTH_LONG).show()

            }


        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.checkedItems.observe(viewLifecycleOwner) { it ->
            Log.e("RECURSIVE", "onViewCreated: chcesklflksfslkfllkfsfs")
            binding.btnUpdateMenu.isEnabled = !it.any { it.price == 0.00 }
            adapter.setCheckedItems(it)
        }

        viewModel.emptyOrder.observe(viewLifecycleOwner) {
            binding.btnUpdateMenu.isEnabled = !it
        }

        viewModel.resetList.observe(viewLifecycleOwner) {

        }

        viewModel.listChanged.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getMenuListForMeal("breakfast")
            }
        }
    }

    override fun onItemChecked(item: FoodMenu, isChecked: Boolean) {
        val updatedCheckedItems = viewModel.checkedItems.value?.toMutableList() ?: mutableListOf()
        if (isChecked) {
            item.status = "Y"
            if (!updatedCheckedItems.contains(item)) {
                updatedCheckedItems.add(item)
            }
        } else {
            item.status = "N"
            updatedCheckedItems.remove(item)
        }
        viewModel.setCheckedItemsList(updatedCheckedItems)
    }

    private fun setUpdatedMenuDetails(changedItems: List<FoodMenu>) {
        var mutableList = mutableListOf<FoodMenu>()
        for (i in changedItems) {
            mutableList.add(FoodMenu(i.id, i.name, i.price, i.status, i.image))
        }
        viewModel.updateMenuTable(mutableList)
    }

}