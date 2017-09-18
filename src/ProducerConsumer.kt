package jp.co.kamakuni.producerconsumer

import java.util.*

class Maker: Thread {

    private var random: Random
    private var table: Table

    //var id: Int = 0

    constructor(name: String, table :Table,seed: Long): super(name){
        this.random = Random(seed)
        this.table = table
    }

    companion object {
        private var id = 0
        @Synchronized
        fun nextId(): Int {
            return id++
        }
    }

    override fun run() {
        try {
            while(true){
                Thread.sleep(random.nextInt(1000).toLong())
                val cake = "[ Cake No. ${nextId()} by $name]"
                table.put(cake)
            }
        } catch (e: InterruptedException){}
    }

    /*@Synchronized
    fun nextId():Int {
        return id++
    }*/
}

class Eater: Thread {

    private var random: Random
    private var table: Table

    constructor(name: String, table: Table, seed: Long): super(name) {
        this.random = Random(seed)
        this.table = table
    }

    override fun run() {
        try {
            while(true){
                val cake = table.take()
                Thread.sleep(random.nextInt(1000).toLong())
            }
        }catch (e: InterruptedException){}
    }
}

class Table(private var count: Int){
    private val buffer = arrayOfNulls<String>(count)
    private var head = 0
    private var tail = 0

    @Synchronized
    fun put(cake:String){
        println("${Thread.currentThread().name} puts $cake")
        while(count >= buffer.size){
            (this as java.lang.Object).wait()
        }
        buffer[tail] = cake
        tail = (tail + 1) % buffer.size
        count++
        (this as java.lang.Object).notifyAll()
    }

    @Synchronized
    fun take(): String{
        while(count <= 0){
            (this as java.lang.Object).wait()
        }
        val cake = buffer[head] ?: ""
        head = (head + 1 ) % buffer.size
        count--
        (this as java.lang.Object).notifyAll()
        println("${Thread.currentThread().name} takes $cake")
        return cake
    }
}

fun main(args: Array<String>){
    val table = Table(3)
    Maker("Maker-1", table,31415).start()
    Maker("Maker-2", table,92653).start()
    Maker("Maker-3", table,58979).start()
    Eater("Eater-1", table,32384).start()
    Eater("Eater-2", table,62643).start()
    Eater("Eater-3", table,38327).start()
}