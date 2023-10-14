package com.dilan.kamuda.houseownerapp.feature.order

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
import com.dilan.kamuda.houseownerapp.databinding.FragmentOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {

    lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("CHECK", "onCreate: visited")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.e("CHECK", "onCreateView: visited")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewOrderVM = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("CHECK", "onViewCreated: visited")
//        viewModel.getOrdersByStatus("accepted")
        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
        adapter = OrderAdapter(object :
            OrderAdapter.OnItemClickListener {

            override fun itemClick(itemId: Int, status: String) {
                viewModel.updateOrderWithStatus(itemId, status)
            }
        })

        binding.rvViewOrderDetails.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.ordersList.observe(viewLifecycleOwner) { listOfOrders ->
            if (listOfOrders.isNotEmpty()) {
                binding.tvOrdersLoading.visibility = View.GONE
                binding.tvNoOrdersYet.visibility = View.GONE
                binding.rvViewOrderDetails.visibility = View.VISIBLE
                viewModel.pendingList.value = listOfOrders.filter { it.status == "pending" }
                viewModel.acceptedList.value = listOfOrders.filter { it.status == "accepted" }
                viewModel.rejectedList.value = listOfOrders.filter { it.status == "rejected" }
                when (viewModel.currentlySelectedGroup) {
                    "pending" -> adapter.submitList(viewModel.pendingList.value)
                    "accepted" -> adapter.submitList(viewModel.acceptedList.value)
                    "rejected" -> adapter.submitList(viewModel.rejectedList.value)
                    "all" -> adapter.submitList(listOfOrders)
                }

            } else {
                binding.rvViewOrderDetails.visibility = View.GONE
                binding.tvNoOrdersYet.visibility = View.VISIBLE
            }
        }

        viewModel.objectHasUpdated.observe(viewLifecycleOwner) {
            if (it != null)
                viewModel.getOrderDetails()
            else
                showErrorPopup()
        }

        binding.tvShowRejected.setOnClickListener {
            viewModel.currentlySelectedGroup = "rejected"
            adapter.submitList(viewModel.rejectedList.value)
        }

        binding.tvShowAccepted.setOnClickListener {
            viewModel.currentlySelectedGroup = "accepted"
            adapter.submitList(viewModel.acceptedList.value)
        }

        binding.tvShowPending.setOnClickListener {
            viewModel.currentlySelectedGroup = "pending"
            adapter.submitList(viewModel.pendingList.value)
        }

        binding.tvShowAll.setOnClickListener {
            viewModel.currentlySelectedGroup = "all"
            adapter.submitList(viewModel.ordersList.value)
        }

    }

    fun showErrorPopup() {
        Toast.makeText(context, "Response is null!", Toast.LENGTH_LONG).show()
    }

}