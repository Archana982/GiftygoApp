package com.example.giftygo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.giftygo.Extensions.toast
import com.example.giftygo.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragmentFragment : Fragment(R.layout.fragment_signin) {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSigninBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        // Auto login if user already signed in
        auth.currentUser?.let {
            findNavController().navigate(R.id.action_signInFragmentFragment_to_mainFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmailSignIn.text.toString()
            val password = binding.etPasswordSignIn.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                requireActivity().toast("Please fill all fields")
            }
        }

        binding.tvNavigateToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragmentFragment_to_signUpFragment)
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    requireActivity().toast("Sign In Successful")
                    findNavController().navigate(R.id.action_signInFragmentFragment_to_mainFragment)
                } else {
                    requireActivity().toast(task.exception?.localizedMessage ?: "Sign In failed")
                }
            }
    }
}
