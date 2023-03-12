package sdumchykov.androidApp.presentation.signUpExtended

import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.navigation.Navigation.findNavController
import sdumchykov.androidApp.databinding.FragmentSignUpExtendedBinding
import sdumchykov.androidApp.presentation.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpExtendedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpExtendedFragment :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {

    override fun setListeners() {
        buttonForwardSetListener()
        textFieldPhoneSetListener()
    }

    override fun setObservers() {
    }

    private fun buttonForwardSetListener() {
        binding.buttonSignUpExtendedForward.setOnClickListener {


            val action =
                SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToMainActivity()
            findNavController(binding.root).navigate(action)
        }
    }

    private fun textFieldPhoneSetListener() {
        binding.textInputEditTextSignUpExtendedMobilePhone.addTextChangedListener(
            PhoneNumberFormattingTextWatcher()
        )
    }
}