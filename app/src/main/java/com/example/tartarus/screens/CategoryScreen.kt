package com.example.tartarus.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.data.Category
import com.example.tartarus.data.CategoryRep
import kotlinx.coroutines.CancellationException

@Composable
fun CategoryScreen(
    onCategoryClick: (Category) -> Unit = {},
    onGoToRegister: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {}
) {
    val repo = remember { CategoryRep() }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            categories = repo.getCategories()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CategoryScreen", "Ошибка загрузки", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier.fillMaxSize()) {

            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Категории",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF2A1A0F)
                    )

                    Spacer(Modifier.height(8.dp))
                    Spacer(Modifier.height(24.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            CategoryItem(category) { onCategoryClick(category) }
                        }
                    }
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
        }
    }
}

@Composable
private fun CategoryItem(category: Category, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = category.name,
            fontSize = 22.sp,
            fontFamily = FontFamily.Serif,
            color = Color(0xFF2A1A0F)
        )
        Spacer(Modifier.height(6.dp))
        Divider(
            color = Color(0xFF2A1A0F),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}