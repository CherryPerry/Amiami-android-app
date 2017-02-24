package ru.cherryperry.amiami.util

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.reflect.KProperty

fun <T : View> Fragment.findView(id: Int): T = this.view?.findViewById(id) as T

fun <T : View> Activity.findView(id: Int): T = this.findViewById(id) as T

fun <T : View> View.findView(id: Int): T = this.findViewById(id) as T

class ViewDelegate<out T : View>(viewId: Int) {
    private var value: T? = null
    private val id = viewId

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null)
            when (thisRef) {
                is Activity -> value = thisRef.findViewById(id) as T
                is Fragment -> value = thisRef.view!!.findViewById(id) as T
                is View -> value = thisRef.findViewById(id) as T
                is RecyclerView.ViewHolder -> value = thisRef.itemView.findViewById(id) as T
                else -> throw IllegalStateException("Other thisRefs not implemented")
            }
        return value!!
    }
}

class NullableViewDelegate<out T : View?>(viewId: Int) {
    private var value: T? = null
    private val id = viewId

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (value == null)
            when (thisRef) {
                is Activity -> value = thisRef.findViewById(id) as T?
                is Fragment -> value = thisRef.view!!.findViewById(id) as T?
                is View -> value = thisRef.findViewById(id) as T?
                is RecyclerView.ViewHolder -> value = thisRef.itemView.findViewById(id) as T?
                else -> throw IllegalStateException("Other thisRefs not implemented")
            }
        return value
    }
}