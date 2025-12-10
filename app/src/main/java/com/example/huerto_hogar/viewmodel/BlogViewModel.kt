package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.Blog
import com.example.huerto_hogar.repository.BlogRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles para las operaciones de blogs
 */
sealed class BlogUiState {
    object Idle : BlogUiState()
    object Loading : BlogUiState()
    data class Success(val blogs: List<Blog>) : BlogUiState()
    data class Error(val message: String) : BlogUiState()
}

/**
 * ViewModel refactorizado para gestionar blogs
 * 
 * Usa BlogRepository para obtener datos de la API
 * Expone estados de UI reactivos mediante StateFlow
 */
class BlogManagerViewModel(
    private val repository: BlogRepository = BlogRepository()
) : ViewModel() {
    
    // Estado de la lista de blogs
    private val _blogsState = MutableStateFlow<BlogUiState>(BlogUiState.Idle)
    val blogsState: StateFlow<BlogUiState> = _blogsState.asStateFlow()
    
    // Lista de blogs actualmente cargados
    private val _blogs = MutableStateFlow<List<Blog>>(emptyList())
    val blogs: StateFlow<List<Blog>> = _blogs.asStateFlow()
    
    /**
     * Obtiene todos los artículos de blog desde la API
     */
    fun getBlogs() {
        viewModelScope.launch {
            repository.getAllBlogs().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _blogsState.value = BlogUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { blogList ->
                            _blogs.value = blogList
                            _blogsState.value = BlogUiState.Success(blogList)
                        }
                    }
                    is Resource.Error -> {
                        _blogsState.value = BlogUiState.Error(
                            resource.message ?: "Error al cargar blogs"
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Busca blogs por etiquetas
     * 
     * @param tags Lista de etiquetas separadas por comas
     */
    fun searchBlogsByTags(tags: String) {
        viewModelScope.launch {
            repository.getBlogsByTags(tags).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _blogsState.value = BlogUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { blogList ->
                            _blogs.value = blogList
                            _blogsState.value = BlogUiState.Success(blogList)
                        }
                    }
                    is Resource.Error -> {
                        _blogsState.value = BlogUiState.Error(
                            resource.message ?: "Error al buscar blogs"
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Busca blogs por término de búsqueda
     * 
     * @param query Término de búsqueda
     */
    fun searchBlogs(query: String) {
        if (query.isBlank()) {
            getBlogs()
            return
        }
        
        viewModelScope.launch {
            repository.searchBlogs(query).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _blogsState.value = BlogUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { blogList ->
                            _blogs.value = blogList
                            _blogsState.value = BlogUiState.Success(blogList)
                        }
                    }
                    is Resource.Error -> {
                        _blogsState.value = BlogUiState.Error(
                            resource.message ?: "Error en la búsqueda"
                        )
                    }
                }
            }
        }
    }
}