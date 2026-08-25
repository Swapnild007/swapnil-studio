package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.graphics.Color

class MainActivity : Activity() {
 override fun onCreate(state: Bundle?) { super.onCreate(state); setContentView(buildUi()) }
 private fun buildUi(): LinearLayout {
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(28,32,28,24);setBackgroundColor(Color.rgb(11,15,20))}
  fun tv(t:String,s:Float)=TextView(this).apply{ text=t; textSize=s; setTextColor(Color.WHITE); setPadding(0,8,0,8) }
  root.addView(tv("SWAPNIL SECURITY LAB",24f)); root.addView(tv("Rootless Android security laboratory",15f))
  val status=tv("Checking ARM64 / rootless readiness…",14f); root.addView(status)
  val cards=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL}
  listOf("Rootless QEMU","Debian ARM64","Network tools","Terminal","Desktop / VNC").forEach { name -> cards.addView(Button(this).apply{text=name;setOnClickListener{Toast.makeText(context,"$name module is being prepared",Toast.LENGTH_SHORT).show()}}) }
  root.addView(cards)
  root.addView(Button(this).apply{text="VM SETTINGS";setOnClickListener{Toast.makeText(context,"Rootless VM settings",Toast.LENGTH_SHORT).show()}})
  status.text="Rootless mode: Android root is NOT required\nArchitecture: ${if (android.os.Build.SUPPORTED_ABIS.any{it==\"arm64-v8a\"}) \"ARM64 ready\" else \"ARM64 unavailable\"}"
  return root
 }
}
