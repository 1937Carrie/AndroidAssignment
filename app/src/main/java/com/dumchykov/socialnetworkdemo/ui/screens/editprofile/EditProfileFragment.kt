package com.dumchykov.socialnetworkdemo.ui.screens.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.databinding.FragmentEditProfileBinding
import com.dumchykov.socialnetworkdemo.domain.logic.toApiContact
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorizedUser.collect { user ->
                if (user.id == -1) return@collect

                fillInDataInInputFields()
                setArrowBackClickListener()
                setSaveClickListener()
                observeEditUserAttempt()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeEditUserAttempt() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editProfileState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    binding.layoutProgress.root.visibility = View.GONE
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            val userId = viewModel.authorizedUser.value.id
            val bearerToken = sharedViewModel.shareState.value.accessToken
            with(binding) {
                val apiContact = viewModel.authorizedUser.value.toApiContact().copy(
                    name = textInputUserNameEditText.text.toString(),
                    career = textInputCareerEditText.text.toString(),
                    email = textInputEmailEditText.text.toString(),
                    phone = textInputPhoneEditText.text.toString(),
                    address = textInputAddressEditText.text.toString(),
                    birthday = textInputBirthDateEditText.text.toString()
                )
                viewModel.editProfile(userId, bearerToken, apiContact)
            }
        }
    }

    private fun setArrowBackClickListener() {
        binding.buttonArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun fillInDataInInputFields() {
        val currentUser = viewModel.authorizedUser.value
        with(binding) {
            textInputUserNameEditText.setText(currentUser.name)
            textInputCareerEditText.setText(currentUser.career)
            textInputEmailEditText.setText(currentUser.email)
            textInputPhoneEditText.setText(currentUser.phone)
            textInputAddressEditText.setText(currentUser.address)
            textInputBirthDateEditText.setText(currentUser.birthday)
        }
    }
}