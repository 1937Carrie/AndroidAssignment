package com.dumchykov.socialnetworkdemo.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dumchykov.socialnetworkdemo.R

@Preview(heightDp = 40)
@Composable
fun ButtonGoogle() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxSize(),
        colors = ButtonDefaults.buttonColors(
            Color.White,
            Color.White,
            Color.White,
            Color.White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = ""
            )
            Text(
                text = stringResource(R.string.google).uppercase(),
                color = colorResource(R.color.black),
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.opensans_bold,
                        FontWeight.W600,
                        FontStyle.Normal
                    )
                ),
                letterSpacing = 1.5.sp
            )
        }
    }
}