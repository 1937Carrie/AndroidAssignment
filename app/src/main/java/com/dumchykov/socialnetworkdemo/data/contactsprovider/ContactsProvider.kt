package com.dumchykov.socialnetworkdemo.data.contactsprovider

fun getHardcodedContacts(): List<IndicatorContact> {
    val indicatorContacts = mutableListOf<IndicatorContact>()
    repeat(20) {
        indicatorContacts.add(
            IndicatorContact(
                id = it,
                name = "Name №$it",
                career = "Career №$it",
                address = "Address №$it"
            )
        )
    }
    return indicatorContacts
}