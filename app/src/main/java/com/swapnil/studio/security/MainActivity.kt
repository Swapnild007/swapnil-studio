package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.graphics.Color
import java.util.concurrent.Executors

class MainActivity:Activity(){private val io=Executors.newSingleThreadExecutor();private lateinit var status:TextView
 override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(ui())}
 private fun ui():LinearLayout{val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(22,28,22,20);setBackgroundColor(Color.rgb(11,15,20))};fun t(s:String,z:Float)=TextView(this).apply{text=s;textSize=z;setTextColor(Color.WHITE);setPadding(0,6,0,6)};root.addView(t("SWAPNIL SECURITY LAB",24f));root.addView(t("Rootless Android security laboratory",14f));status=t("Rootless mode: ON",14f);root.addView(status)
 root.addView(Button(this).apply{text="INSTALL / VERIFY ROOTLESS ENGINE";setOnClickListener{install()}});root.addView(Button(this).apply{text="START DEBIAN VM";setOnClickListener{startVm()}});root.addView(Button(this).apply{text="NETWORK DISCOVERY";setOnClickListener{startActivity(android.content.Intent(this@MainActivity,ScannerActivity::class.java))}});root.addView(Button(this).apply{text="TERMINAL";setOnClickListener{startActivity(android.content.Intent(this@MainActivity,TerminalActivity::class.java))}});root.addView(Button(this).apply{text="VM SETTINGS";setOnClickListener{Toast.makeText(context,"ARM64 QEMU • 2 GB default memory • user-mode networking",Toast.LENGTH_LONG).show()}});root.addView(t("\nAuthorized testing only. This app is designed for your own lab and permitted security assessments.",12f));return root}
 private fun install(){status.text="Downloading and verifying components…";io.execute{val ok=RootlessInstaller.install(this){n,d,t->runOnUiThread{status.text="$n  ${d/1024/1024}/${t/1024/1024} MB"}};runOnUiThread{status.text=if(ok)"Rootless engine verified and ready." else "Installation failed."}}}
 private fun startVm(){io.execute{try{RootlessVm(this).start();runOnUiThread{status.text="Debian VM started."}}catch(e:Exception){runOnUiThread{status.text="VM not ready: ${e.message}"}}}}
}
