package com.swapnil.studio.security

import android.content.Context
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.zip.GZIPInputStream

object RootlessInstaller {
 data class Asset(val name:String,val url:String,val sha256:String,val size:Long)
 private val qemu=Asset("QEMU","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/qemu-system-aarch64","2a87f531371b3f8d45141d48632e0dbe2c3d968fbcb385ba105d671479fb8c99",43800304)
 private val kernel=Asset("Kernel","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/Image","cbe59a02e7ea979a150661032440c94e2c4db0b735af2416e11ae5cac15a58e4",37605312)
 private val initrd=Asset("Initrd","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/initrd.img","655f3ef013e7818e9ee874cf3b44a4c0bdc8a586c986cf237cb74c41862dfd02",38301815)
 private val slirp=Asset("libslirp","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/libslirp.so","226372426fda32c9fccd8e831d0901a86bfff3c3e6f7a60336d6dde149f756c4",1145496)
 private val rootfs=Asset("Debian rootfs","https://github.com/zalexdev/strykerapp/releases/download/rootless-main/rootfs.imgz","f80c2b1e2433c3036aa745da2a5935cf5dd61b65b17bbeabae87b49bd68a12ef",427974567)
 fun install(c:Context,p:(String,Long,Long)->Unit):Boolean {if(!android.os.Build.SUPPORTED_ABIS.contains("arm64-v8a"))return false;val d=File(c.filesDir,"rootless").apply{mkdirs()};return try{download(qemu,File(d,"qemu-system-aarch64"),p);download(kernel,File(d,"Image"),p);download(initrd,File(d,"initrd.img"),p);download(slirp,File(d,"libslirp.so"),p);val gz=File(d,"rootfs.imgz");download(rootfs,gz,p);gunzip(gz,File(d,"rootfs.img"));gz.delete();true}catch(_:Exception){false}}
 private fun download(a:Asset,f:File,p:(String,Long,Long)->Unit){if(f.exists()&&f.length()==a.size&&sha(f)==a.sha256)return;val c=URL(a.url).openConnection() as HttpURLConnection;c.connectTimeout=30000;c.readTimeout=120000;c.connect();require(c.responseCode in 200..299);c.inputStream.use{input->FileOutputStream(f).use{out->val b=ByteArray(262144);var n=0L;while(true){val r=input.read(b);if(r<0)break;out.write(b,0,r);n+=r;p(a.name,n,a.size)}}};require(f.length()==a.size&&sha(f)==a.sha256)}
 private fun gunzip(src:File,dst:File){GZIPInputStream(FileInputStream(src),65536).use{input->FileOutputStream(dst).use{out->val b=ByteArray(65536);while(true){val n=input.read(b);if(n<0)break;out.write(b,0,n)}}}}
 private fun sha(f:File):String{val md=MessageDigest.getInstance("SHA-256");FileInputStream(f).use{i->val b=ByteArray(131072);while(true){val n=i.read(b);if(n<0)break;md.update(b,0,n)}};return md.digest().joinToString(""){String.format("%02x",it)}}
}
