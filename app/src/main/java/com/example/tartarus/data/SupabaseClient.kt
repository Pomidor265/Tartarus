package com.example.tartarus.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.HttpTimeout

@OptIn(SupabaseInternal::class)
object SupabaseModule {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://ehyafjpaehaudphgrklp.supabase.co",
        supabaseKey = "sb_publishable_1yM35ouo1ZmtOdSyF5_jOQ_oRsluG2g"
    ) {
        install(Postgrest)
        install(Storage)
        install(Auth)
        httpConfig {
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                socketTimeoutMillis  = 60_000
            }
        }
    }
}