package com.dumchykov.socialnetworkdemo.data.contactsprovider

fun getHardcodedContacts(): List<Contact> {
    val contacts = mutableListOf<Contact>()
    repeat(20) {
        contacts.add(
            Contact(
                id = it,
                name = "Name №$it",
                career = "Career №$it",
                address = "Address №$it"
            )
        )
    }
    return contacts
}