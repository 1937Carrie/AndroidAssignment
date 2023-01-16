package sdumchykov.task3.extensions

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

fun AppCompatImageView.setImageCacheless(context: Context, photo: String?) {
    Glide.with(context).load(photo).signature(ObjectKey(System.currentTimeMillis().toString()))
        .circleCrop().into(this)
}

fun AppCompatImageView.setImage(fragment: Fragment, resourceId: Int?) {
    Glide.with(fragment).load(resourceId).circleCrop().into(this)
}