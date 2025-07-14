package com.rombsquare.solocards.presentation.screens.true_false_mode.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.presentation.theme.SolocardsTheme

@Composable
fun YesNoButtons(
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row {
            Button(
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                ,
                onClick = { onClick(true) },
                shape = RoundedCornerShape(10),
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Yes",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(Modifier.width(8.dp))

            Button(
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(255, 138, 128, 255),
                ),
                onClick = { onClick(false) },
                shape = RoundedCornerShape(10),
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = "No",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    SolocardsTheme {
        YesNoButtons {  }
    }
}