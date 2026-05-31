package com.example.user.auth.data.datasource

import com.example.user.auth.data.models.UserModel
import com.example.user.auth.data.models.UserPayloadModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class UserDataSourceImpl(private val supabaseClient: SupabaseClient) : UserDataSource {

    override suspend fun registerUser(payload: UserPayloadModel): UserModel {
        val user = supabaseClient.auth.signUpWith(Email) {
            email = payload.email
            password = payload.password
        }
        
        return UserModel(
            email = user?.email ?: payload.email, 
            token = supabaseClient.auth.currentAccessTokenOrNull()
        )
    }

    override suspend fun loginUser(payload: UserPayloadModel): UserModel {
        supabaseClient.auth.signInWith(Email) {
            email = payload.email
            password = payload.password
        }

        val user = supabaseClient.auth.currentUserOrNull()
        
        return UserModel(
            email = user?.email ?: payload.email,
            token = supabaseClient.auth.currentAccessTokenOrNull()
        )
    }

    override suspend fun logout() {
        supabaseClient.auth.signOut()
    }
}
