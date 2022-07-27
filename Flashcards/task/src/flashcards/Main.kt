package flashcards

val cards = mutableMapOf<String, String>()


fun main() {
    println("Input the number of cards:")

    val numberOfCards: Int? = try {
        readln().toInt()
    } catch (e: Exception) {
        println("please enter a valid number")
        null
    }

    numberOfCards?.let { number ->
        var term: String
        var definition: String
        var i = 1
        while (i <= number) {
            println("Card #$i:")
            term = readln()
            if (cards.containsKey(term)) {
                println("The term \"$term\" already exists. Try again:")
            } else {
                println("The definition for card #$i:")
                definition = readln()
                while (cards.containsValue(definition)) {
                    println("The definition \"$definition\" already exists. Try again:")
                    definition = readln()
                }
                cards[term] = definition
                i++

            }
        }


        for (k in cards.keys) {
            println("Print the definition of \"$k\":")
            definition = readln()
            if (definition == cards[k]) {
                println("Correct!")
            } else {
                if (cards.containsValue(definition)) {
                    println(
                        "Wrong. The right answer is \"${cards[k]}\"," +
                                " but your definition is correct for \"${findKey(definition)}\"."
                    )
                } else{
                    println("Wrong. The right answer is \"${cards[k]}\".")
                }
            }
        }
    }

}

fun findKey(def: String): String {
    var key = ""
    for (k in cards.keys) {
        if (cards[k] == def) {
            key = k
        }
    }
    return key
}
