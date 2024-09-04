package com.dumchykov.socialnetworkdemo.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.FragmentDetailsBinding
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
        val userId = arguments?.getInt("contact.id") ?: throw IllegalStateException()
        val contactName = arguments?.getString("contact.name")
        val users = sharedViewModel.shareState.value.userList
        val userContactIdList = sharedViewModel.shareState.value.userContactIdList
        setTransition(userId, contactName)
        val user = viewModel.getUserById(users, userContactIdList, userId)
        bindUi(user)

        binding.buttonArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindUi(user: Contact) {
        binding.textName.text = user.name
        binding.textProfession.text = user.career
        binding.textAddress.text = user.address
        binding.imageMain.setImageWithGlide("https://www.reuters.com/resizer/v2/MKQZUV67IFKAHDUNK4LJATIVMQ.jpg?auth=85a0616067eb4e93c8895d334072973babbfedb1376eb30339e6988218abc7ab")

        when (user.isAdded) {
            true -> {
                binding.buttonMessage.visibility = View.VISIBLE
                binding.buttonMessageWhileNotFriended.visibility = View.GONE
                binding.buttonAddToMyContacts.visibility = View.GONE
            }

            false -> {
                binding.buttonMessage.visibility = View.GONE
                binding.buttonMessageWhileNotFriended.visibility = View.VISIBLE
                binding.buttonAddToMyContacts.visibility = View.VISIBLE
            }
        }
    }

    private fun setTransition(contactId: Int, contactName: String?) {
        binding.imageMain.transitionName = "${contactId}_${contactName}"
        startPostponedEnterTransition()
    }
}