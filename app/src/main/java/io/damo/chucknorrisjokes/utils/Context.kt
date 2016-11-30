package io.damo.chucknorrisjokes.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.damo.chucknorrisjokes.R


@Suppress("DEPRECATION")
fun Context.color(@ColorRes colorId: Int): Int {
    if (Build.VERSION.SDK_INT >= 23)
        return getColor(colorId)
    else
        return resources.getColor(colorId)
}

fun Fragment.color(@ColorRes colorId: Int)
    = context.color(colorId)


@Suppress("DEPRECATION")
fun Context.drawable(@DrawableRes drawableId: Int): Drawable {
    if (Build.VERSION.SDK_INT >= 23)
        return getDrawable(drawableId)
    else
        return resources.getDrawable(drawableId)
}

fun Fragment.drawable(@DrawableRes drawableId: Int)
    = context.drawable(drawableId)


fun Context.highlightName(text: String, name: String = "Chuck Norris"): Spannable {
    val spannable = SpannableString(text)
    val nameStart = text.indexOf(name)

    if (nameStart > -1) {
        spannable.setSpan(
            ForegroundColorSpan(color(R.color.highlightedText)),
            nameStart,
            nameStart + name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannable
}

fun Fragment.highlightName(text: String, name: String = "Chuck Norris")
    = context.highlightName(text, name)


fun Context.snackbar(parentView: View, @StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG) =
    Snackbar
        .make(parentView, text, duration)
        .apply {
            val textView = view.findViewById(android.support.design.R.id.snackbar_text) as TextView
            val barColor = colorWithOpacity(R.color.primary, 204)

            view.setBackgroundColor(barColor)
            textView.setTextColor(color(R.color.highlightedTextDarkBg))
        }

fun Fragment.snackbar(parentView: View, @StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG)
    = context.snackbar(parentView, text, duration)


fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast
        .makeText(this, message, length)
        .show()
}

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT)
    = context.toast(message, length)


fun Context.toast(@StringRes messageId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast
        .makeText(this, messageId, length)
        .show()
}

fun Fragment.toast(@StringRes messageId: Int, length: Int = Toast.LENGTH_SHORT)
    = context.toast(messageId, length)


fun Context.colorWithOpacity(@ColorRes colorId: Int, opacity: Int): Int {
    val color = color(colorId)
    return Color.argb(opacity, red(color), green(color), blue(color))
}

fun Fragment.colorWithOpacity(@ColorRes colorId: Int, opacity: Int)
    = context.colorWithOpacity(colorId, opacity)
