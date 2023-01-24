package sdumchykov.task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import sdumchykov.task3.databinding.FragmentContactProfileBinding
import sdumchykov.task3.extensions.setImage
import sdumchykov.task3.model.ContactProfileViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ContactProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactProfileFragment :
    BaseFragment<FragmentContactProfileBinding>(FragmentContactProfileBinding::inflate) {
    private val viewModelContact: ContactProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imageButtonArrowBack.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_contactProfileFragment_to_myContactsFragment)
            }
            textViewName.text = viewModelContact.contact.value?.name
            textViewProfession.text = viewModelContact.contact.value?.profession
            val drawable = R.drawable.ic_profile_image
            imageViewPicture.setImage(this@ContactProfileFragment, drawable)
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ContactProfileFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ContactProfileFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}