package org.wickedtools.safenose

import android.app.Application
import org.wickedtools.safenose.di_main.initKoin

class SafenoseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
