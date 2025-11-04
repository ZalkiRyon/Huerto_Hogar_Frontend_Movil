package com.example.huerto_hogar.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Producto::Class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase(){

    abstract fun productoDao(): PersonaDao
}