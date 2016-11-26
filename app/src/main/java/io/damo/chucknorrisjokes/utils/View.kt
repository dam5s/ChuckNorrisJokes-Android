package io.damo.chucknorrisjokes.utils

import android.view.View

fun View.setVisibleIf(condition: Boolean, otherwise: Int = View.GONE) {
    val newVisibility = if (condition) View.VISIBLE else otherwise
    visibility = newVisibility
}
