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
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentAddContactsBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleContactResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.notification.NOTIFICATION_ID
import com.dumchykov.socialnetworkdemo.ui.notification.createOnAddNotification
import com.dumchykov.socialnetworkdemo.ui.screens.addcontacts.adapter.AddContactsAdapter
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.MyContactsFragment.Companion.INDICATOR_CONTACT_ID
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.MyContactsFragment.Companion.INDICATOR_CONTACT_NAME
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
                val contact = viewModel.processingContact.value
                showNotification(contact.id, contact.name)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorizedUser.collect { user ->
                if (user.id == -1) return@collect

                setUserContactIdList()
                updateAllUsers()
                setArrowBackClickListener()
                initAdapter()
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
            viewModel.addContactsState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    binding.layoutProgress.root.visibility = View.GONE
                    when ((state as ResponseState.Success<*>).data) {
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
                    INDICATOR_CONTACT_ID to userId,
                    INDICATOR_CONTACT_NAME to userName
                )
                findNavController().navigate(
                    R.id.action_addContactsFragment_to_detailsFragment,
                    bundle
                )
            },
            onAddListener = { indicatorContact ->
                val bearerToken = sharedViewModel.shareState.value.accessToken
                val currentUserId = viewModel.authorizedUser.value.id
                viewModel.addContact(bearerToken, currentUserId, ContactId(indicatorContact.id))
                viewModel.setProcessingContact(indicatorContact)
                val isRequiredTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                if (isRequiredTiramisu && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    showNotification(indicatorContact.id, indicatorContact.name)
                }
            }
        )
        binding.recyclerContacts.adapter = addContactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(requireContext()))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allUsers.collect { users ->
                val mappedList = users.map {
                    it.copy(
                        isAdded = viewModel.isUserAddedToContact(it.id)
                    )
                }
                addContactsAdapter.submitList(mappedList)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        indicatorContactId: Int,
        indicatorContactName: String,
    ) {
        val notification =
            createOnAddNotification(requireContext(), indicatorContactId, indicatorContactName)
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

    private fun setUserContactIdList() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.shareState.collect { state ->
                viewModel.setUserContactIdList(state.userContactIdList)
                val mappedList = viewModel.allUsers.value.map {
                    it.copy(
                        isAdded = viewModel.isUserAddedToContact(it.id)
                    )
                }
                addContactsAdapter.submitList(mappedList)
            }
        }
    }
}