package jp.co.kamakuni.deadlock

/**
 * Created by kamakuni on 2017/08/17.
 */
class Tool(private val name:String) {
    override fun toString(): String = "[ $name ]"
}

class Eater(private val name:String, private val lefthand:Tool, private val righthand: Tool): Runnable{

    override fun run() {
        while(true){
            eat(lefthand,righthand)
        }
    }

    @Synchronized
    private fun eat(lefthand: Tool, righthand: Tool){
        println("$name takes up $lefthand (left).")
        println("$name takes up $righthand (right).")
        println("$name is eating now, yum yum.")
        println("$name puts down $righthand (right).")
        println("$name puts down $lefthand (left).")
    }

/*    private fun eat() {
        synchronized(lefthand){
            synchronized(righthand){
                println("$name takes up lefthand $lefthand (left).")
                println("$name takes up $righthand (right).")
                println("$name is eating now, yum yum.")
                println("$name puts down $righthand (right).")
                println("$name puts down $lefthand (left).")
            }
        }
    }*/
}

fun main(args: Array<String>){
    val spoon = Tool("spoon")
    val fork = Tool("fork")
    Thread(Eater(name = "Alice",lefthand = spoon,righthand = fork)).start()
    Thread(Eater(name = "Bob",lefthand = fork,righthand = spoon)).start()
}