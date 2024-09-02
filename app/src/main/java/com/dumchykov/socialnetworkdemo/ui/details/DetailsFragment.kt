package com.dumchykov.socialnetworkdemo.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.dumchykov.socialnetworkdemo.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactId = arguments?.getString("contact.id")
        val contactName = arguments?.getString("contact.name")
        val contactCareer = arguments?.getString("contact.career")
        val contactAddress = arguments?.getString("contact.address")
        setTransition(contactId, contactName)
        binding.textName.text = contactName
        binding.textProfession.text = contactCareer
        binding.textAddress.text = contactAddress

        binding.buttonArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTransition(contactId: String?, contactName: String?) {
        binding.imageMain.transitionName = "${contactId}_${contactName}"
        startPostponedEnterTransition()
    }
}