package org.devio.`as`.proj.common.utils

import android.app.Application

object AppGlobals {
    var application: Application? = null
    /*反射android.app.ActivityThread 的currentApplication方法，可在任意地方得到APP*/
    fun get(): Application? {
        if (application == null) {
            try {
                application=Class.forName("android.app.ActivityThread").getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return application
    }
}