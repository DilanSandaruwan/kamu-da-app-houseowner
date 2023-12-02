package com.dilan.kamuda.houseownerapp.feature.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.common.util.KamuDaPopup
import com.dilan.kamuda.houseownerapp.common.util.component.ResponseHandlingDialogFragment
import com.dilan.kamuda.houseownerapp.databinding.FragmentOrderBinding
import com.dilan.kamuda.houseownerapp.feature.main.MainActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {

    lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var adapter: OrderAdapter
    private lateinit var mainActivity: MainActivity

    override fun onResume() {
        super.onResume()
        context?.let { MainActivity.kamuDaSecurePreference.getCustomerID(it).toInt() }
            ?.let {
                viewModel.getOrderDetails()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("CHECK", "onCreate: visited")
        mainActivity = requireActivity() as MainActivity
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
                viewModel.completedList.value = listOfOrders.filter { it.status == "completed" }
                when (viewModel.currentlySelectedGroup) {
                    "pending" -> adapter.submitList(viewModel.pendingList.value)
                    "accepted" -> {
                        adapter.submitList(viewModel.acceptedList.value)
                        binding.toggleButton.check(binding.tvShowAccepted.id)
                    }

                    "completed" -> adapter.submitList(viewModel.completedList.value)
                    else -> {
                        adapter.submitList(viewModel.acceptedList.value)
                        binding.toggleButton.check(binding.tvShowAccepted.id)
                    }
                }

            } else {
                binding.rvViewOrderDetails.visibility = View.GONE
                binding.tvNoOrdersYet.visibility = View.VISIBLE
            }
        }

        viewModel.objectHasUpdated.observe(viewLifecycleOwner) {
            if (it != null) viewModel.getOrderDetails()
        }

        binding.lytCommonErrorScreenIncluded.findViewById<MaterialButton>(R.id.mbtnCommonErrorScreen)
            .setOnClickListener {
                mainActivity.binding.navView.visibility = View.VISIBLE
                viewModel.getOrderDetails()
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

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.tvShowPending.id -> {
                        viewModel.currentlySelectedGroup = "pending"
                        setColorAsSelected(binding.tvShowPending)
                        setColorAsDeSelected(binding.tvShowAccepted)
                        setColorAsDeSelected(binding.tvShowCompleted)
                        adapter.submitList(viewModel.pendingList.value)
                    }

                    binding.tvShowAccepted.id -> {
                        viewModel.currentlySelectedGroup = "accepted"
                        setColorAsSelected(binding.tvShowAccepted)
                        setColorAsDeSelected(binding.tvShowPending)
                        setColorAsDeSelected(binding.tvShowCompleted)
                        adapter.submitList(viewModel.acceptedList.value)
                    }

                    binding.tvShowCompleted.id -> {
                        viewModel.currentlySelectedGroup = "completed"
                        setColorAsSelected(binding.tvShowCompleted)
                        setColorAsDeSelected(binding.tvShowPending)
                        setColorAsDeSelected(binding.tvShowAccepted)
                        adapter.submitList(viewModel.completedList.value)
                    }
                }
            }
        }

    }

    private fun setColorAsSelected(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.white, resources.newTheme()))
        button.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
    }

    private fun setColorAsDeSelected(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.black, resources.newTheme()))
        button.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
    }

    private fun showErrorPopup(kamuDaPopup: KamuDaPopup) {
        val dialogFragment = ResponseHandlingDialogFragment.newInstance(
            title = kamuDaPopup.title,
            message = kamuDaPopup.message,
            positiveButtonText = kamuDaPopup.positiveButtonText,
            negativeButtonText = kamuDaPopup.negativeButtonText,
            type = kamuDaPopup.type,
        ).apply { setNegativeActionListener { viewModel.resetShowErrorPopup() } }
        dialogFragment.show(childFragmentManager, "custom_dialog")
    }

    private fun showCommonErrorScreen() {
        //mainActivity.binding.navView.visibility = View.GONE
        binding.lytCommonErrorScreenIncluded.visibility = View.VISIBLE
    }

}