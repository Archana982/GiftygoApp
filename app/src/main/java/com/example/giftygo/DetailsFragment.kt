package com.example.giftygo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.giftygo.Extensions.toast
import com.example.giftygo.databinding.FragmentDetailspageBinding
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import java.net.URLEncoder

@Suppress("DEPRECATION")
class DetailsFragment : Fragment(R.layout.fragment_detailspage) {

  private lateinit var binding: FragmentDetailspageBinding
  private lateinit var auth: FirebaseAuth
  private val args: DetailsFragmentArgs by navArgs()

  private var orderQuantity: Int = 1
  private var selectedColor: String? = null

  @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentDetailspageBinding.bind(view)

    auth = FirebaseAuth.getInstance()

    val glbUrl = arguments?.getString("glbUrl")
    val name = arguments?.getString("name") ?: ""
    val description = arguments?.getString("description") ?: ""
    val price = arguments?.getInt("price") ?: 0
    val color = arguments?.getString("color")

    // ✅ Set product details
    binding.tvDetailsProductName.text = name
    binding.tvDetailsProductDescription.text = description
    binding.tvDetailsProductPrice.text = "₹ $price"

    // ✅ Load GLB into WebView
    glbUrl?.let {
      val encodedUrl = URLEncoder.encode(it, "UTF-8")
      val viewerUrl = "file:///android_asset/gltf_viewer.html?model=$encodedUrl"
      with(binding.ivDetails) {
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.domStorageEnabled = true
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()
        loadUrl(viewerUrl)
      }
    }

    // ✅ Handle color chip selection with WebView color change
    binding.colorChipGroup.setOnCheckedChangeListener { group, checkedId ->
      selectedColor = group.findViewById<Chip>(checkedId)?.text.toString()
      binding.tvSelectedColor.text = "Selected Color: $selectedColor"

      // 🔁 Update 3D model color in WebView
      val jsColor = when (selectedColor?.lowercase()) {
        "red" -> "[1.0, 0.0, 0.0, 1.0]"
        "white" -> "[1.0, 1.0, 1.0, 1.0]"
        "black" -> "[0.0, 0.0, 0.0, 1.0]"
        "orange" -> "[0.984, 0.490, 0.027, 1.0]"
        "yellow" -> "[1.0, 1.0, 0.0, 1.0]"
        "grey" -> "[0.5, 0.5, 0.5, 1.0]"
        "brown"-> "[0.6627, 0.4549, 0.3254, 1.0]"
        "green" -> "[0.0, 1.0, 0.0, 1.0]"
        else -> null
      }

      jsColor?.let {
        binding.ivDetails.evaluateJavascript("setColor($it)", null)
      }
    }


    // ✅ Quantity spinner
    val quantities = resources.getStringArray(R.array.quantity_array)
    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, quantities)
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.quantitySpinner.adapter = spinnerAdapter
    binding.quantitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        orderQuantity = quantities[position].toIntOrNull() ?: 1
      }

      override fun onNothingSelected(parent: AdapterView<*>) {}
    }

      binding.btnDetailsAddToCart.setOnClickListener {
        // Just navigate to CartFragment
        findNavController().navigate(R.id.action_detailsFragment_to_cartFragment)
      }

    // ✅ Place Order
    binding.btnOrder.setOnClickListener {
      val user = auth.currentUser ?: return@setOnClickListener requireActivity().toast("Login required")
      val selectedChipId = binding.colorChipGroup.checkedChipId
      if (selectedChipId == View.NO_ID) {
        requireActivity().toast("Please select a color")
        return@setOnClickListener
      }

      val action = DetailsFragmentDirections.actionDetailsFragmentToOrderSummaryFragment(
        name = name,
        color = selectedColor ?: color.orEmpty(),
        quantity = orderQuantity,
        price = price,
        glbUrl = glbUrl.orEmpty()
      )
      findNavController().navigate(action)
    }
  }
}
