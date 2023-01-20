package sdumchykov.task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import sdumchykov.task3.databinding.FragmentMyProfileBinding
import sdumchykov.task3.extensions.setImage

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val EMAIL = "email"
private const val SYMBOL_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"

/**
 * A simple [Fragment] subclass.
 * Use the [MyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
//    private val signUpEmailModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextToTextName()
        setMainPicture()
        setURIToImageInstagram()

        buttonViewMyContactsSetOnClickListener()
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
////            val intent = Intent(this, MyContactsActivity::class.java)
////            startActivity(intent)
//            val fragmentMyContacts: Fragment = MyContactsFragment()
//            val fragment: Fragment? =
//                supportFragmentManager.findFragmentByTag(MyContactsFragment::class.java.simpleName)
//            if (fragment !is MyContactsFragment) {
//                supportFragmentManager.beginTransaction().add(
//                    R.id.main_activity_constraint_layout,
//                    fragmentMyContacts,
//                    MyContactsFragment::class.java.simpleName
//                ).commit()
//            }
//
//
//            binding.buttonEditProfile.visibility = View.GONE
//            binding.buttonViewMyContacts.visibility = View.GONE
//
        }
    }

    private fun setURIToImageInstagram() {
//        TODO для фрагменту такий метод не працює
//        binding.imageInstagram.setOnClickListener {
//            startActivity(
//                Intent(
//                    Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)
//                )
//            )
//        }
    }

    private fun setTextToTextName() {
        val textViewName: AppCompatTextView? = view?.findViewById(R.id.textViewName)
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("data")
            val splitted = result?.substring(0, result.indexOf('@'))?.split(Regex(PATTERN_NON_CHARACTER))

            if (splitted?.size!! > 1) {
                val firstName = splitted[0].replaceFirstChar { it.uppercase() }
                val secondName = splitted[1].replaceFirstChar { it.uppercase() }
                val textContent = "$firstName $secondName"

                binding.textViewName.text = textContent
//                textViewName!!.text = textContent
            } else {
                binding.textViewName.text = result.substring(0, result.indexOf(SYMBOL_AT))
                val d = 1

//                textViewName!!.text = result.substring(0, result.indexOf('@'))
            }
        }
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.image

//        TODO чому при встановленні зображення через binding, зображення є прозорим/відсутнє?
//        binding.imageViewPicture.apply {
//            setImage(this@MyProfileFragment, drawableSource)
//        }

        val image: AppCompatImageView? = view?.findViewById(R.id.imageViewPicture)
        image?.setImage(this@MyProfileFragment, drawableSource)

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment MyProfileFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MyProfileFragment().apply {
//
//            }
//    }
}