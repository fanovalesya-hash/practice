package ci.nsu.mobile.main

import android.app.Application

class DepositApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: DepositApplication
            private set
    }
}