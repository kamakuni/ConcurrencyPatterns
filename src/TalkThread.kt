package jp.co.kamakuni.talkthread

import java.util.*

class Request(val name:String) {
    override fun toString(): String = "[ Request $name]"
}

class RequestQueue {
    var queue = LinkedList<Request>()

    @Synchronized
    fun getRequest(): Request {
        while(queue.peek() == null){
            try{
                (this as java.lang.Object).wait()
            }catch (e: InterruptedException){

            }
        }
        return queue.remove()
    }

    @Synchronized
    fun putRequest(request: Request) {
        queue.offer(request)
        (this as java.lang.Object).notifyAll()
    }
}

class TalkThread(private val input:RequestQueue, private val output:RequestQueue, private val name:String): Runnable {
    override fun run() {
        print("${Thread.currentThread().name}:BEGIN")
        for(i in 0 until 10){
            val request1 = input.getRequest()
            print("${Thread.currentThread().name} gets $request1")
            val request2 = Request("${request1.name}!")
            print("${Thread.currentThread().name} puts $request2")
            output.putRequest(request2)
        }
        print("${Thread.currentThread().name}:END")
    }
}

fun main(args: Array<String>){
    val request1 = RequestQueue()
    val request2 = RequestQueue()
    Thread(TalkThread(request1, request2, "Alice")).start()
    //Thread(TalkThread(request2, request1, "Bobby")).start()
}