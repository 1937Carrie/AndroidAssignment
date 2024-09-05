package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.dialogfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class AddContactFragmentFactory(
    private val onConfirmAction: (name: String, career: String, address: String) -> Unit,
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            AddContactDialog::class.java -> AddContactDialog(onConfirmAction)
            else -> super.instantiate(classLoader, className)
        }
}