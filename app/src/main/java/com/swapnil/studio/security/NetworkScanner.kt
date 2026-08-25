package com.swapnil.studio.security

import java.net.*
import java.util.concurrent.Executors

class NetworkScanner {
 data class Host(val address:String,val openPorts:List<Int>)
 fun localSubnet():String?=try{NetworkInterface.getNetworkInterfaces().toList().asSequence().flatMap{it.inetAddresses.toList().asSequence()}.filterIsInstance<Inet4Address>().map{it.hostAddress}.firstOrNull{!it.startsWith("127.")}}catch(_:Exception){null}
 fun scanHost(ip:String, ports:IntRange=1..1024):List<Int>{val open=mutableListOf<Int>();ports.forEach{p->try{Socket().use{s->s.connect(InetSocketAddress(ip,p),120);open+=p}}catch(_:Exception){}};return open}
 fun scanLocalPrefix(progress:(String)->Unit):List<Host>{val local=localSubnet()?:return emptyList();val prefix=local.substringBeforeLast('.')+".";val pool=Executors.newFixedThreadPool(16);val out=java.util.Collections.synchronizedList(mutableListOf<Host>());for(i in 1..254)pool.submit{val ip=prefix+i;try{Socket().use{s->s.connect(InetSocketAddress(ip,80),120)};out+=Host(ip,listOf(80));progress("Found $ip")}catch(_:Exception){}};pool.shutdown();pool.awaitTermination(40,java.util.concurrent.TimeUnit.SECONDS);return out}
}
