package com.example.huerto_hogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.huerto_hogar.data.AppDatabase
import com.example.huerto_hogar.repository.RegisterUserViewModel
import com.example.huerto_hogar.repository.UserRepository
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.AppNavigationContainer


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBase =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "datBaseHH").build()

        val repository = UserRepository(dataBase.userDao())

        val viewModelFactory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RegisterUserViewModel::class.java)) {

                    return RegisterUserViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        enableEdgeToEdge()
        setContent {
            Huerto_HogarTheme {
                // Refactor que solo se llame al componente que contendrá toda la UI
                AppNavigationContainer(viewModelFactory)
            }
        }
    }
}
