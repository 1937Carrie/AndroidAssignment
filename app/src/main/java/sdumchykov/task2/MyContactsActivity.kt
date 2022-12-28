package sdumchykov.task2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import sdumchykov.task2.adapter.ItemAdapter
import sdumchykov.task2.data.Datasource
import sdumchykov.task2.databinding.ActivityMyContactsBinding
import sdumchykov.task2.model.Contact

class MyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyContactsBinding
    private var liveDataContacts = MutableLiveData<List<Contact>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageButtonArrowBackSetOnClickListener()

//        val myDataSet = Datasource().contactList()

        liveDataContacts.value = Datasource().contactList()

        val recyclerView = binding.recyclerViewContacts
        recyclerView.adapter = ItemAdapter(this, liveDataContacts.value!!)

    }


    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageButtonArrowBack.setOnClickListener {
            finish()
        }
    }
}