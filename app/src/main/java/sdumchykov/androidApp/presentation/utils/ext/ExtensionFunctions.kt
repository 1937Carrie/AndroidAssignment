package sdumchykov.androidApp.presentation.utils.ext

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import sdumchykov.androidApp.R

fun AppCompatImageView.setImageCacheless(photo: String?) {
    if (photo != null) {
//        Glide.with(this).load(photo).signature(ObjectKey(System.currentTimeMillis().toString()))
//            .circleCrop().into(this)
        Glide
            .with(this)
            .load(photo)
            .circleCrop()
            .into(this)
    } else {
        Glide
            .with(this)
            .load(R.drawable.ic_profile_image_girl)
            .circleCrop()
            .into(this)
    }
}

fun AppCompatImageView.setImage(resourceId: Uri?) {
    Glide.with(this).load(resourceId).circleCrop().into(this)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}
