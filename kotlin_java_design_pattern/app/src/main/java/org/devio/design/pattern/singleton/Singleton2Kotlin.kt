package org.devio.design.pattern.singleton

class Singleton2Kotlin private constructor(){
    companion object{
        private var instance:Singleton2Kotlin?=null
        get() {
            if (field == null) {
                field= Singleton2Kotlin()
            }
            return field
        }
        @Synchronized
        fun get():Singleton2Kotlin= instance!!
    }
}