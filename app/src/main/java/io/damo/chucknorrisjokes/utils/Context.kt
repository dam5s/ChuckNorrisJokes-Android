package io.damo.chucknorrisjokes.utils

import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import io.damo.chucknorrisjokes.R

fun Context.color(@ColorRes colorId: Int): Int {
    if (Build.VERSION.SDK_INT >= 23)
        return getColor(colorId)
    else
        return resources.getColor(colorId)
}

fun Context.highlightName(text: String, name: String = "Chuck Norris"): Spannable {
    val spannable = SpannableString(text)
    val nameStart = text.indexOf(name)

    if (nameStart > -1) {
        spannable.setSpan(
            ForegroundColorSpan(color(R.color.highlighted_text)),
            nameStart,
            nameStart + name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannable
}
