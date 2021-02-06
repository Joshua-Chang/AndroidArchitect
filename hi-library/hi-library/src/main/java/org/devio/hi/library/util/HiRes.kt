package org.devio.hi.library.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object HiRes {
    private fun context():Context{
        return AppGlobals.get() as Context
    }
    fun getString(@StringRes id:Int):String= context().getString(id)
    fun getString(@StringRes id: Int,vararg formatArgs:Any?):String= context().getString(id,*formatArgs)
    fun getColor(@ColorRes id: Int): Int=ContextCompat.getColor(context(), id)
    fun getColorStateList(@ColorRes id: Int): ColorStateList? = ContextCompat.getColorStateList(context(), id)
    fun getDrawable(@DrawableRes drawableId: Int):Drawable?=ContextCompat.getDrawable(context(),drawableId)
}