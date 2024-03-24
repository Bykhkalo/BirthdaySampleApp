package com.birthdayapp.sample.core.extensions.result

enum class ResultStatus {
    AUTHORIZATION_ERROR,
    CANCELED,
    GENERAL_ERROR,
    IO_ERROR,
    NETWORK_ERROR,
    NOT_FOUND,
    RESOURCE_CONFLICT,
    SUCCESS,
    TIMEOUT_ERROR,
    USER_ERROR;

    companion object {
        fun from(statusString: String?): ResultStatus =
            if (statusString == null) GENERAL_ERROR
            else {
                ResultStatus.valueOf(statusString)
            }
    }
}