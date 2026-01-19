package com.example.giftygo.rvadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.giftygo.Models.OrderModel
import com.example.giftygo.databinding.ItemProductBinding

class OrderAdapter(
  private var orders: MutableList<OrderModel>,
  private val onAcceptClick: (OrderModel) -> Unit,
  private val onPreparedClick: (OrderModel) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

  inner class OrderViewHolder(val binding: ItemProductBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
    val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return OrderViewHolder(binding)
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
    val order = orders[position]
    holder.binding.apply {
      tvProductName.text = order.productName
      tvProductColor.text = "Color: ${order.color}"
      tvProductQuantity.text = "Quantity: ${order.quantity}"
      tvTotalPrice.text = "Total Price: ₹${order.totalPrice}"
      tvCustomerName.text = "Customer: ${order.customerName}"
      tvCustomerPhone.text = "Phone: ${order.customerPhone}"
      tvOrderStatus.text = "Status: ${order.status}"

      btnAccept.setOnClickListener { onAcceptClick(order) }
      btnPrepared.setOnClickListener { onPreparedClick(order) }
    }
  }

  override fun getItemCount(): Int = orders.size

  @SuppressLint("NotifyDataSetChanged")
  fun updateOrderList(newOrders: List<OrderModel>) {
    orders.clear()
    orders.addAll(newOrders)
    notifyDataSetChanged()
  }
}
