package com.swapnil.studio.security

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.widget.*

class TerminalActivity:Activity(){
 override fun onCreate(b:Bundle?){super.onCreate(b);val box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(16,20,16,16);setBackgroundColor(Color.BLACK)};val out=TextView(this).apply{textColor=Color.WHITE;textSize=13f;text="Swapnil Security Lab\nrootless terminal interface\n\n$ "};val input=EditText(this).apply{setTextColor(Color.WHITE);hint="command";hintTextColor=Color.GRAY};val run=Button(this).apply{text="RUN";setOnClickListener{val cmd=input.text.toString();out.append("$cmd\nCommand execution is available inside the Debian VM after it is booted.\n$ ");input.text.clear()}};box.addView(out);box.addView(input);box.addView(run);setContentView(box)}
}
