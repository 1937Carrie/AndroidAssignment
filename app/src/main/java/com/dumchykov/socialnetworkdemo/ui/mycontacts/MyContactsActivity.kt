package com.dumchykov.socialnetworkdemo.ui.mycontacts

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.databinding.ActivityMyContactsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyContactsBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: MyContactsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right)
            insets
        }
        initAdapter()
    }

    private fun initAdapter() {
        contactsAdapter = ContactsAdapter { contact ->
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
        }

        binding.recyclerContacts.adapter = contactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(this))

        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                contactsAdapter.submitList(contacts)
            }
        }
    }
}