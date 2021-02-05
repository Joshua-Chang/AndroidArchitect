package org.devio.design.pattern.singleton

class Singleton3Kotlin private constructor() {
    companion object {
        val instance: Singleton3Kotlin by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Singleton3Kotlin()
        }
    }
}