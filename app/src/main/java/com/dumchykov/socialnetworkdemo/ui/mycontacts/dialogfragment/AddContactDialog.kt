package com.dumchykov.socialnetworkdemo.ui.mycontacts.dialogfragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.dumchykov.socialnetworkdemo.databinding.DialogAddContactBinding

class AddContactDialog(
    private val onConfirmAction: (name: String, career: String, address: String) -> Unit,
) :
    DialogFragment() {
    private var _binding: DialogAddContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            _binding = DialogAddContactBinding.inflate(layoutInflater)

            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            builder.setView(binding.root)
                // Add action buttons.
                .setPositiveButton("Confirm") { _, _ ->
                    with(binding) {
                        onConfirmAction(
                            editTextName.text.toString(),
                            editTextCareer.text.toString(),
                            editTextAddress.text.toString()
                        )

                        editTextName.setText("")
                        editTextCareer.setText("")
                        editTextAddress.setText("")
                    }
                    dialog?.cancel()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    with(binding) {
                        editTextName.setText("")
                        editTextCareer.setText("")
                        editTextAddress.setText("")
                    }
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}