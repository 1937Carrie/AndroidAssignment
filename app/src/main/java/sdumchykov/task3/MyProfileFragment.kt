package sdumchykov.task3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import sdumchykov.task3.databinding.FragmentMyProfileBinding
import sdumchykov.task3.extensions.setImage

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val SYMBOL_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val REQUEST_KEY = "requestKey"
private const val DATA = "data"

class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setURIToImageInstagram()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextToTextName()
        setMainPicture()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {

            if (FEATURE_FLAG) {
                Navigation.findNavController(binding.root).navigate(R.id.action_myProfileFragment_to_myContactsFragment)
            } else {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, MyContactsFragment())
                    addToBackStack("")
                }
            }
        }
    }

    private fun setURIToImageInstagram() {
        binding.imageInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image

        binding.imageViewPicture.apply {
            setImage(this@MyProfileFragment, drawableSource)
        }
    }

    private fun setTextToTextName() {
        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(DATA) ?: ""
            val splitted =
                result.substring(0, result.indexOf(SYMBOL_AT)).split(Regex(PATTERN_NON_CHARACTER))

            binding.textViewName.text = if (splitted.size > 1) {
                val firstName = splitted[0].replaceFirstChar { it.uppercase() }
                val secondName = splitted[1].replaceFirstChar { it.uppercase() }
                val textContent = "$firstName $secondName"

                textContent
            } else {
                result.substring(0, result.indexOf(SYMBOL_AT))
            }
        }
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