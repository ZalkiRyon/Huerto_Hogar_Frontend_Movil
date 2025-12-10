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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.ui.theme.components.InputField
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffect
import com.example.huerto_hogar.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {

    val formState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(formState.loginSuccess, formState.loginErrors) {
        if (formState.loginSuccess) {
            val user = formState.loggedInUser

            Toast.makeText(
                context,
                "¡Bienvenido, ${user?.name ?: "Usuario"}!",
                Toast.LENGTH_SHORT
            ).show()


            val destination =
                if (user?.role == "admin") {
                    "admin_dashboard_screen"
                } else {
                    "home_screen"
                }

            navController.navigate(destination) {
                popUpTo(navController.graph.id) { inclusive = true }
            }

            viewModel.resetUiState()

        } else if (formState.error != null && !formState.isLoading) {
            Toast.makeText(
                context,
                "Error: ${formState.error}",
                Toast.LENGTH_LONG
            ).show()

            viewModel.clearError()
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
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .bounceInEffect(delay = 0)
            )

            InputField(
                value = formState.email,
                onValueChange = viewModel::onChangeEmail,
                label = "Correo electrónico",
                placeholder = "Solo @duocuc.cl o @profesor.duoc.cl",
                error = formState.loginErrors.emailError,
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 100)
            )

            InputField(
                value = formState.password,
                onValueChange = viewModel::onChangePassword,
                label = "Contraseña",
                error = formState.loginErrors.passwordError,
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 200),
                isPassword = true
            )

            formState.error?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = { viewModel.onClickLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 300)
                    .pressClickEffect()
                    .height(56.dp),
                enabled = !formState.isLoading &&
                        formState.loginErrors.emailError == null &&
                        formState.loginErrors.passwordError == null &&
                        formState.email.isNotBlank() && formState.password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (formState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(24.dp)
                    )
                } else {
                    Text(
                        text = "ACCEDER",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }


            Button(
                onClick = { navController.navigate("registro_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceInEffect(delay = 400)
                    .pressClickEffect(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "CREAR CUENTA",
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }

}


