package flashcards

import java.io.File
import kotlin.random.Random

val cards = mutableMapOf<String, Card>()

class Card(var definition: String, var mistakes: Int = 0)

fun main(args: Array<String>) {
    if (args.contains("-import")) {
        val importFile = args[args.indexOf("-import") + 1]
        val file = File(importFile)
        importCards(file)
    }
    var action: String = ""
    while (action != "exit") {
        println("Input the action (add, remove, import, export, ask, exit):")
        action = readln()
        when (action) {
            "add" -> addCard()
            "remove" -> removeCard()
            "export" -> exportCards()
            "import" -> importCards()
            "ask" -> askUser()
            "log" -> saveLog()
            "hardest card" -> findTheHardestCard()
            "reset stats" -> resetState()
            "exit" -> {
                if (args.contains("-export")) {
                    val exportFile = args[args.indexOf("-export") + 1]
                    val file = File(exportFile)
                    exportCards(file)
                }
                println("Bye bye!")
            }
        }
    }


}

fun resetState() {
    for (card in cards) {
        card.value.mistakes = 0
    }
    println("Card statistics have been reset.")
}

fun findTheHardestCard() {
    val map = mutableMapOf<String, Card>()
    var max = 1
    for (card in cards) {
        if (card.value.mistakes > max) {
            map.clear()
            max = card.value.mistakes
            map[card.key] = card.value
        } else {
            if (card.value.mistakes == max) {
                map[card.key] = card.value
            }
        }
    }

    when (map.size) {
        0 -> println("There are no cards with errors.")
        1 -> {
            println("The hardest card is \"${map.keys.first()}\". You have ${map.values.toList()[0].mistakes} errors answering it")
        }
        else -> {
            var s: String = ""
            for (card in map) {
                s += "\"${card.key}\", "
            }
            s = s.dropLast(2)
            println("The hardest cards are $s. You have ${map.values.toList()[0].mistakes} errors answering them.")

        }
    }


}

fun saveLog() {
    println("File name:")
    val fileName = readln()
    val file = File(fileName)
    val logfile = File("/home/ax/IdeaProjects/Flashcards/logsfile")
    file.appendText(logfile.readText())
    println("The log has been saved.")
}


fun findKey(def: String): String {
    var key = ""
    for (k in cards.keys) {
        if (cards[k]?.definition == def) {
            key = k
        }
    }
    return key
}

fun findDefinition(definition: String): Boolean {
    var temp: Boolean = false
    for (card in cards) {
        if (card.value.definition == definition) {
            temp = true
            break
        }
    }
    return temp
}

fun addCard() {
    val definition: String
    println("The Card:")
    val term: String = readln()
    if (cards.containsKey(term)) {
        println("The card \"$term\" already exists")
    } else {
        println("The definition of the card:")
        definition = readln()
        if (findDefinition(definition)) {
            println("The definition \"$definition\" already exists.")
        } else {
            cards[term] = Card(definition)
            println("The pair (\"$term\":\"$definition\") has been added")
        }

    }
}

fun removeCard() {
    println("Which card?")
    val term = readln()
    if (cards.containsKey(term)) {
        cards.remove(term)
        println("The card has been removed.")
    } else {
        println("Can't remove \"$term\": there is no such card.")
    }
}

fun exportCards(expFile: File? = null) {
    val file: File = if (expFile == null) {
        println("File name:")
        val fileName = readln()
        File(fileName)
    } else {
        expFile
    }
    if (file.exists()) file.writeText("")
    for ((u, v) in cards) {
        file.appendText("$u&${v.definition}&${v.mistakes}\n")
    }
    println("${cards.size} cards have been saved.")

}

fun importCards(impFile: File? = null) {

    val file: File = if (impFile == null) {
        println("File name:")
        val fileName = readln()
        File(fileName)
    } else {
        impFile
    }
    if (file.exists()) {
        file.forEachLine { line ->
            val list = line.split("&")
            cards[list[0]] =
                Card(list[1], list[2].toInt())
        }
        println("${file.readLines().size} cards have been loaded.")
    } else {
        println("File not found.")
    }
}

fun askUser() {
    if (cards.isEmpty()) {
        println("there is no cards, try add or import some cards.")
    } else {

        println("How many times to ask?")
        val numberOfCards: Int? = try {
            readln().toInt()
        } catch (e: Exception) {
            println("please enter a valid number")
            null
        }

        numberOfCards?.let { number ->
            val list = cards.keys.toList()
            for (k in 1..number) {
                val i = Random.nextInt(list.size)
                println("Print the definition of \"${list[i]}\":")

                val definition = readln()
                if (definition == cards[list[i]]?.definition) {
                    println("Correct!")
                } else {
                    cards[list[i]]?.mistakes = cards[list[i]]?.mistakes!! + 1
                    if (findDefinition(definition)) {
                        println(
                            "Wrong. The right answer is \"${cards[list[i]]?.definition}\"," +
                                    " but your definition is correct for \"${findKey(definition)}\"."
                        )
                    } else {
                        println("Wrong. The right answer is \"${cards[list[i]]?.definition}\".")
                    }
                }
            }
        }
    }
}
