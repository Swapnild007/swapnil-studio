package com.swapnil.studio.security

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/** Rootless component installer. Downloads only pinned ARM64 components and verifies SHA-256. */
object RootlessInstaller {
 data class Asset(val name:String,val url:String,val sha256:String,val size:Long)
 private val assets=listOf(
  Asset("QEMU","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/qemu-system-aarch64","2a87f531371b3f8d45141d48632e0dbe2c3d968fbcb385ba105d671479fb8c99",43800304),
  Asset("Kernel","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/Image","cbe59a02e7ea979a150661032440c94e2c4db0b735af2416e11ae5cac15a58e4",37605312),
  Asset("Initrd","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/initrd.img","655f3ef013e7818e9ee874cf3b44a4c0bdc8a586c986cf237cb74c41862dfd02",38301815),
  Asset("libslirp","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/libslirp.so","226372426fda32c9fccd8e831d0901a86bfff3c3e6f7a60336d6dde149f756c4",1145496),
  Asset("Debian rootfs","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/rootfs.imgz","f80c2b1e2433c3036aa745da2a5935cf5dd61b65b17bbeabae87b49bd68a12ef",427974567)
 )
 fun install(context:Context, progress:(String,Long,Long)->Unit):Boolean {
  if(!android.os.Build.SUPPORTED_ABIS.contains("arm64-v8a")) return false
  val dir=File(context.filesDir,"rootless").apply{mkdirs()}
  return try { assets.all { a -> download(a,File(dir,a.name.replace(' ','_')),progress) } } catch(_:Exception){false}
 }
 private fun download(a:Asset,dest:File,p:(String,Long,Long)->Unit):Boolean {
  if(dest.exists() && dest.length()==a.size && sha(dest)==a.sha256) return true
  val c=(URL(a.url).openConnection() as HttpURLConnection).apply{connectTimeout=30000;readTimeout=120000;instanceFollowRedirects=true}
  c.connect(); if(c.responseCode !in 200..299) return false
  c.inputStream.use{input->FileOutputStream(dest).use{out->val b=ByteArray(1024*256);var done=0L;while(true){val n=input.read(b);if(n<0)break;out.write(b,0,n);done+=n;p(a.name,done,a.size)}}}
  return dest.length()==a.size && sha(dest)==a.sha256
 }
 private fun sha(f:File):String{val md=MessageDigest.getInstance("SHA-256");f.inputStream().use{ i->val b=ByteArray(1024*128);while(true){val n=i.read(b);if(n<0)break;md.update(b,0,n)}};return md.digest().joinToString(""){ "%02x".format(it)} }
}
