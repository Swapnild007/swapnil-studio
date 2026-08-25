package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.graphics.Color
import java.util.concurrent.Executors

class MainActivity : Activity() {
 private val io=Executors.newSingleThreadExecutor()
 private lateinit var status:TextView
 override fun onCreate(state:Bundle?) { super.onCreate(state); setContentView(ui()) }
 private fun ui():LinearLayout{
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,30,24,24);setBackgroundColor(Color.rgb(11,15,20))}
  fun t(s:String,z:Float)=TextView(this).apply{text=s;textSize=z;setTextColor(Color.WHITE);setPadding(0,7,0,7)}
  root.addView(t("SWAPNIL SECURITY LAB",24f));root.addView(t("Rootless Android security laboratory",14f));
  status=t("Checking ARM64…",14f);root.addView(status)
  val install=Button(this).apply{text="INSTALL ROOTLESS ENGINE";setOnClickListener{install()}};root.addView(install)
  root.addView(Button(this).apply{text="START DEBIAN VM";setOnClickListener{startVm()}})
  listOf("NETWORK DISCOVERY","PORT SCANNER","WEB SECURITY","TERMINAL","DESKTOP / VNC","VM SETTINGS").forEach{name->root.addView(Button(this).apply{text=name;setOnClickListener{Toast.makeText(context,"Module: $name",Toast.LENGTH_SHORT).show()}})}
  val arm=android.os.Build.SUPPORTED_ABIS.contains("arm64-v8a");status.text="Rootless: ON\nAndroid root: NOT REQUIRED\nArchitecture: ${if(arm)"ARM64 ready" else "ARM64 unavailable"}"
  return root
 }
 private fun install(){status.text="Downloading and verifying rootless components…";io.execute{val ok=RootlessInstaller.install(this){n,d,t->runOnUiThread{status.text="$n  ${d/1024/1024} / ${t/1024/1024} MB"}};runOnUiThread{status.text=if(ok)"Rootless engine installed and verified." else "Installation failed. Check network/storage."}}}
 private fun startVm(){io.execute{try{RootlessVm(this).start();runOnUiThread{status.text="Debian VM started."}}catch(e:Exception){runOnUiThread{status.text="VM not ready: ${e.message}"}}}}
}
