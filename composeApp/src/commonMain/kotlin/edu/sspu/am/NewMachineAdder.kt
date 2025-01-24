package edu.sspu.am

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.HideSource
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material.icons.filled._5g
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material.icons.outlined.HideSource
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material.icons.outlined._5g
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun NewMachineAdder(
    ui: UI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .size(25.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable {
                    expanded = !expanded
                },
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer),
            shape = MaterialTheme.shapes.large
        ) {
            DropdownMenuItem(
                text = {
                    Text("搜索新设备")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("扫码识别")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.QrCodeScanner,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("查找云端设备")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined._5g,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("新建空设备")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.HideSource,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("新建设备模板")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.TableView,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("新建设备组")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.GroupWork,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("新建分类")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text("新建虚拟设备（测试）")
                },
                onClick = {
                    expanded = false
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Print,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )
        }
    }
}