package com.birthdayapp.sample.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import com.birthdayapp.sample.R

class MainNavigation(private val navController: NavController) {

    fun back() {
        navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStack(resId: Int, include: Boolean) {
        navController.popBackStack(resId, include)
    }

    fun imagePickerDialog() {
        navController.navigate(ID_IMAGE_PICKER)
    }

    companion object {
        val ID_IMAGE_PICKER = R.id.imagePickerFragment

        private const val RESULT_KEY_IMAGE_PICKER_DIALOG = "DATA_KEY_IS_IMAGE_PICKED"
        private const val DATA_KEY_IS_IMAGE_PICKED = "RESULT_KEY_IMAGE_PICKER_DIALOG"

        fun Fragment.setImagePickerResultListener(resultAction: (Boolean) -> Unit) =
            setFragmentResultListener(RESULT_KEY_IMAGE_PICKER_DIALOG) { _, bundle ->
                resultAction(
                    bundle.getBoolean(DATA_KEY_IS_IMAGE_PICKED),
                )
            }

        fun Fragment.setImagePickerPermissionsResult(isPicked: Boolean) {
            setFragmentResult(
                RESULT_KEY_IMAGE_PICKER_DIALOG,
                bundleOf(
                    DATA_KEY_IS_IMAGE_PICKED to isPicked,
                )
            )
        }
    }
}
