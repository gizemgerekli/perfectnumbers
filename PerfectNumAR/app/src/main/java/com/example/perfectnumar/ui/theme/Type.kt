package com.example.perfectnumar.ui.theme

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

object TextStyle {
    // Text boyutları (sp cinsinden)
    const val BODY_LARGE_SIZE = 16f
    const val TITLE_LARGE_SIZE = 22f
    const val LABEL_MEDIUM_SIZE = 14f

    // Text stilleri
    fun applyBodyLargeStyle(textView: TextView) {
        textView.apply {
            textSize = BODY_LARGE_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            letterSpacing = 0.5f / BODY_LARGE_SIZE
        }
    }

    fun applyTitleLargeStyle(textView: TextView) {
        textView.apply {
            textSize = TITLE_LARGE_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            letterSpacing = 0f
        }
    }

    fun applyLabelMediumStyle(textView: TextView) {
        textView.apply {
            textSize = LABEL_MEDIUM_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            letterSpacing = 0.5f / LABEL_MEDIUM_SIZE
        }
    }
}