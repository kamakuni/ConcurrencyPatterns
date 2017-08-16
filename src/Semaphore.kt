package jp.co.kamakuni.semaphore

import java.util.*
import java.util.concurrent.Semaphore

/**
 * Created by kamakuni on 2017/08/15.
 */
object Log {
    fun log(s: String){
        println("${Thread.currentThread().name}: $s")
    }
}

class BoundedResource(private val permit: Int) {
    private val semaphore: Semaphore = Semaphore(permit)

    companion object {
        val random = Random(314159)
    }

    fun use() {
        semaphore.acquire()
        try {
            doUse()
        } finally {
            semaphore.release()
        }
    }

    private fun doUse() {
        Log.log("BEGIN: used = ${permit - semaphore.availablePermits()}")
        Thread.sleep(random.nextInt(500).toLong())
        Log.log("END: used = ${permit - semaphore.availablePermits()}")
    }
}

class User(private val resource: BoundedResource): Runnable {
    companion object {
        val random = Random(26535)
    }

    override fun run() {
        try {
            while(true){
                resource.use()
                Thread.sleep(random.nextInt(3000).toLong())
            }
        } catch(e: InterruptedException) {
        }
    }
}


fun main(args: Array<String>){
    val resource = BoundedResource(3)
    for (i in 1..10) Thread(User(resource)).start()
}