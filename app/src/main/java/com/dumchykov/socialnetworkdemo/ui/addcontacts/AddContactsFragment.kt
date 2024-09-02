package com.dumchykov.socialnetworkdemo.ui.addcontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.databinding.FragmentAddContactsBinding
import com.dumchykov.socialnetworkdemo.ui.addcontacts.adapter.AddContactsAdapter
import com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.ContactsItemDecoration
import com.dumchykov.socialnetworkdemo.ui.pager.Page
import com.dumchykov.socialnetworkdemo.ui.pager.PagerFragment

class AddContactsFragment : Fragment() {
    private var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var addContactsAdapter: AddContactsAdapter
    private val viewModel: AddContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArrowBackClickListener()
        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setArrowBackClickListener() {
        binding.buttonArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAdapter() {
        addContactsAdapter = AddContactsAdapter()
        binding.recyclerContacts.adapter = addContactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(requireContext()))

    }
}