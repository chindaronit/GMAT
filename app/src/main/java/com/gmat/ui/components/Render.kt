package com.gmat.ui.components


import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

//@Composable
//fun RenderPainterIcon(
//    id: Int,
//    desc: String="",
//    modifier: Modifier
//){
//    Icon(
//        painter = painterResource(id),
//        contentDescription = desc,
//        modifier = modifier
//    )
//}


@Composable
fun RenderPainterIcon(
    id: Int,  // Drawable resource ID
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified  // Optional tint color
) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = null,  // Add description if needed
        tint = tint,
        modifier = modifier
    )
}
