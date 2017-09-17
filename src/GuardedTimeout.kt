package jp.co.kamakuni.guardedtimeout

import java.util.concurrent.TimeoutException

class Host(val timeout:Long){
    var ready: Boolean = false

    @Synchronized
    fun setExecutable(on:Boolean) {
        ready = on
        (this as java.lang.Object).notifyAll()
    }

    @Synchronized
    fun execute(){
        val start = System.currentTimeMillis()
        while (!ready){
            val now = System.currentTimeMillis()
            val rest = timeout - (now - start)
            if(rest <= 0) {
                throw TimeoutException("now - start = ${now - start} , timeout = $timeout")
            }
            (this as java.lang.Object).wait(rest)
        }
        doExecute()
    }

    private fun doExecute(){
        println("${Thread.currentThread().name} calls doExecute")
    }
}

fun main(args: Array<String>){
    val host = Host(10000)
    try {
        println("execute BEGIN")
        host.execute()
    } catch (e: TimeoutException){
        e.printStackTrace()
    } catch (e: InterruptedException){
        e.printStackTrace()
    }
}