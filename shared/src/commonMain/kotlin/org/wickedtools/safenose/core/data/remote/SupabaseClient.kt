package org.wickedtools.safenose.core.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://uhrobxsuehjmjypudfrn.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVocm9ieHN1ZWhqbWp5cHVkZnJuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY3NTY2NzUsImV4cCI6MjA5MjMzMjY3NX0.D0tDEX8w1JlD2y8whvbcfoHtzHYvtJHQw2Jqbi0PiEI"
) {
    install(Postgrest)
    install(Auth)
}