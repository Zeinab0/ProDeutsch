package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class BannerModel(
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val order: Int = 0,
    val targetUrl: String = ""
)

class BannerViewModel : ViewModel() {

    private val _banners = MutableStateFlow<List<BannerModel>>(emptyList())
    val banners: StateFlow<List<BannerModel>> = _banners

    fun loadBanners() {
        viewModelScope.launch {
            try {
                val snapshot = Firebase.firestore
                    .collection("banners")
                    .whereEqualTo("isActive", true)
                    .orderBy("order")
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(BannerModel::class.java)
                }

                _banners.value = list
                Log.d("BannerViewModel", "✅ Loaded banners: ${list.size}")
            } catch (e: Exception) {
                Log.e("BannerViewModel", "❌ Error loading banners: ${e.message}")
            }
        }
    }
}