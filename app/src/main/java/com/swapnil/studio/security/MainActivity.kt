package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import java.util.concurrent.Executors

class MainActivity:Activity(){private val io=Executors.newSingleThreadExecutor();private lateinit var status:TextView
 override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(ui())}
 private fun ui():LinearLayout{val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(22,28,22,20);setBackgroundColor(Color.rgb(11,15,20))};fun t(s:String,z:Float)=TextView(this).apply{text=s;textSize=z;setTextColor(Color.WHITE);setPadding(0,6,0,6)}
 val title=t("SWAPNIL SECURITY LAB",24f);root.addView(title);root.addView(t("ROOTLESS SECURITY PLATFORM • v1.4.0",12f));status=t("ARM64 readiness: ${if(android.os.Build.SUPPORTED_ABIS.contains("arm64-v8a"))"READY" else "UNSUPPORTED"}",14f);root.addView(status)
 fun button(label:String,action:()->Unit)=Button(this).apply{text=label;gravity=Gravity.CENTER;setOnClickListener{action()}};root.addView(button("INSTALL / VERIFY ROOTLESS ENGINE"){install()});root.addView(button("START DEBIAN ARM64 VM"){startVm()});root.addView(button("NETWORK DISCOVERY"){startActivity(Intent(this,ScannerActivity::class.java))});root.addView(button("TERMINAL"){startActivity(Intent(this,TerminalActivity::class.java))});root.addView(button("VM SETTINGS"){showInfo()});root.addView(t("\nModules are intended for systems and networks you own or are authorized to assess.\n\nOpen-source attribution is retained in NOTICE.",12f));return root}
 private fun install(){status.text="Downloading, verifying and unpacking rootless engine…";io.execute{val ok=RootlessInstaller.install(this){n,d,t->runOnUiThread{status.text="$n • ${d/1024/1024}/${t/1024/1024} MB"}};runOnUiThread{status.text=if(ok)"ROOTLESS ENGINE READY • checksums verified" else "INSTALL FAILED • check storage/network"}}}
 private fun startVm(){io.execute{try{RootlessVm(this).start();runOnUiThread{status.text="DEBIAN VM STARTED • rootless"}}catch(e:Exception){runOnUiThread{status.text="VM ERROR • ${e.message}"}}}}
 private fun showInfo(){AlertDialog.Builder(this).setTitle("Rootless VM").setMessage("ARM64 QEMU\n2 GB default memory\nUser-mode networking\nAndroid root is not required\n\nBuild 1–4 consolidated in v1.4.0").setPositiveButton("OK",null).show()}
}
