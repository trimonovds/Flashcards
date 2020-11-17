package flashcards

import java.util.*

data class FlashCard(val term: String, val definition: String)

class Engine(private val scanner: Scanner) {
    private fun checkCard(card: FlashCard) {
        println("Print the definition of \"${card.term}\":")
        val answer = scanner.nextLine()
        if (card.definition == answer) {
            println("Correct!")
        } else {
            println("Wrong. The right answer is \"${card.definition}\"")
        }
    }

    private fun readCard(index: Int): FlashCard {
        println("Card #$index")
        val term = scanner.nextLine()
        println("The definition for card #$index")
        val definition = scanner.nextLine()
        return FlashCard(term, definition)
    }

    fun readCards(): List<FlashCard> {
        println("Input the number of cards:")
        val numberOfCards = scanner.nextLine().toInt()
        val cards = mutableListOf<FlashCard>()
        for (i in 1..numberOfCards) {
            val card = readCard(i)
            cards.add(card)
        }
        return cards
    }

    fun checkCards(cards: List<FlashCard>) {
        for (i in 1..cards.size) {
            val card = cards[i - 1]
            checkCard(card)
        }
    }

}

fun main() {
    val scanner = Scanner(System.`in`)
    val engine = Engine(scanner)
    val cards = engine.readCards()
    engine.checkCards(cards)
}



