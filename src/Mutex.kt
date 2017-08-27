package jp.co.kamakuni.mutex

import java.util.concurrent.Semaphore

/**
 * Created by kamakuni on 2017/08/15.
 */

class Mutex{
    fun lock(){
        // NOP
    }
    fun unlock(){
        // NOP
    }
}

class Gate {

    private var counter = 0
    private var name = ""
    private var address = ""
    private val mutex: Mutex = Mutex()

    fun pass(name: String, address: String) {
        mutex.lock()
        try {
            this.name = name
            this.address = address
            check()
        } finally {
            mutex.unlock()
        }
    }

    override fun toString():String {
        mutex.lock()
        try {
            return "No.$counter: $name, $address"
        } finally {
            mutex.unlock()
        }
    }

    private fun check() {
        if (this.name[0] != this.address[0]){
            println("****** BROKEN ******: $name, $address")
        }
    }
}

class User(private val gate: Gate,private val name:String,private val address:String): Runnable {

    override fun run() {
        while(true) {
            this.gate.pass(name, address)
        }
    }
}

fun main(args: Array<String>) {
    val gate = Gate()
    Thread(User(gate,"Alice","Alaska")).start()
    Thread(User(gate,"Bobby","Brazil")).start()
    Thread(User(gate,"Chris","Canada")).start()
}
