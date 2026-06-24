package com.stennu718.myboardgames.feature.puzzles.engine

import kotlin.random.Random

data class MemoryCard(
    val id: Int,
    val emoji: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

data class MemoryGameState(
    val cards: List<MemoryCard>,
    val flippedIndices: List<Int> = emptyList(),
    val matchedPairs: Int = 0,
    val moves: Int = 0,
    val isComplete: Boolean = false
)

class MemoryGame {
    private val emojis = listOf(
        "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼",
        "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵", "🐔",
        "🐧", "🐦", "🐤", "🦄", "🐝", "🐌", "🦋", "🐙"
    )

    private var state = MemoryGameState(emptyList())

    fun newState(pairCount: Int = 8): MemoryGameState {
        val selectedEmojis = emojis.shuffled().take(pairCount)
        val cards = mutableListOf<MemoryCard>()
        for ((index, emoji) in selectedEmojis.withIndex()) {
            cards.add(MemoryCard(id = index * 2, emoji = emoji))
            cards.add(MemoryCard(id = index * 2 + 1, emoji = emoji))
        }
        state = MemoryGameState(cards = cards.shuffled())
        return state
    }

    fun getState(): MemoryGameState = state

    fun flipCard(index: Int): MemoryGameState {
        if (state.isComplete) return state
        if (state.flippedIndices.size >= 2) return state
        if (state.cards[index].isFlipped || state.cards[index].isMatched) return state

        val newCards = state.cards.toMutableList()
        newCards[index] = newCards[index].copy(isFlipped = true)

        val newFlipped = state.flippedIndices + index
        var newState = state.copy(cards = newCards, flippedIndices = newFlipped)

        if (newFlipped.size == 2) {
            val first = newCards[newFlipped[0]]
            val second = newCards[newFlipped[1]]
            if (first.emoji == second.emoji) {
                newCards[newFlipped[0]] = first.copy(isMatched = true)
                newCards[newFlipped[1]] = second.copy(isMatched = true)
                val matchedPairs = state.matchedPairs + 1
                val isComplete = matchedPairs == (state.cards.size / 2)
                newState = newState.copy(
                    cards = newCards,
                    flippedIndices = emptyList(),
                    matchedPairs = matchedPairs,
                    moves = state.moves + 1,
                    isComplete = isComplete
                )
            } else {
                // Flip back after delay (handled by UI)
                newState = newState.copy(moves = state.moves + 1)
            }
        }

        state = newState
        return state
    }

    fun flipBackUnmatched() {
        if (state.flippedIndices.size == 2) {
            val newCards = state.cards.toMutableList()
            for (idx in state.flippedIndices) {
                if (!newCards[idx].isMatched) {
                    newCards[idx] = newCards[idx].copy(isFlipped = false)
                }
            }
            state = state.copy(cards = newCards, flippedIndices = emptyList())
        }
    }
}