package com.dumchykov.socialnetworkdemo.domain.logic

import com.dumchykov.socialnetworkdemo.domain.room.models.AuthorizedUserDBO
import com.dumchykov.socialnetworkdemo.ui.screens.myprofile.Contact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact as ApiContact

fun ApiContact.toAuthorizedUserDBO(): AuthorizedUserDBO {
    return AuthorizedUserDBO(
        id = id,
        name = name,
        email = email,
        phone = phone,
        career = career,
        address = address,
        birthday = birthday,
        facebook = facebook,
        instagram = instagram,
        twitter = twitter,
        linkedin = linkedin,
        image = image,
        created_at = created_at,
        updated_at = updated_at
    )
}

fun AuthorizedUserDBO.toContact(): Contact {
    return Contact(
        id = id,
        name = name.toString(),
        email = email.toString(),
        phone = phone.toString(),
        career = career.toString(),
        address = address.toString(),
        birthday = birthday.toString(),
        facebook = facebook.toString(),
        instagram = instagram.toString(),
        twitter = twitter.toString(),
        linkedin = linkedin.toString(),
        image = image.toString(),
        created_at = created_at,
        updated_at = updated_at
    )
}

fun Contact.toApiContact(): ApiContact {
    return ApiContact(
        id = id,
        name = name,
        email = email,
        phone = phone,
        career = career,
        address = address,
        birthday = birthday,
        facebook = facebook,
        instagram = instagram,
        twitter = twitter,
        linkedin = linkedin,
        image = image,
        created_at = created_at,
        updated_at = updated_at
    )
}