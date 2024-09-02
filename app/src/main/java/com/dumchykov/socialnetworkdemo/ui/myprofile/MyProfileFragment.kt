package com.dumchykov.socialnetworkdemo.ui.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.databinding.FragmentMyProfileBinding
import com.dumchykov.socialnetworkdemo.ui.pager.Page
import com.dumchykov.socialnetworkdemo.ui.pager.PagerFragment
import kotlinx.coroutines.launch

class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyProfileViewModel by viewModels {
        MyProfileViewModel.factory(DataStoreProvider(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        setLogOutClickListener()
        setEditProfileClickListener()
        setViewMyContactsClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewMyContactsClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
            (parentFragment as PagerFragment).changeCurrentItem(Page.MyContacts.ordinal)
        }
    }

    private fun setEditProfileClickListener() {
        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_pagerFragment_to_editProfileFragment)
        }
    }

    private fun setLogOutClickListener() {
        binding.textLogOut.setOnClickListener {
            viewModel.clearCredentials()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.exitFlag.collect {
                    if (it) {
                        findNavController().navigate(R.id.action_pagerFragment_to_loginFragment)
                    }
                }
            }
        }
    }

    private fun setName() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.name.collect { name ->
                binding.textName.text = name
            }
        }
    }
}