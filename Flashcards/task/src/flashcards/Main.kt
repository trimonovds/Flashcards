package flashcards

import java.util.*

data class FlashCard(val term: String, val definition: String)

class Engine(private val scanner: Scanner) {
    private fun checkCard(card: FlashCard, cards: List<FlashCard>) {
        println("Print the definition of \"${card.term}\":")
        val answer = scanner.nextLine()
        if (card.definition == answer) {
            println("Correct!")
        } else {
            val answerCard = cards.filter { it != card }.singleOrNull { it.definition == answer }
            val additionalText: String = if (answerCard != null) "but your definition is correct for \"${answerCard.term}\"" else ""
            println("Wrong. The right answer is \"${card.definition}\"" + additionalText)
        }
    }

    private fun readCard(index: Int, cards: List<FlashCard>): FlashCard {
        val cardsTerms = cards.map { it.term }.toSet()
        val cardsDefinitions = cards.map { it.definition }.toSet()
        var term: String? = null
        var definition: String? = null

        println("Card #$index")
        while (true) {
            val inputTerm = scanner.nextLine()
            if (cardsTerms.contains(inputTerm)) {
                println("The term \"$inputTerm\" already exists. Try again:")
            } else {
                term = inputTerm
                break
            }
        }

        println("The definition for card #$index")
        while (true) {
            val inputDefinition = scanner.nextLine()
            if (cardsDefinitions.contains(inputDefinition)) {
                println("The definition \"$inputDefinition\" already exists. Try again:")
            } else {
                definition = inputDefinition
                break
            }
        }

        return FlashCard(term!!, definition!!)
    }

    fun readCards(): List<FlashCard> {
        println("Input the number of cards:")
        val numberOfCards = scanner.nextLine().toInt()
        val cards = mutableListOf<FlashCard>()
        for (i in 1..numberOfCards) {
            val card = readCard(i, cards)
            cards.add(card)
        }
        return cards
    }

    fun checkCards(cards: List<FlashCard>) {
        for (i in 1..cards.size) {
            val card = cards[i - 1]
            checkCard(card, cards)
        }
    }

}

fun main() {
    val scanner = Scanner(System.`in`)
    val engine = Engine(scanner)
    val cards = engine.readCards()
    engine.checkCards(cards)
}



