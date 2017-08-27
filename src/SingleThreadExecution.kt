package jp.co.kamakuni.singlethreadexecution

/**
 * Created by kamakuni on 2017/08/15.
 */
class Gate {

    private var counter = 0
    private var name = ""
    private var address = ""

    @Synchronized
    fun pass(name: String, address: String) {
        this.name = name
        this.address = address
        check()
    }
    @Synchronized
    override fun toString() = "No.$counter: $name, $address"
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
