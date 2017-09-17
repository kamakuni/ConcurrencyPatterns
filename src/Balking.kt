package jp.co.kamakuni.balking

import java.io.File
import java.io.IOException
import java.util.*

class Data (val filename: String, var content: String){
    private var changed: Boolean = true

    @Synchronized
    fun change(content: String) {
        this.content = content
        this.changed = true
    }

    @Synchronized
    fun save() {
        if(!changed) {
            return
        } else {
            doSave()
            this.changed = false
        }
    }

    private fun doSave(){
        println("${Thread.currentThread().name} calls doSave, content = $content")
        File(this.filename).writeText(this.content)
    }
}

class Saver: Thread{

    private val threadName:String
    private val data:Data

    constructor(threadName: String, data: Data): super(threadName){
        this.threadName = threadName
        this.data = data
    }

    override fun run() {
        try{
            while(true){
                data.save()
                Thread.sleep(1000)
            }
        } catch (e: IOException){
            e.printStackTrace()
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }
}

class Changer: Thread{

    private val threadName: String
    private val data: Data
    private val random = Random()

    constructor(threadName: String, data: Data): super(threadName){
        this.threadName = threadName
        this.data = data
    }

    override fun run() {
        var i = 0
        try {
            while (true) {
                data.change("No.$i")
                Thread.sleep(random.nextInt(1000).toLong())
                data.save()
                i++
            }
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: InterruptedException){
            e.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    val data = Data("data.txt","(empty)")
    Changer("Changer",data).start()
    Saver("Saver",data).start()
}