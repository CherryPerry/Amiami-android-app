package ru.cherryperry.amiami.util

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.reflect.KProperty

fun <T : View> Fragment.findViewById(id: Int): T = this.view?.findViewById(id) as T

class ViewDelegateReset {

    private val list = ArrayList<ViewDelegate<*>>()

    internal fun register(viewDelegate: ViewDelegate<*>) {
        list.add(viewDelegate)
    }

    fun onDestroyView() {
        list.forEach { it.onDestroyView() }
    }
}

class ViewDelegate<out T : View?>(
    viewId: Int,
    viewDelegateReset: ViewDelegateReset? = null
) {
    private var value: T? = null
    private val id = viewId

    init {
        viewDelegateReset?.register(this)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        value?.let { return it }
        val newValue = when (thisRef) {
            is Activity -> thisRef.findViewById<T>(id)
            is Fragment -> thisRef.view!!.findViewById<T>(id)
            is View -> thisRef.findViewById<T>(id)
            is RecyclerView.ViewHolder -> thisRef.itemView.findViewById<T>(id)
            else -> throw IllegalStateException("Other thisRefs not implemented")
        }
        value = newValue
        return newValue
    }

    internal fun onDestroyView() {
        value = null
    }
}