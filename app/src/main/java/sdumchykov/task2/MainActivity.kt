package sdumchykov.task2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import sdumchykov.task2.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.image

        Glide.with(this)
            .load(drawableSource).circleCrop().into(binding.imageViewPicture)
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
            val intent = Intent(this, MyContactsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setURIToImageInstagram() {
        binding.imageInstagram.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/p/BDdr32ZrvgP/")
                )
            )
        }
    }

    private fun setTextToTextName() {
        val signupEmail = intent.getStringExtra("email")
        val splitted = signupEmail?.substring(0, signupEmail.indexOf('@'))?.split(Regex("\\W"))

        if (splitted?.size!! > 1) {
            val firstName = splitted[0].replaceFirstChar { it.uppercase() }
            val secondName = splitted[1].replaceFirstChar { it.uppercase() }
            val textContent = "$firstName $secondName"

            binding.textViewName.text = textContent
        } else {
            binding.textViewName.text = signupEmail.substring(0, signupEmail.indexOf('@'))
        }
    }

}