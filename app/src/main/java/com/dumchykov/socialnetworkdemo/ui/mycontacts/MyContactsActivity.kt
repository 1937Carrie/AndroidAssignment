package com.dumchykov.socialnetworkdemo.ui.mycontacts

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.getHardcodedContacts
import com.dumchykov.socialnetworkdemo.databinding.ActivityMyContactsBinding

class MyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyContactsBinding
    private lateinit var contactsAdapter: ContactsAdapter

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
            Log.d("AAA", "$contact have been deleted")
        }

        binding.recyclerContacts.adapter = contactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(this))

        contactsAdapter.submitList(getHardcodedContacts().toMutableList())
    }
}