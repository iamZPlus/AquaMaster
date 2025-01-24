package edu.sspu.am

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


internal data class VariableValePair(
    val name: String,
    val value: String
)


private fun String.decodeVariable(vararg pairs: VariableValePair): String {
    var result = this
    for (pair in pairs) result = result.replace(Regex("\\{(?i)${pair.name}\\}"), pair.value)
    return result
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenJumper(
    ui: UI,
    modifier: Modifier = Modifier,
    text: MutableState<String> = remember { mutableStateOf("") }
) {
    var t by text
    val font by ui.font.collectAsState()
    var isError by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.inversePrimary,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                TextField(
                    value = t,
                    onValueChange = { t = it },
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f)
                        .border(
                            BorderStroke(
                                0.dp,
                                Color.Transparent
                            )
                        ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.inversePrimary

                    ),
                    isError = isError,
                    singleLine = true,
                    maxLines = 1,
                    placeholder = {
                        Text(
                            text = "示例: @app.Home",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            fontWeight = FontWeight.Medium,
                            fontFamily = font
                        )
                    }
                )
            }
            Card(
                modifier = Modifier
                    .size(100.dp, 55.dp)
                    .combinedClickable(
                        onLongClick = { t = "" }
                    ) {
                        val path = t.decodeVariable(
                            VariableValePair("current", ui.screen.value.toString()),
                            VariableValePair("current.meta", ui.screen.value.meta.toString()),
                            VariableValePair("current.father", ui.screen.value.father.toString()),
                            VariableValePair("meta.app", MetaScreens.app.toString()),
                            VariableValePair("meta.exit", MetaScreens.exit.toString()),
                            VariableValePair("meta.splash", MetaScreens.splash.toString()),
                            VariableValePair("meta.surprise", MetaScreens.surprise.toString()),
                            VariableValePair("root.home", RootScreens.List.toString()),
                            VariableValePair("root.list", RootScreens.List.toString()),
                            VariableValePair("root.cloud", RootScreens.Cloud.toString()),
                            VariableValePair("root.community", RootScreens.Community.toString()),
                            VariableValePair("root.personal", RootScreens.Personal.toString()),
                        )
                        val checkResult = checkScreenPathSyntax(path)
                        isError = !checkResult.valid
                        if (checkResult.message != null) ui.scope.launch {
                            ui.snack.showSnackbar(checkResult.message, "OK", duration = SnackbarDuration.Short)
                        }
                        if (checkResult.valid) ui goto path
                    },
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "前往",
                        modifier = Modifier
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = font
                    )
                }
            }
        }
    }
}
