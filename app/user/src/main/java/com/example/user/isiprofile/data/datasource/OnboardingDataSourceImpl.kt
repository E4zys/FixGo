package com.example.user.isiprofile.data.datasource

import com.example.user.isiprofile.domain.entities.OnboardingPayload
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class OnboardingDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : OnboardingDataSource {

    override suspend fun checkProfileComplete(userId: String): Boolean = withContext(Dispatchers.IO) {
        val row = supabaseClient.postgrest["user"]
            .select(columns = Columns.list("username, phone")) {
                filter { eq("id", userId) }
            }.decodeSingleOrNull<ProfileCheck>()
        row != null && !row.username.isNullOrBlank() && !row.phone.isNullOrBlank()
    }

    override suspend fun checkHasKendaraan(userId: String): Boolean = withContext(Dispatchers.IO) {
        val list = supabaseClient.postgrest["kendaraan"]
            .select(columns = Columns.list("id")) {
                filter { eq("user_id", userId) }
            }.decodeList<IdCheck>()
        list.isNotEmpty()
    }

    override suspend fun updateUserProfile(userId: String, username: String, phone: String) {
        withContext(Dispatchers.IO) {
            supabaseClient.postgrest["user"].update({
                set("username", username)
                set("phone", phone)
            }) {
                filter { eq("id", userId) }
            }
        }
    }

    override suspend fun insertKendaraan(payload: OnboardingPayload, userId: String) {
        withContext(Dispatchers.IO) {
            supabaseClient.postgrest["kendaraan"].insert(
                KendaraanInsert(
                    jenis = payload.jenisKendaraan,
                    merk = payload.merkKendaraan,
                    tipe = payload.tipeKendaraan,
                    tahun = payload.tahunKendaraan,
                    totalKm = payload.totalKm,
                    userId = userId
                )
            )
        }
    }

    // ── Serializable models private ke datasource ─────────────────────────────

    @Serializable
    private data class ProfileCheck(
        val username: String? = null,
        val phone: String? = null
    )

    @Serializable
    private data class IdCheck(@SerialName("id") val id: Long)

    @Serializable
    private data class KendaraanInsert(
        @SerialName("jenis") val jenis: String,
        @SerialName("merk") val merk: String,
        @SerialName("tipe") val tipe: String,
        @SerialName("tahun") val tahun: Int,
        @SerialName("total_km") val totalKm: Int,
        @SerialName("user_id") val userId: String
    )
}
