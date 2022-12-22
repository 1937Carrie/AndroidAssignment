package sdumchykov.task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private val dataSet: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

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
        dataSet[position].photo?.let { viewHolder.image.setImageResource(it) }
        viewHolder.name.text = dataSet[position].name
        viewHolder.profession.text = dataSet[position].profession
    }

    override fun getItemCount() = dataSet.size

}