package com.swapnil.studio.security

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private val io = Executors.newSingleThreadExecutor()
    private lateinit var status: TextView

    private val bg = Color.rgb(8, 12, 17)
    private val card = Color.rgb(18, 24, 32)
    private val card2 = Color.rgb(22, 29, 38)
    private val accent = Color.rgb(45, 220, 125)
    private val muted = Color.rgb(150, 163, 177)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        setContentView(buildUi())
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int, radius: Int = 18, stroke: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius).toFloat()
            if (stroke != null) setStroke(dp(1), stroke)
        }

    private fun label(value: String, size: Float, color: Int = Color.WHITE, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.create("sans-serif", Typeface.BOLD)
        }

    private fun buildUi(): View {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(28))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val brand = label("SWAPNIL\nSECURITY LAB", 25f, Color.WHITE, true)
        header.addView(brand, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        val version = label("v1.4.1", 12f, accent, true).apply {
            setPadding(dp(10), dp(7), dp(10), dp(7))
            background = rounded(card2, 12, accent)
        }
        header.addView(version)
        root.addView(header)

        root.addView(label("ROOTLESS ANDROID SECURITY LAB", 12f, muted).apply {
            setPadding(0, dp(5), 0, dp(18))
        })

        val supported = android.os.Build.SUPPORTED_ABIS.contains("arm64-v8a")
        val statusCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = rounded(card, 18, if (supported) Color.rgb(35, 105, 70) else Color.rgb(120, 70, 70))
        }
        statusCard.addView(label("ENVIRONMENT", 11f, muted, true))
        status = label(if (supported) "●  ARM64 READY" else "●  ARM64 UNSUPPORTED", 16f, if (supported) accent else Color.rgb(255, 110, 110), true)
        statusCard.addView(status)
        statusCard.addView(label("Android root is not required", 12f, muted))
        root.addView(statusCard)

        root.addView(label("TOOLS", 12f, muted, true).apply {
            setPadding(dp(2), dp(22), 0, dp(9))
        })

        val install = actionButton("INSTALL / VERIFY", "Rootless engine", accent) { install() }
        val vm = actionButton("START VM", "Debian ARM64", Color.rgb(95, 165, 255)) { startVm() }
        val network = actionButton("NETWORK", "Discovery tools", Color.rgb(180, 120, 255)) {
            startActivity(Intent(this, ScannerActivity::class.java))
        }
        val terminal = actionButton("TERMINAL", "Debian console", Color.rgb(255, 170, 80)) {
            startActivity(Intent(this, TerminalActivity::class.java))
        }
        val settings = actionButton("VM SETTINGS", "Memory & runtime", Color.rgb(100, 205, 185)) { showInfo() }

        val grid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        grid.addView(row(install, vm))
        grid.addView(row(network, terminal))
        grid.addView(single(settings))
        root.addView(grid)

        val note = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(15), dp(16), dp(15))
            background = rounded(card2, 16)
        }
        note.addView(label("SAFE LAB", 11f, accent, true))
        note.addView(label("Use security tools only on systems and networks you own or are explicitly authorized to test.", 12f, muted).apply {
            setPadding(0, dp(5), 0, 0)
        })
        root.addView(note, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(18) })

        root.addView(label("Open-source attribution retained in NOTICE.", 11f, Color.rgb(105, 116, 128)).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(18), 0, 0)
        })

        scroll.addView(root)
        return scroll
    }

    private fun row(a: View, b: View): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        addView(a, LinearLayout.LayoutParams(0, dp(112), 1f).apply { rightMargin = dp(5); bottomMargin = dp(10) })
        addView(b, LinearLayout.LayoutParams(0, dp(112), 1f).apply { leftMargin = dp(5); bottomMargin = dp(10) })
    }

    private fun single(v: View): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        addView(v, LinearLayout.LayoutParams(-1, dp(94)).apply { bottomMargin = dp(10) })
    }

    private fun actionButton(title: String, subtitle: String, color: Int, action: () -> Unit): View =
        LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(12), dp(12), dp(12))
            background = rounded(card, 18)
            isClickable = true
            isFocusable = true
            setOnClickListener { action() }
            addView(label(title, 14f, Color.WHITE, true))
            addView(label(subtitle, 11f, color))
        }

    private fun install() {
        status.text = "DOWNLOADING • verifying rootless engine"
        io.execute {
            val ok = RootlessInstaller.install(this) { name, downloaded, total ->
                runOnUiThread {
                    status.text = "$name  •  ${downloaded / 1024 / 1024}/${total / 1024 / 1024} MB"
                }
            }
            runOnUiThread {
                status.text = if (ok) "●  ROOTLESS ENGINE READY" else "●  INSTALL FAILED • check storage/network"
                status.setTextColor(if (ok) accent else Color.rgb(255, 110, 110))
            }
        }
    }

    private fun startVm() {
        io.execute {
            try {
                RootlessVm(this).start()
                runOnUiThread { status.text = "●  DEBIAN VM STARTED • rootless" }
            } catch (e: Exception) {
                runOnUiThread { status.text = "●  VM ERROR • ${e.message}" }
            }
        }
    }

    private fun showInfo() {
        AlertDialog.Builder(this)
            .setTitle("Rootless VM")
            .setMessage("ARM64 QEMU\n2 GB default memory\nUser-mode networking\nAndroid root is not required\n\nSwapnil Security Lab v1.4.1")
            .setPositiveButton("OK", null)
            .show()
    }
}
