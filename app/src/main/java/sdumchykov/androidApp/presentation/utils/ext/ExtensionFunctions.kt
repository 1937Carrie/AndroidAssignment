package sdumchykov.androidApp.presentation.utils.ext

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

// TODO optimize the installation of images so that there is not so much garbage in memory
fun AppCompatImageView.setImageCacheless(photo: String?) {
    Glide.with(this).load(photo).signature(ObjectKey(System.currentTimeMillis().toString()))
        .circleCrop().into(this)
}

fun AppCompatImageView.setImage(resourceId: Int?) {
    Glide.with(this).load(resourceId).circleCrop().into(this)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}
