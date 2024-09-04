package com.dumchykov.socialnetworkdemo.ui.screens.signupextended

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentSignUpExtendedBinding
import com.dumchykov.socialnetworkdemo.domain.logic.toApiContact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.EditUserResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtendedFragment : Fragment() {
    private var _binding: FragmentSignUpExtendedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpExtendedViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpExtendedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorizedUser.collect { user ->
                if (user.id == -1) return@collect

                setOnAddImageClickListener()
                setCancelClickListener()
                setForwardClickListener()
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
            viewModel.signUpExtendedState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    binding.layoutProgress.root.visibility = View.GONE
                    val (_) = (state as ResponseState.Success<*>).data as EditUserResponse
                    ;
                    findNavController().navigate(R.id.action_signUpExtendedFragment_to_pagerFragment)
                }
            }
        }
    }

    private fun setForwardClickListener() {
        binding.buttonForward.setOnClickListener {
            val userId = viewModel.authorizedUser.value.id
            val bearerToken = sharedViewModel.shareState.value.accessToken
            val editedUser = viewModel.authorizedUser.value.toApiContact().copy(
                email = binding.textInputEmailEditText.text.toString(),
                phone = binding.textInputMobilePhoneEditText.text.toString()
            )
            viewModel.editUser(userId, bearerToken, editedUser)
        }
    }

    private fun setCancelClickListener() {
        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setOnAddImageClickListener() {
        binding.imageAddImage.setOnClickListener {
            val url = "https://youtu.be/dQw4w9WgXcQ"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }
}