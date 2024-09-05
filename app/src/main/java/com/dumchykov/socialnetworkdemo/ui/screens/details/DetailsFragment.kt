package com.dumchykov.socialnetworkdemo.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentDetailsBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleContactResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.notification.IS_FROM_DEEP_LINK
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.MyContactsFragment.Companion.INDICATOR_CONTACT_ID
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.MyContactsFragment.Companion.INDICATOR_CONTACT_NAME
import com.dumchykov.socialnetworkdemo.ui.util.PROFILE_IMAGE_URL
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorizedUser.collect { user ->
                if (user.id == -1) return@collect

                handleDeepLink()
                val userId = arguments?.getInt(INDICATOR_CONTACT_ID) ?: 0
                val contactName = arguments?.getString(INDICATOR_CONTACT_NAME).orEmpty()
                setTransition(userId, contactName)
                bindUi(userId)
                observeApiResponse()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeApiResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailsState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this
                ) {
                    val (contacts) = (state as ResponseState.Success<*>).data as MultipleContactResponse
                    sharedViewModel.updateState { copy(userContactIdList = contacts.map { it.id }) }
                    viewModel.updateContactState {
                        copy(
                            isAdded = viewModel.isUserAddedToContact(
                                sharedViewModel.shareState.value.userContactIdList
                            )
                        )
                    }
                }
            }
        }
    }

    private fun bindUi(contactId: Int) {
        val userContactIdList = sharedViewModel.shareState.value.userContactIdList
        viewModel.getUserById(userContactIdList, contactId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactState.collect { indicatorContact ->
                binding.textName.text = indicatorContact.name
                binding.textProfession.text = indicatorContact.career
                binding.textAddress.text = indicatorContact.address
                binding.imageMain.setImageWithGlide(PROFILE_IMAGE_URL)

                when (indicatorContact.isAdded) {
                    true -> {
                        binding.buttonMessage.visibility = View.VISIBLE
                        binding.buttonMessageWhileNotFriended.visibility = View.GONE
                        binding.buttonAddToMyContacts.visibility = View.GONE
                    }

                    false -> {
                        binding.buttonMessage.visibility = View.GONE
                        binding.buttonMessageWhileNotFriended.visibility = View.VISIBLE
                        binding.buttonAddToMyContacts.visibility = View.VISIBLE

                        binding.buttonAddToMyContacts.setOnClickListener {
                            val bearerToken = sharedViewModel.shareState.value.accessToken
                            val userId = viewModel.authorizedUser.value.id
                            viewModel.addContact(bearerToken, userId, ContactId(contactId))
                        }
                    }
                }
            }
        }
    }

    private fun setTransition(contactId: Int, contactName: String?) {
        binding.imageMain.transitionName = "${contactId}_${contactName}"
        startPostponedEnterTransition()
    }

    private suspend fun handleDeepLink() {
        val navBackStackEntry = findNavController().currentBackStackEntry
        val isFromDeepLink =
            navBackStackEntry?.arguments?.getBoolean(IS_FROM_DEEP_LINK, false) ?: false

        setOnArrowBackClickListener(isFromDeepLink)
        if (isFromDeepLink) {
            requireActivity().onBackPressedDispatcher.addCallback(
                owner = viewLifecycleOwner,
                onBackPressed = {
                    navigateToMyProfile()
                }
            )
            viewLifecycleOwner.lifecycleScope.launch {
                async { sharedViewModel.authorize() }.await()
                async { sharedViewModel.getUserContacts() }.await()
                async { sharedViewModel.getUsers() }.await()
            }.join()
        }
    }

    private fun navigateToMyProfile() {
        findNavController().navigate(R.id.action_detailsFragment_to_pagerFragment)
    }

    private fun setOnArrowBackClickListener(isDeepLinked: Boolean) {
        binding.buttonArrowBack.setOnClickListener {
            when (isDeepLinked) {
                true -> {
                    navigateToMyProfile()
                }

                false -> {
                    findNavController().navigateUp()
                }
            }
        }
    }
}