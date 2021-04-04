package org.devio.hi.config.core

interface ConfigListener {
    fun onConfigUpdated(configMap: Map<String, Any>)
}