package jp.co.kamakuni.balking

import java.io.File

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

    fun doSave(){
        println("${Thread.currentThread().name} calls doSave, content = $content")
        File(this.filename).writeText(this.content)
    }
}

class Changer: Runnable{

}

class Sever: Runnable{

}

fun main(args: Array<String>) {

}

