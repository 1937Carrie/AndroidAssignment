package com.dumchykov.socialnetworkdemo.ui.mycontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.databinding.FragmentMyContactsBinding
import com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.ContactsAdapter
import com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.ContactsItemDecoration
import com.dumchykov.socialnetworkdemo.ui.mycontacts.dialogfragment.AddContactDialog
import com.dumchykov.socialnetworkdemo.ui.mycontacts.dialogfragment.AddContactFragmentFactory
import com.dumchykov.socialnetworkdemo.ui.pager.Page
import com.dumchykov.socialnetworkdemo.ui.pager.PagerFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MyContactsFragment : Fragment() {
    private var _binding: FragmentMyContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: MyContactsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        parentFragmentManager.fragmentFactory =
            AddContactFragmentFactory { name, career, address ->
                viewModel.addContact(name, career, address)
            }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackPressDispatcher()
        setArrowBackClickListener()
        setAddContactClickListener()
        setFabClickListener()
        initAdapter()
        postponeEnterTransition()
        binding.recyclerContacts.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setBackPressDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            onBackPressed = {
                (parentFragment as PagerFragment).changeCurrentItem(Page.MyProfile.ordinal)
            }
        )
    }

    private fun setArrowBackClickListener() {
        binding.buttonArrowBack.setOnClickListener {
            (parentFragment as PagerFragment).changeCurrentItem(Page.MyProfile.ordinal)
        }
    }

    private fun setAddContactClickListener() {
        binding.textAddContacts.setOnClickListener {
            val newFragment = AddContactDialog { name, career, address ->
                viewModel.addContact(name, career, address)
            }
            newFragment.show(parentFragmentManager, "add contact")
        }
    }

    private fun setFabClickListener() {
        binding.fab.setOnClickListener {
            viewModel.multipleRemovingContact()
        }
    }

    private fun initAdapter() {
        contactsAdapter = ContactsAdapter(
            context = requireContext(),
            onClick = { view, contact ->
                val contactBundle = bundleOf(
                    "contact.id" to contact.id.toString(),
                    "contact.name" to contact.name,
                    "contact.career" to contact.career,
                    "contact.address" to contact.address,
                )
                val extras = FragmentNavigatorExtras(view to "${contact.id}_${contact.name}")
                findNavController().navigate(
                    R.id.action_pagerFragment_to_detailsFragment,
                    contactBundle,
                    null,
                    extras
                )
            },
            onDelete = { contact ->
                viewModel.removeContact(contact.id)
                Snackbar
                    .make(
                        binding.recyclerContacts,
                        getString(R.string.contact_has_been_deleted, contact.name),
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(getString(R.string.undo)) {
                        viewModel.addContact(contact)
                    }
                    .show()
            },
            onChangeSelect = { contact ->
                viewModel.updateContactCheckState(contact)
            }
        )

        binding.recyclerContacts.adapter = contactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(requireContext()))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myContactsState.collect { myContactsState ->
                contactsAdapter.updateMultiSelectState { myContactsState.isMultiselect }
                contactsAdapter.submitList(myContactsState.contacts)

                binding.fab.visibility =
                    if (myContactsState.isMultiselect) View.VISIBLE else View.GONE
            }
        }
    }
}