package com.example.tartarus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.ViewModel.CreaturesViewModel
import com.example.tartarus.data.Creature

@Composable
fun CreaturesScreen(
    categoryId: Long,
    onCreatureClick: (Creature) -> Unit = {},
    onExtraClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    viewModel: CreaturesViewModel = viewModel()
) {
    val categoryName by viewModel.categoryName.collectAsStateWithLifecycle()
    val creatures by viewModel.creatures.collectAsStateWithLifecycle()

    LaunchedEffect(categoryId) {
        viewModel.loadData(categoryId)
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

                    if (creatures.isEmpty()) {
                        Text(
                            text = "Существа не найдены",
                            color = Color(0xFF2A1A0F),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        LazyColumn(
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
        HorizontalDivider(
            color = Color(0xFF2A1A0F),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}