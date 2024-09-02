package com.dumchykov.socialnetworkdemo.ui.mycontacts

import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact

data class MyContactsState(
    val contacts:List<Contact> = emptyList(),
    val isMultiselect: Boolean = false,
)