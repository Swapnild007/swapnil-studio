package com.swapnil.studio.security

import android.content.Context
import java.io.File

/** VM lifecycle shell. Actual QEMU invocation is isolated here so the UI never needs root. */
class RootlessVm(private val context:Context) {
 private val dir=File(context.filesDir,"rootless")
 fun installed()=File(dir,"QEMU").exists() && File(dir,"Kernel").exists() && File(dir,"Initrd").exists() && File(dir,"libslirp").exists() && File(dir,"Debian_rootfs").exists()
 fun start():Process {
  require(installed()) { "Rootless engine is not installed" }
  val qemu=File(dir,"QEMU");qemu.setExecutable(true,false)
  return ProcessBuilder(qemu.absolutePath,"-machine","virt","-cpu","max","-nographic","-m","2048","-kernel",File(dir,"Kernel").absolutePath,"-initrd",File(dir,"Initrd").absolutePath,"-drive","file=${File(dir,"Debian_rootfs").absolutePath},format=raw,if=virtio","-netdev","user,id=n0","-device","virtio-net-device,netdev=n0").directory(dir).redirectErrorStream(true).start()
 }
}
