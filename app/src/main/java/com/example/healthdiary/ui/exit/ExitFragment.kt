package com.example.healthdiary.ui.exit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import kotlin.system.exitProcess

class ExitFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ exitProcess(0) }, 2000)
        return inflater.inflate(R.layout.fragment_exit, container, false)
    }
}