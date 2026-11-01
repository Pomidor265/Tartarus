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
import com.example.tartarus.ViewModel.DifficultyViewModel
import com.example.tartarus.data.Difficulty

@Composable
fun DifficultyScreen(
    onDifficultySelected: (Difficulty) -> Unit,
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: DifficultyViewModel = viewModel()
) {
    val difficulties by viewModel.difficulties.collectAsStateWithLifecycle()



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
                text = "Выберите сложность",
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F)
            )

            Spacer(Modifier.height(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(difficulties) { diff ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDifficultySelected(diff) }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${diff.name} (${diff.questionsCount} вопр.)",
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF2A1A0F),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(6.dp))
                        HorizontalDivider(
                            color = Color(0xFF2A1A0F),
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = {},
                onProfileClick = onProfileClick
            )
        }
    }
}