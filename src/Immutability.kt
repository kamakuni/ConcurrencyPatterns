package jp.co.kamakuni.immutability

import java.util.*

class Writer(private var list: MutableList<Int>): Runnable {

    override fun run() {
        var i = 0
        while(true){
            list.add(i)
            list.removeAt(0)
            i++
        }
    }
}

class Reader(private val list: MutableList<Int>):Runnable{

    override fun run() {
        synchronized(list) {
            while(true){
                for (item in list){
                    print(item)
                }
            }
        }
    }

}

fun main(args: Array<String>) {
    var list = Collections.synchronizedList(mutableListOf<Int>())
    Thread(Writer(list)).start()
    Thread(Reader(list)).start()
}