package sdumchykov.task3.extensions

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

fun AppCompatImageView.setImageCacheless(context: Context, photo: String?) {
    Glide.with(context).load(photo).signature(ObjectKey(System.currentTimeMillis().toString()))
        .circleCrop().into(this)
}

fun AppCompatImageView.setImage(activity: FragmentActivity, resourceId: Int?) {
    Glide.with(activity).load(resourceId).circleCrop().into(this)
}