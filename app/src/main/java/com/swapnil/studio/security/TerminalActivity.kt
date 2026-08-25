package com.swapnil.studio.security

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class TerminalActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 20, 16, 16)
            setBackgroundColor(Color.BLACK)
        }

        val out = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 13f
            text = "Swapnil Security Lab\nrootless terminal interface\n\n$ "
        }

        val input = EditText(this).apply {
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            hint = "command"
        }

        val run = Button(this).apply {
            text = "RUN"
            setOnClickListener {
                val command = input.text.toString()
                out.append(
                    "$command\nCommand execution is available inside the Debian VM after it is booted.\n$ "
                )
                input.text.clear()
            }
        }

        box.addView(out)
        box.addView(input)
        box.addView(run)
        setContentView(box)
    }
}
