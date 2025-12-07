package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.manager.BlogManagerViewModel
import com.example.huerto_hogar.manager.BlogUiState
import com.example.huerto_hogar.model.Blog
import com.example.huerto_hogar.ui.theme.components.NewsCard
import com.example.huerto_hogar.ui.theme.components.NewsModal
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect

@Composable
fun BlogScreen(
    navController: NavController,
    viewModel: BlogManagerViewModel = viewModel()
) {
    var selectedBlog by remember { mutableStateOf<Blog?>(null) }
    var isModalOpen by remember { mutableStateOf(false) }

    // Observar estados del ViewModel
    val blogsState by viewModel.blogsState.collectAsState()
    val blogs by viewModel.blogs.collectAsState()
    
    // Cargar blogs al iniciar
    LaunchedEffect(Unit) {
        viewModel.getBlogs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Noticias importantes",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .bounceInEffect(delay = 0)
        )

        // Manejo de estados
        when (blogsState) {
            is BlogUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is BlogUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (blogsState as BlogUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getBlogs() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            is BlogUiState.Success, BlogUiState.Idle -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(blogs) { blog ->
                        NewsCard(
                            article = blog,
                            onViewBlogClicked = { clickedBlog ->
                                selectedBlog = clickedBlog
                                isModalOpen = true
                            }
                        )
                    }
                }
            }
        }
    }

    selectedBlog?.let { blog ->
        NewsModal(
            article = blog,
            isOpen = isModalOpen,
            onClose = {
                isModalOpen = false
                selectedBlog = null
            }
        )
    }

}