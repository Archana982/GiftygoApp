package com.example.giftygo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.giftygo.Extensions.toast
import com.example.giftygo.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignupBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()
            val name = binding.etNameSignUp.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                createUser(email, password)
            } else {
                requireActivity().toast("Please fill all fields")
            }
        }

        binding.loginAsAdmin.setOnClickListener {
            val intent = Intent(requireContext(), DialogMobileLoginActivity::class.java)
            startActivity(intent)
        }


        binding.tvNavigateToSignIn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_signUpFragment_to_signInFragmentFragment)
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    requireActivity().toast("User created successfully")
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_signUpFragment_to_mainFragment)
                } else {
                    requireActivity().toast(task.exception?.localizedMessage ?: "Signup failed")
                }
            }
    }


}