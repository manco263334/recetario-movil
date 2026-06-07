package com.dmm.recetario.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmm.recetario.domain.entity.CategoryEntity

@Entity(tableName = "categories")
data class CategoryEntityImpl (
    @PrimaryKey
    override val id: String,
    override val name: String,
    override val icon: String?
) : CategoryEntity(id, name, icon)