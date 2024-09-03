package com.dumchykov.socialnetworkdemo.ui.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentEditProfileBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.EditUserResponse
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
        fillInDataInInputFields()
        setArrowBackClickListener()
        setSaveClickListener()
        observeEditUserAttempt()
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
                    val (user) = (state as ResponseState.Success<*>).data as EditUserResponse
                    sharedViewModel.updateState {
                        copy(
                            currentUser = user
                        )
                    }
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            val userId = sharedViewModel.shareState.value.currentUser.id
            val bearerToken = sharedViewModel.shareState.value.accessToken
            val contact = sharedViewModel.shareState.value.currentUser.copy(
                name = binding.textInputUserNameEditText.text.toString(),
                career = binding.textInputCareerEditText.text.toString(),
                email = binding.textInputEmailEditText.text.toString(),
                phone = binding.textInputPhoneEditText.text.toString(),
                address = binding.textInputAddressEditText.text.toString(),
                birthday = binding.textInputBirthDateEditText.text.toString()
            )
            viewModel.editProfile(userId, bearerToken, contact)
        }
    }

    private fun setArrowBackClickListener() {
        binding.buttonArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun fillInDataInInputFields() {
        val currentUser = sharedViewModel.shareState.value.currentUser
        binding.textInputUserNameEditText.setText(currentUser.name)
        binding.textInputCareerEditText.setText(currentUser.career)
        binding.textInputEmailEditText.setText(currentUser.email)
        binding.textInputPhoneEditText.setText(currentUser.phone)
        binding.textInputAddressEditText.setText(currentUser.address)
        binding.textInputBirthDateEditText.setText(currentUser.birthday)
    }
}