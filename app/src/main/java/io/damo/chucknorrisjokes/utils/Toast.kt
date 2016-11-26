package io.damo.chucknorrisjokes.utils

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast
        .makeText(activity, message, length)
        .show()
}
