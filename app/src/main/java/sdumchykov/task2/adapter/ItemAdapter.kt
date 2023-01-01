package sdumchykov.task2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import sdumchykov.task2.databinding.ContactItemBinding
import sdumchykov.task2.model.Contact


class ItemAdapter(private val onDeleteCallback: (Contact) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var contacts = mutableListOf<Contact>()

    @SuppressLint("NotifyDataSetChanged")
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
            Glide.with(viewHolder.itemView.context).load(contact.photo)
                .signature(ObjectKey(System.currentTimeMillis().toString())).circleCrop()
                .into(imageViewPhoto)

            imageButtonDelete.setOnClickListener { onDeleteCallback(contact) }
        }
    }

    override fun getItemCount() = contacts.size

}