package com.birthdayapp.sample.data.model

data class Profile(
    val name: String,
    val birthDate: Long,
    val photoPath: String,
) {

    companion object {

        const val DEFAULT_BIRTH_DATE = -1L
    }
}
