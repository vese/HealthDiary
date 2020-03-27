package com.example.healthdiary.util

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

abstract class AbstractListAdapter<T>(
    context: Context,
    private val resource: Int,
    objects: List<T>
) :
    ArrayAdapter<T>(context, resource, objects) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView != null) {
            convertView
        } else {
            Log.d(javaClass.toString(), "getView")
            val v =
                LayoutInflater
                    .from(this.context)
                    .inflate(resource, parent, false)
            createViewFromResource(v, position)
        }
    }

    abstract fun createViewFromResource(v: View, position: Int): View
}