package com.example.user.auth.data.datasource

import com.example.user.auth.data.models.KendaraanModel
import com.example.user.auth.domain.entities.OnboardingPayload
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface OnboardingDataSource {
    suspend fun checkProfileComplete(userId: String): Boolean
    suspend fun checkHasKendaraan(userId: String): Boolean
    suspend fun updateUserProfile(userId: String, username: String, phone: String)
    suspend fun insertKendaraan(payload: OnboardingPayload, userId: String)
}

class OnboardingDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : OnboardingDataSource {

    override suspend fun checkProfileComplete(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val row = supabaseClient.postgrest["user"]
                .select(columns = Columns.list("username, phone")) {
                    filter { eq("id", userId) }
                }.decodeSingleOrNull<PublicUserCheck>()
            row != null && !row.username.isNullOrBlank() && !row.phone.isNullOrBlank()
        }
    }

    override suspend fun checkHasKendaraan(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val list = supabaseClient.postgrest["kendaraan"]
                .select(columns = Columns.list("id")) {
                    filter { eq("user_id", userId) }
                }.decodeList<KendaraanCheck>()
            list.isNotEmpty()
        }
    }

    override suspend fun updateUserProfile(userId: String, username: String, phone: String) {
        withContext(Dispatchers.IO) {
            supabaseClient.postgrest["user"].update(
                {
                    set("username", username)
                    set("phone", phone)
                }
            ) {
                filter { eq("id", userId) }
            }
        }
    }

    override suspend fun insertKendaraan(payload: OnboardingPayload, userId: String) {
        withContext(Dispatchers.IO) {
            val kendaraan = KendaraanModel(
                jenis = payload.jenisKendaraan,
                merk = payload.merkKendaraan,
                tipe = payload.tipeKendaraan,
                tahun = payload.tahunKendaraan,
                totalKm = payload.totalKm,
                userId = userId
            )
            supabaseClient.postgrest["kendaraan"].insert(kendaraan)
        }
    }

    @Serializable
    private data class PublicUserCheck(
        val username: String? = null,
        val phone: String? = null
    )

    @Serializable
    private data class KendaraanCheck(
        @SerialName("id") val id: Long
    )
}
