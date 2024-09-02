package com.dumchykov.socialnetworkdemo.ui.signupextended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.databinding.FragmentSignUpExtendedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpExtendedFragment : Fragment() {
    private var _binding: FragmentSignUpExtendedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpExtendedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setForwardClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setForwardClickListener() {
        binding.buttonForward.setOnClickListener {
            findNavController().navigate(R.id.action_signUpExtendedFragment_to_pagerFragment)
        }
    }
}