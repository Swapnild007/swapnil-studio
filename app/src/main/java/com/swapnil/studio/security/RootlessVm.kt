package com.swapnil.studio.security

import android.content.Context
import java.io.File

class RootlessVm(private val context:Context){private val d=File(context.filesDir,"rootless");fun installed()=File(d,"qemu-system-aarch64").exists()&&File(d,"Image").exists()&&File(d,"initrd.img").exists()&&File(d,"rootfs.img").exists();fun start():Process{require(installed()){"Rootless engine is not installed"};val q=File(d,"qemu-system-aarch64");q.setExecutable(true,false);return ProcessBuilder(q.absolutePath,"-machine","virt","-cpu","max","-m","2048","-kernel",File(d,"Image").absolutePath,"-initrd",File(d,"initrd.img").absolutePath,"-append","console=ttyAMA0 root=/dev/vda rw","-drive","file=${File(d,"rootfs.img").absolutePath},format=raw,if=virtio","-netdev","user,id=n0","-device","virtio-net-device,netdev=n0","-nographic").directory(d).redirectErrorStream(true).apply{environment()["LD_LIBRARY_PATH"]=d.absolutePath}.start()}}
