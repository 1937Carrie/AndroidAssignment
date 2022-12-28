package sdumchykov.task2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import sdumchykov.task2.R
import sdumchykov.task2.model.Contact


class ItemAdapter(private val context: Context, private val dataSet: List<Contact>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val name: TextView
        val profession: TextView

        init {
            image = view.findViewById(R.id.imageViewPhoto)
            name = view.findViewById(R.id.textViewName)
            profession = view.findViewById(R.id.textViewProfession)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.contact_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(context).load("https://picsum.photos/200")
            .signature(ObjectKey(System.currentTimeMillis().toString())).circleCrop()
            .into(viewHolder.image)
        viewHolder.name.text = dataSet[position].name
        viewHolder.profession.text = dataSet[position].profession
    }

    override fun getItemCount() = dataSet.size

}