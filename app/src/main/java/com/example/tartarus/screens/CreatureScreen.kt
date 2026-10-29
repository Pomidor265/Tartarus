package com.example.tartarus.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.data.Category
import com.example.tartarus.data.Creature
import com.example.tartarus.data.CreatureRep
import com.example.tartarus.data.SupabaseModule
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CancellationException

@Composable
fun CreaturesScreen(
    categoryId: Long,
    onCreatureClick: (Creature) -> Unit = {},
    onExtraClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {}
) {
    val repo = remember { CreatureRep() }

    var categoryName by remember { mutableStateOf("") }
    var creatures by remember { mutableStateOf<List<Creature>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categoryId) {
        Log.d("CreaturesScreen", "Загружаем данные для категории $categoryId")
        try {
            try {
                categoryName = SupabaseModule.client
                    .postgrest["Category"]
                    .select { filter { eq("Category_id", categoryId) } }
                    .decodeSingleOrNull<Category>()
                    ?.name ?: "Категория $categoryId"
                Log.d("CreaturesScreen", "Имя категории: $categoryName")
            } catch (e: Exception) {
                Log.e("CreaturesScreen", "Не удалось загрузить имя категории", e)
            }
            creatures = repo.getCreaturesByCategory(categoryId)
            Log.d("CreaturesScreen", "Загружено существ: ${creatures.size}")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CreaturesScreen", "Ошибка загрузки существ", e)
            error = e.message ?: "Неизвестная ошибка"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.fillMaxSize()) {

            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick,
                showExtra = true,
                extraIcon = R.drawable.back,
                extraLabel = "Назад",
                onExtraClick = onExtraClick
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = categoryName,
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF2A1A0F),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    when {
                        error != null -> Text(
                            text = "Ошибка: $error",
                            color = Color(0xFF8B0000),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        creatures.isEmpty() -> Text(
                            text = "Существа не найдены",
                            color = Color(0xFF2A1A0F),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        else -> LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(creatures) { creature ->
                                CreatureItem(creature) { onCreatureClick(creature) }
                            }
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.sealing),
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
private fun CreatureItem(creature: Creature, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = creature.name,
            fontSize = 22.sp,
            fontFamily = FontFamily.Serif,
            color = Color(0xFF2A1A0F),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Divider(
            color = Color(0xFF2A1A0F),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}