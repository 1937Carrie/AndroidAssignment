package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts

import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact

data class MyContactsState(
    val indicatorContacts: List<IndicatorContact> = emptyList(),
    val isMultiselect: Boolean = false,
)