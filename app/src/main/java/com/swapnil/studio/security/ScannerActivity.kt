package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.widget.*
import java.util.concurrent.Executors

class ScannerActivity:Activity(){
 override fun onCreate(b:Bundle?){super.onCreate(b);val box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,24,18,18)};val output=TextView(this);val button=Button(this).apply{text="SCAN LOCAL NETWORK";setOnClickListener{isEnabled=false;output.text="Scanning your local network…";Executors.newSingleThreadExecutor().execute{val r=NetworkScanner().scanLocalPrefix{m->runOnUiThread{output.append("\n$m")}};runOnUiThread{output.append("\nFinished. ${r.size} responsive hosts.");isEnabled=true}}}};box.addView(button);box.addView(output);setContentView(box)}
}
