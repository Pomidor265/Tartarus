package com.example.tartarus.screens

import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tartarus.R
import com.example.tartarus.ViewModel.AuthViewModel
import com.example.tartarus.data.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(60.dp))

            Text(
                text = "Авторизация",
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F)
            )

            Spacer(Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.sealing),
                contentDescription = null,
                modifier = Modifier.size(220.dp)
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("почта") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (error != null) {
                Text(
                    text = error!!,
                    color = Color(0xFF8B0000),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    viewModel.login(email, password, onLoginSuccess)
                },
                enabled = !isLoading && email.isNotBlank() && password.length >= 6,
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp),
                shape = RectangleShape,
                border = BorderStroke(width = 1.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),

            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Войти", fontFamily = FontFamily.Serif)
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Нет аккаунта? ",
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    color = Color(0xFF2A1A0F)
                )
                Text(
                    text = "Зарегистрироваться",
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
}