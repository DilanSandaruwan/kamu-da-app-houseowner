package com.dilan.kamuda.houseownerapp.feature.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.databinding.FragmentMenuBinding
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        adapter = HouseMenuAdapter(object :
            HouseMenuAdapter.OnItemClickListener {

            override fun itemClick(item: FoodMenu) {

            }
        }, this)

        binding.rvFoodMenu.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

//        binding.btnUpdateMenu.setOnClickListener {
//            val checkedItems = adapter.getCheckedItemsList()
//
//            val dialogFragment = CustomDialogFragment.newInstance(
//                title = "Order Confirmation",
//                message = "Please press Confirm if you sure to confirm the order.",
//                positiveButtonText = "Confirm",
//                negativeButtonText = "Cancel",
//                checkedItems = checkedItems
//            )
//            dialogFragment.setPositiveActionListener { setOrderDetails(checkedItems) }
//            dialogFragment.show(childFragmentManager, "custom_dialog")
//
//        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.checkedItems.observe(viewLifecycleOwner) { it ->

            binding.btnUpdateMenu.isEnabled = !it.any { it.price == 0.00 }
            adapter.setCheckedItems(it)
        }

        viewModel.emptyOrder.observe(viewLifecycleOwner) {
            binding.btnUpdateMenu.isEnabled = !it
        }

        viewModel.resetList.observe(viewLifecycleOwner) {

        }
    }
    override fun onItemChecked(item: FoodMenu, isChecked: Boolean) {
        val updatedCheckedItems = viewModel.checkedItems.value?.toMutableList() ?: mutableListOf()
        if (isChecked) {
            if (!updatedCheckedItems.contains(item)) {
                updatedCheckedItems.add(item)
            }
        } else {
            updatedCheckedItems.remove(item)
        }
        viewModel.setCheckedItemsList(updatedCheckedItems)
    }

    private fun setMenuDetails(checkedItems: List<FoodMenu>) {
        var mutableList = mutableListOf<FoodMenu>()
        for (i in checkedItems) {
            mutableList.add(FoodMenu(i.id, i.name, i.price, i.status))
        }
        //viewModel.updateMenuTable(myOrder)
    }

}