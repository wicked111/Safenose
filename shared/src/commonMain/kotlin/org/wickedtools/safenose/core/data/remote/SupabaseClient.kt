package org.wickedtools.safenose.core.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://fwvqkggcoyiwaxjeeptd.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ3dnFrZ2djb3lpd2F4amVlcHRkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQ0NDM5ODcsImV4cCI6MjA3MDAxOTk4N30.QVa3mUbuP_MNIPDVrqAHCv5PmAIWRQYlSYfjqTY3t_U"
) {
    install(Postgrest)
    install(Auth)
}