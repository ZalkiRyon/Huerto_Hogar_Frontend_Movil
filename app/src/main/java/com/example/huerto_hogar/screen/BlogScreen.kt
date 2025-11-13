package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.manager.BlogManagerViewModel
import com.example.huerto_hogar.model.Blog
import com.example.huerto_hogar.ui.theme.components.NewsCard
import com.example.huerto_hogar.ui.theme.components.NewsModal

@Composable
fun BlogScreen(navController: NavController) {

    var selectedBlog by remember { mutableStateOf<Blog?>(null) }
    var isModalOpen by remember { mutableStateOf(false) }

    val viewModel: BlogManagerViewModel = viewModel()
    val blogs = viewModel.getBlogs()

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
        )

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