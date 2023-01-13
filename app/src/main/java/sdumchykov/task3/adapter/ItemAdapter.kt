package sdumchykov.task3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sdumchykov.task3.databinding.ContactItemBinding
import sdumchykov.task3.extensions.setImageCacheless
import sdumchykov.task3.model.Contact

class ItemAdapter(private val onDeleteCallback: (Contact) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var contacts = mutableListOf<Contact>()

    fun setContactList(contacts: List<Contact>) {
        this.contacts = contacts.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder.binding) {
            val contact = contacts[position]
            textViewName.text = contact.name
            textViewProfession.text = contact.profession
            imageViewPhoto.setImageCacheless(viewHolder.itemView.context, contact.photo)

            imageButtonDelete.setOnClickListener { onDeleteCallback(contact) }
        }
    }

    override fun getItemCount() = contacts.size

}