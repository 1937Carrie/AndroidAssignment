package sdumchykov.task2

import android.os.Bundle
import android.provider.ContactsContract.Contacts
import androidx.appcompat.app.AppCompatActivity
import sdumchykov.task2.databinding.ActivityMyContactsBinding

class MyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyContactsBinding
    var states: ArrayList<Contact> = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageButtonArrowBackSetOnClickListener()

        setInitialData()

        val recyclerView = binding.recyclerViewContacts
        recyclerView.adapter = ContactAdapter(states)
    }

    private fun setInitialData() {
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
        states.add(Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image))
    }

    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageButtonArrowBack.setOnClickListener {
            finish()
        }
    }
}