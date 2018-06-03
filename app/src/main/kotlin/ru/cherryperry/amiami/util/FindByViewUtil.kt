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
            value = when (thisRef) {
                is Activity -> thisRef.findViewById(id) as T
                is Fragment -> thisRef.view!!.findViewById(id) as T
                is View -> thisRef.findViewById(id) as T
                is RecyclerView.ViewHolder -> thisRef.itemView.findViewById(id) as T
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
            value = when (thisRef) {
                is Activity -> thisRef.findViewById<T>(id)
                is Fragment -> thisRef.view!!.findViewById<T>(id)
                is View -> thisRef.findViewById<T>(id)
                is RecyclerView.ViewHolder -> thisRef.itemView.findViewById<T>(id)
                else -> throw IllegalStateException("Other thisRefs not implemented")
            }
        return value
    }
}