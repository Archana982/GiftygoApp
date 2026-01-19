package com.example.giftygo.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.giftygo.Models.OrderModel
import com.example.giftygo.R

class CartAdapter(
    private var orderList: List<OrderModel>,
    private val onOrderReceived: (String) -> Unit,
    private val onOrderCancelled: (String) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartproduct_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    fun updateOrderList(newList: List<OrderModel>) {
        orderList = newList
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tvOrderProductName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvOrderPrice)
        private val tvColor: TextView = itemView.findViewById(R.id.tvOrderColor)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvOrderQuantity)
        private val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        private val btnReceived: Button = itemView.findViewById(R.id.btnReceived)
        private val btnCancel: Button = itemView.findViewById(R.id.btnCancel)

        fun bind(item: OrderModel) {
            tvProductName.text = item.productName
            tvPrice.text = "₹${item.totalPrice}"
            tvColor.text = "Color: ${item.color}"
            tvQuantity.text = "Quantity: ${item.quantity}"
            tvOrderStatus.text = "Status: ${item.orderStatus}"

            // Show/hide buttons based on order status
            when (item.orderStatus) {
                "Cancelled", "Received" -> {
                    btnCancel.visibility = View.GONE
                    btnReceived.visibility = View.GONE
                }
                else -> {
                    btnCancel.visibility = View.VISIBLE
                    btnReceived.visibility = View.VISIBLE
                }
            }

            // Set status text color
            tvOrderStatus.setTextColor(
                when (item.orderStatus) {
                    "Received" -> android.graphics.Color.GREEN
                    "Cancelled" -> android.graphics.Color.RED
                    else -> android.graphics.Color.BLUE
                }
            )

            btnReceived.setOnClickListener { onOrderReceived(item.orderId ?: "") }
            btnCancel.setOnClickListener { onOrderCancelled(item.orderId ?: "") }
        }
    }
}
