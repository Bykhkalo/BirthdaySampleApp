package com.birthdayapp.sample.data.model

data class Profile(
    val name: String,
    val birthDate: Long,
    val photoPath: String,
) {

    companion object {

        const val DEFAULT_BIRTH_DATE = -1L

        fun empty(): Profile = Profile(
            name = "",
            birthDate = DEFAULT_BIRTH_DATE,
            photoPath = "",
        )
    }
}
