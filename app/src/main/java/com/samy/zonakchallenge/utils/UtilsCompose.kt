package com.samy.zonakchallenge.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Int.sdp(): Dp {
    val density = LocalDensity.current.density
    return (this * density).dp
}

// Extension function for Float to convert to TextUnit using ssp
@Composable
fun Int.ssp(): TextUnit {
    val density = LocalDensity.current.density
    return (this * density).sp
}
fun String.truncatedText(maxLength:Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }

}