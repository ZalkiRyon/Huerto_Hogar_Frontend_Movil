package com.example.huerto_hogar.screen


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.huerto_hogar.model.LoginResult
import com.example.huerto_hogar.ui.theme.components.InputField
import com.example.huerto_hogar.viewmodel.LoginViewModel
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect
import com.example.huerto_hogar.ui.theme.components.animations.loadingPulseEffect
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffect

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel){
    val formState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(formState.loginResultEvent) {
        val resultEvent = formState.loginResultEvent
        if (resultEvent != null) {
            when (resultEvent) {
                LoginResult.SUCCESS -> {
                    Toast.makeText(
                        context,
                        "¡Bienvenido, ${formState.loggedInUser?.name}!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("home_screen")
                }
                LoginResult.INVALID_CREDENTIALS -> {
                    Toast.makeText(
                        context,
                        "Error: Credenciales incorrectos.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                LoginResult.ERROR -> {
                    Toast.makeText(
                        context,
                        "Error desconocido al iniciar sesión.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            viewModel.clearLoginResultEvent()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .bounceInEffect(delay = 0)
            )

            InputField(
                value = formState.email,
                onValueChange = viewModel::onChangeEmail,
                label = "Correo electrónico",
                error = formState.errors.emailError,
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 100)
            )

            InputField(
                value = formState.password,
                onValueChange = viewModel::onChangePassword,
                label = "Contraseña",
                error = formState.errors.passwordError,
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 200),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = { viewModel.onClickLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 300)
                    .pressClickEffect()
                    .loadingPulseEffect(formState.isLoading),
                enabled = !formState.isLoading && formState.errors.emailError == null && formState.errors.passwordError == null
            ) {
                if (formState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.height(20.dp))
                } else {
                    Text(text = "ACCEDER", color = Color.White, fontSize = 18.sp)
                }
            }


            Button(
                onClick = { navController.navigate("registro_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 400)
                    .pressClickEffect(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text(text = "CREAR CUENTA", color = Color.Black)
            }
        }
    }


}
