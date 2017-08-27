package jp.co.kamakuni.immutable

class Person(val name:String, val address: String) {

    override fun toString(): String {
        return "Person [name = $name, address = $address]"
    }
}

class PrintPerson(private val person: Person): Runnable {
    override fun run() {
        while(true){
            println("${Thread.currentThread().name} prints $person")
        }
    }
}

fun main(args: Array<String>) {
    val alice = Person("Alice", address = "Alaska")
    val bob = Person("Bob", address = "Brazil")
    Thread(PrintPerson(alice)).start()
    Thread(PrintPerson(bob)).start()
}