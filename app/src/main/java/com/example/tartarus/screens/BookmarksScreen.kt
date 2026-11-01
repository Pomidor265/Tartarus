package com.example.tartarus.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.ViewModel.BookmarksViewModel
import com.example.tartarus.data.Mark

@Composable
fun BookmarksScreen(
    onBookmarkClick: (Mark) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: BookmarksViewModel = viewModel()
) {
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    var showCreateDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var bookmarkToDelete by remember { mutableStateOf<Mark?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 80.dp, end = 16.dp, top = 32.dp, bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Метки",
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier.height(40.dp),
                shape = RectangleShape,
                border = BorderStroke(width = 1.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Создать закладку", fontFamily = FontFamily.Serif)
            }

            Spacer(Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(bookmarks) { bookmark ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = bookmark.name,
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF2A1A0F),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onBookmarkClick(bookmark) }
                                .padding(vertical = 8.dp)
                        )

                        IconButton(
                            onClick = { bookmarkToDelete = bookmark },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Удалить",
                                tint = Color(0xFF8B0000)
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Color(0xFF2A1A0F),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = {},
                onGameClick = onGameClick,
                onProfileClick = onProfileClick
            )
        }

        Image(
            painter = painterResource(R.drawable.sealing),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(250.dp)
        )
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Новая закладка") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Название") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.createBookmark(newName.trim())
                    newName = ""
                    showCreateDialog = false
                }) {
                    Text("Создать")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (bookmarkToDelete != null) {
        AlertDialog(
            onDismissRequest = { bookmarkToDelete = null },
            title = { Text("Удалить закладку?") },
            text = { Text("Вы уверены, что хотите удалить '${bookmarkToDelete?.name}'? Все существа внутри также удалятся.") },
            confirmButton = {
                TextButton(onClick = {
                    bookmarkToDelete?.id?.let { viewModel.deleteBookmark(it) }
                    bookmarkToDelete = null
                }) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { bookmarkToDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}