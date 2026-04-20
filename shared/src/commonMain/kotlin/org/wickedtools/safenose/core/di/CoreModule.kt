package org.wickedtools.safenose.core.di


import io.github.jan.supabase.SupabaseClient
import org.koin.dsl.module
import org.wickedtools.safenose.core.data.remote.supabase

val coreModule = module {
    single <SupabaseClient> {supabase}
}