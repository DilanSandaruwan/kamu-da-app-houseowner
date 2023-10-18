package com.dilan.kamuda.houseownerapp.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.databinding.FragmentHomeBinding
import com.dilan.kamuda.houseownerapp.feature.main.MainActivity
import com.dilan.kamuda.houseownerapp.feature.main.MainActivity.Companion.kamuDaSecurePreference
import com.dilan.kamuda.houseownerapp.feature.order.ViewOrderedItemsAdapter
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import com.google.android.material.button.MaterialButton
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter
    private var latestOrderDetail: OrderDetail? = null
    private lateinit var mainActivity: MainActivity

    override fun onResume() {
        super.onResume()
        context?.let { kamuDaSecurePreference.getCustomerID(it).toInt() }
            ?.let {
                viewModel.getOrdersListForAllFromDataSource()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeVM = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.latestOrder.observe(viewLifecycleOwner) {
            latestOrderDetail = it
            manageFirstPriorityOrder()
        }

        viewModel.allOrders.observe(viewLifecycleOwner) { allOrdersList ->
            if (allOrdersList != null && allOrdersList.isNotEmpty()) {
                binding.mtvAllPendingCount.text = allOrdersList.count {
                    it.status == getString(R.string.order_status_pending)
                }.toString()
                binding.mtvAllAcceptedCount.text = allOrdersList.count {
                    it.status == getString(R.string.order_status_accepted)
                }.toString()
                binding.mtvAllCompletedCount.text = allOrdersList.count {
                    it.status == getString(R.string.order_status_completed)
                }.toString()
                binding.mtvAllRejectedCount.text = allOrdersList.count {
                    it.status == getString(R.string.order_status_rejected)
                }.toString()
            }
            latestOrderDetail =
                allOrdersList.filter { it.status == getString(R.string.order_status_accepted) }
                    .minByOrNull { it.id }
            manageFirstPriorityOrder()
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
            val dialogFragment = mainActivity.showErrorPopup(it).apply {
                setPositiveActionListener {
                    viewModel.getOrdersListForAllFromDataSource()
                }
                setNegativeActionListener {

                }
            }
            dialogFragment.show(childFragmentManager, "custom_dialog")
        }

        viewModel.showErrorPage.observe(viewLifecycleOwner) {
            if (it) {
                showCommonErrorScreen()
            }
        }

        binding.lytCommonErrorScreenIncluded.findViewById<MaterialButton>(R.id.mbtnCommonErrorScreen)
            .setOnClickListener {
                mainActivity.binding.navView.visibility = View.VISIBLE
                viewModel.getOrdersListForAllFromDataSource()
                binding.lytCommonErrorScreenIncluded.visibility = View.GONE
            }
    }

    private fun manageFirstPriorityOrder() {
        if (latestOrderDetail == null) {
            binding.lytLatestOrderLoading.visibility = View.GONE
            binding.lytLatestOrderNot.visibility = View.VISIBLE
            binding.lytLatestOrder.visibility = View.GONE
        } else {
            binding.lytLatestOrder.findViewById<LinearLayout>(R.id.lytBtnActions).visibility =
                View.GONE
            binding.lytLatestOrderLoading.visibility = View.GONE
            binding.lytLatestOrderNot.visibility = View.GONE
            binding.lytLatestOrder.visibility = View.VISIBLE

            SimpleDateFormat("yyyy MMM dd").format(
                SimpleDateFormat("yyyy-MM-dd").parse(
                    latestOrderDetail!!.date
                )
            )
                .also {
                    binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderDate).text =
                        it
                }
            binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderTime).text =
                latestOrderDetail!!.createdAt
            binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderTotal).text =
                "LKR ${latestOrderDetail!!.total}"
            binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderedItemCount).text =
                "${latestOrderDetail!!.items.size} Items"
            binding.lytLatestOrder.findViewById<TextView>(R.id.tvOrderStatus).text =
                "${latestOrderDetail!!.status.uppercase()}"

            when (latestOrderDetail!!.status) {
                "pending" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.yellow
                        )

                }

                else -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.green
                        )
                }
            }

            when (latestOrderDetail!!.status) {
                "completed" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.black
                        )
                }

                "rejected" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.redish
                        )
                }

                "cancelled" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.grey
                        )
                }
            }

            // Set up child RecyclerView
            val childAdapter = ViewOrderedItemsAdapter()
            binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).layoutManager =
                LinearLayoutManager(binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).context) // Set layout manager
            binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems)
                .addItemDecoration(
                    DividerItemDecoration(
                        binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).context,
                        (binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).layoutManager as LinearLayoutManager).orientation
                    )
                )
            binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).adapter =
                childAdapter
            childAdapter.submitList(latestOrderDetail!!.items)

            binding.lytLatestOrder.findViewById<RelativeLayout>(R.id.lytBtnToggle)
                .setOnClickListener {
                    if (binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown).visibility == View.VISIBLE) {
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown).visibility =
                            View.GONE
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowUp).visibility =
                            View.VISIBLE
                        binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).visibility =
                            View.VISIBLE
                    } else {
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowUp).visibility =
                            View.GONE
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown).visibility =
                            View.VISIBLE
                        binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).visibility =
                            View.GONE
                    }
                }

        }
    }

    private fun showCommonErrorScreen() {
        mainActivity.binding.navView.visibility = View.GONE
        binding.lytCommonErrorScreenIncluded.visibility = View.VISIBLE
    }

}