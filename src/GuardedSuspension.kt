package jp.co.kamakuni.guardedsuspension

import java.util.*

class Request(private val name:String) {
    override fun toString(): String = "[ Request $name]"
}

class Client(private val requestQueue: RequestQueue, private val name : String, private val seed:Long): Runnable {

    private val random = Random(seed)

    override fun run() {
        for(i in 0 until 10000) {
            val request = Request("[No.$i]")
            requestQueue.putRequest(request)
            println("${Thread.currentThread().name} requests $request")
            try {
                Thread.sleep(random.nextInt(1000).toLong())
            } catch (e : InterruptedException) {

            }
        }
    }
}

class Server(private val requestQueue: RequestQueue, private val name:String, private val seed:Long): Runnable{

    private val random = Random(seed)

    override fun run() {
        for(i in 0 until 10000) {
            val request = requestQueue.getRequest()
            println("${Thread.currentThread().name} handles $request")
            try {
                Thread.sleep(random.nextInt(1000).toLong())
            } catch(e: InterruptedException) {

            }
        }
    }
}

class RequestQueue {
    var queue = LinkedList<Request>()

    @Synchronized
    fun getRequest(): Request{
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

fun main(args: Array<String>) {
    val q = RequestQueue()
    Thread(Client(q,"Alice",3141592)).start()
    Thread(Server(q,"Bobby",6539857)).start()
}