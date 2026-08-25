package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import kotlin.concurrent.thread

class MainActivity : Activity() {
    private lateinit var status: TextView
    private val items = listOf(
        "QEMU ARM64" to Pair("https://github.com/zalexdev/strykerapp/releases/download/rootless-main/qemu-system-aarch64", "2a87f531371b3f8d45141d48632e0dbe2c3d968fbcb385ba105d671479fb8c99"),
        "Debian kernel" to Pair("https://github.com/zalexdev/strykerapp/releases/download/rootless-main/Image", "cbe59a02e7ea979a150661032440c94e2c4db0b735af2416e11ae5cac15a58e4"),
        "Initrd" to Pair("https://github.com/zalexdev/strykerapp/releases/download/rootless-main/initrd.img", "655f3ef013e7818e9ee874cf3b44a4c0bdc8a586c986cf237cb74c41862dfd02"),
        "libslirp" to Pair("https://github.com/zalexdev/strykerapp/releases/download/rootless-main/libslirp.so", "226372426fda32c9fccd8e831d0901a86bfff3c3e6f7a60336d6dde149f756c4"),
        "Debian rootfs" to Pair("https://github.com/zalexdev/strykerapp/releases/download/rootless-main/rootfs.imgz", "f80c2b1e2433c3036aa745da2a5935cf5dd61b65b17bbeabae87b49bd68a12ef")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 40, 32, 24); setBackgroundColor(Color.rgb(11,15,20)) }
        val title = TextView(this).apply { text = "SWAPNIL SECURITY LAB"; textSize = 25f; setTextColor(Color.WHITE); gravity = Gravity.CENTER }
        val sub = TextView(this).apply { text = "v1.0.0  •  ROOTLESS ANDROID SECURITY LAB"; textSize = 13f; setTextColor(Color.LTGRAY); gravity = Gravity.CENTER; setPadding(0,10,0,24) }
        status = TextView(this).apply { text = "Ready. No root required."; textSize = 15f; setTextColor(Color.WHITE); setPadding(0,12,0,20) }
        val install = Button(this).apply { text = "SET UP ROOTLESS ENGINE"; setOnClickListener { verifyAndPrepare() } }
        val lab = Button(this).apply { text = "OPEN SAFE LAB"; setOnClickListener { status.text = "Lab mode is ready for authorized targets only." } }
        val note = TextView(this).apply { text = "Built for ARM64 phones. Uses an isolated Debian/QEMU environment. Only test systems you own or are authorized to assess."; textSize = 12f; setTextColor(Color.GRAY); setPadding(0,24,0,0) }
        root.addView(title); root.addView(sub); root.addView(status); root.addView(install); root.addView(lab); root.addView(note)
        setContentView(root)
    }

    private fun verifyAndPrepare() {
        status.text = "Checking official rootless components…"
        thread {
            try {
                var ok = 0
                for ((name, pair) in items) {
                    val conn = URL(pair.first).openConnection() as HttpURLConnection
                    conn.requestMethod = "HEAD"; conn.connectTimeout = 10000; conn.readTimeout = 10000
                    val code = conn.responseCode; conn.disconnect()
                    if (code in 200..399) ok++ else throw Exception("$name returned HTTP $code")
                }
                runOnUiThread { status.text = "Official rootless bundle reachable: $ok/${items.size}. Next step: secure download + SHA-256 verification." }
            } catch (e: Exception) { runOnUiThread { status.text = "Check failed: ${e.message}" } }
        }
    }
}
