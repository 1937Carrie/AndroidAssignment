package com.dumchykov.socialnetworkdemo.ui.screens.addcontacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentAddContactsBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleContactResponse
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleUserResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.notification.NOTIFICATION_ID
import com.dumchykov.socialnetworkdemo.ui.notification.createOnAddNotification
import com.dumchykov.socialnetworkdemo.ui.screens.addcontacts.adapter.AddContactsAdapter
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.ContactsItemDecoration
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactsFragment : Fragment() {
    private var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var addContactsAdapter: AddContactsAdapter
    private val viewModel: AddContactsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showNotification(viewModel.currentContact.value)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Post notification permission was not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateAllUsers()
        setArrowBackClickListener()
        initAdapter()
        observeApiResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeApiResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addContactsState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    binding.layoutProgress.root.visibility = View.GONE
                    when ((state as ResponseState.Success<*>).data) {
                        is MultipleUserResponse -> {
                            val (users) = state.data as MultipleUserResponse
                            sharedViewModel.updateState { copy(userList = users) }
                        }

                        is MultipleContactResponse -> {
                            val (contacts) = state.data as MultipleContactResponse
                            sharedViewModel.updateState { copy(userContactIdList = contacts.map { it.id }) }
                        }
                    }

                }
            }
        }
    }

    private fun initAdapter() {
        addContactsAdapter = AddContactsAdapter(
            onClickListener = { userId, userName ->
                val bundle = bundleOf(
                    "contact.id" to userId,
                    "contact.name" to userName
                )
                findNavController().navigate(
                    R.id.action_addContactsFragment_to_detailsFragment,
                    bundle
                )
            },
            onAddListener = { contact ->
                val bearerToken = sharedViewModel.shareState.value.accessToken
                val currentUserId = sharedViewModel.shareState.value.currentUser.id
                viewModel.addContact(bearerToken, currentUserId, ContactId(contact.id))
                viewModel.setProcessingContact(contact)
                val isRequiredTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                if (isRequiredTiramisu && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    showNotification(contact)
                }
            }
        )
        binding.recyclerContacts.adapter = addContactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(requireContext()))

        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.shareState.collect { state ->
                val mappedUserList =
                    state.userList.map {
                        Contact(
                            id = it.id,
                            name = it.name.toString(),
                            career = it.career.toString(),
                            address = it.address.toString(),
                            isAdded = viewModel.isUserAddedToContact(
                                sharedViewModel.shareState.value.userContactIdList,
                                it.id
                            )
                        )
                    }
                addContactsAdapter.submitList(mappedUserList)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(contact: Contact) {
        val notification =
            createOnAddNotification(requireContext(), contact.id, contact.name)
        NotificationManagerCompat.from(requireContext())
            .notify(NOTIFICATION_ID, notification)
    }

    private fun setArrowBackClickListener() {
        binding.buttonArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateAllUsers() {
        val bearerToken = sharedViewModel.shareState.value.accessToken
        viewModel.getAllUsers(bearerToken)
    }
}