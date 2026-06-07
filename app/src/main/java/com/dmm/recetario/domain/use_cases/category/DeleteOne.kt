package com.dmm.recetario.domain.use_cases.category

import android.util.Log
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.exceptions.APIException
import com.dmm.recetario.domain.repository.CategoryRepository
import jakarta.inject.Inject

class DeleteCategoryUseCase @Inject constructor (
    private val repository: CategoryRepository,
    private val dao: CategoryDao
) {
    suspend operator fun invoke(id: String): Boolean {
        try {
            repository.deleteCategory(id)
        } catch (e: APIException) {
            Log.w("DeleteCategoryUseCase", "${e.message}")
            return false
        }

        dao.deleteCategory(id)

        return true
    }
}