package sdumchykov.androidApp.presentation.contactDetailView

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentContactProfileBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.contacts.MyContactsViewModel
import sdumchykov.androidApp.presentation.utils.ext.setImage

@AndroidEntryPoint
class ContactProfileFragment :
    BaseFragment<FragmentContactProfileBinding>(FragmentContactProfileBinding::inflate) {
    private val args: ContactProfileFragmentArgs by navArgs()
    private val contactListViewModel: MyContactsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            imageButtonArrowBack.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_contactProfileFragment_to_myContactsFragment)
            }

            val contactId = args.contactId

            val user = contactListViewModel.userLiveData.value?.get(contactId)
            textViewName.text = user?.name
            textViewProfession.text = user?.profession

            val drawable = R.drawable.ic_profile_image
            imageViewPicture.setImage(drawable)
        }
    }

}