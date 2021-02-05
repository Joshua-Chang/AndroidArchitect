package org.devio.design.pattern.singleton

class Singleton4Kotlin {
    companion object{
        val instance=SingletonProvider.holder
    }
    private object SingletonProvider{
        val holder=Singleton4Kotlin
    }
}