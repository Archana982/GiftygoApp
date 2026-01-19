package com.example.giftygo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.giftygo.databinding.FragmentOrderSuccessFragmentBinding

class OrderSuccessFragment : Fragment(R.layout.fragment_order_success_fragment) {

  private lateinit var binding: FragmentOrderSuccessFragmentBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Automatically navigate to CartFragment after 2 seconds
    Handler(Looper.getMainLooper()).postDelayed({
      findNavController().navigate(R.id.action_orderSuccessFragment_to_cartFragment)
    }, 2000)
  }
}

