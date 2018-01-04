package io.github.anotherjack.onresultmanagerdemo.application

import android.app.Application
import io.github.anotherjack.onresultmanager.HookUtil

/**
 * Created by jack on 2018/1/4.
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        HookUtil.hookActivityThreadHandler()
    }
}