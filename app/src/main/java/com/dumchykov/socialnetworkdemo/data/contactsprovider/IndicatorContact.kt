package com.dumchykov.socialnetworkdemo.data.contactsprovider

data class IndicatorContact(
    val id: Int = 0,
    val name: String = "",
    val career: String = "",
    val address: String = "",
    val isSelected: Boolean = false,
    val isAdded: Boolean = false,
)