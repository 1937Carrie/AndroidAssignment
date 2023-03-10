package sdumchykov.androidApp.presentation.utils.ext

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import sdumchykov.androidApp.R

// TODO optimize the installation of images so that there is not so much garbage in memory
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

fun AppCompatImageView.setImage(resourceId: Int?) {
    Glide.with(this).load(resourceId).circleCrop().into(this)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun createToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}
