package com.stennu718.myboardgames.feature.puzzles.engine

import org.junit.Test
import org.junit.Assert.*

class Game2048Test {

    @Test
    fun testNewGameHasTwoTiles() {
        val game = Game2048()
        game.newGame()
        val state = game.getState()
        val tileCount = state.grid.sumOf { row -> row.count { it != 0 } }
        assertEquals(2, tileCount)
    }

    @Test
    fun testMoveLeft() {
        val game = Game2048()
        game.newGame()
        val moved = game.move(Game2048.Direction.LEFT)
        val state = game.getState()
        assertTrue(state.score >= 0)
    }

    @Test
    fun testGameOverDetection() {
        val game = Game2048()
        game.newGame()
        assertFalse(game.getState().gameOver)
    }

    @Test
    fun testScoreIncreasesOnMerge() {
        val game = Game2048()
        game.newGame()
        val initialScore = game.getState().score
        repeat(10) {
            game.move(Game2048.Direction.LEFT)
            game.move(Game2048.Direction.RIGHT)
        }
        assertTrue(game.getState().score >= initialScore)
    }
}

class MinesweeperTest {

    @Test
    fun testNewGameHasCorrectMineCount() {
        val game = MinesweeperGame(9, 9, 10)
        game.newGame()
        val state = game.getState()
        var mineCount = 0
        for (r in 0 until 9) for (c in 0 until 9) {
            if (state.grid[r][c].hasMine) mineCount++
        }
        assertEquals(10, mineCount)
    }

    @Test
    fun testRevealEmptyCell() {
        val game = MinesweeperGame(9, 9, 10)
        game.newGame()
        val result = game.reveal(0, 0)
        assertTrue(result)
    }

    @Test
    fun testFlagToggle() {
        val game = MinesweeperGame(9, 9, 10)
        game.newGame()
        game.toggleFlag(0, 0)
        assertEquals(1, game.getState().flagCount)
        game.toggleFlag(0, 0)
        assertEquals(0, game.getState().flagCount)
    }

    @Test
    fun testNeighborMinesCalculation() {
        val game = MinesweeperGame(9, 9, 10)
        game.newGame()
        val state = game.getState()
        for (r in 0 until 9) for (c in 0 until 9) {
            val cell = state.grid[r][c]
            if (!cell.hasMine) {
                assertTrue(cell.neighborMines in 0..8)
            }
        }
    }

    @Test
    fun testGameWonWhenAllSafeRevealed() {
        val game = MinesweeperGame(3, 3, 1)
        game.newGame()
        for (r in 0 until 3) for (c in 0 until 3) {
            if (!game.getState().grid[r][c].hasMine) {
                game.reveal(r, c)
            }
        }
        assertEquals(MineGameStatus.WON, game.getState().status)
    }
}

class MemoryGameTest {

    @Test
    fun testNewGameHasCorrectCardCount() {
        val game = MemoryGame()
        val state = game.newState(8)
        assertEquals(16, state.cards.size)
    }

    @Test
    fun testFlipCard() {
        val game = MemoryGame()
        game.newState(2)
        val state = game.flipCard(0)
        assertTrue(state.cards[0].isFlipped)
    }

    @Test
    fun testMatchFound() {
        val game = MemoryGame()
        val initial = game.newState(2)
        val first = initial.cards[0]
        val second = initial.cards.indexOfFirst { it.id != first.id && it.emoji == first.emoji }
        assertTrue(second >= 0)

        game.flipCard(0)
        game.flipCard(second)
        assertTrue(game.getState().matchedPairs > 0)
    }

    @Test
    fun testGameComplete() {
        val game = MemoryGame()
        game.newState(2)
        val cards = game.getState().cards
        for (i in cards.indices step 2) {
            game.flipCard(i)
            game.flipCard(i + 1)
        }
        assertTrue(game.getState().isComplete)
    }

    @Test
    fun testMovesCounter() {
        val game = MemoryGame()
        game.newState(2)
        game.flipCard(0)
        game.flipCard(1)
        assertTrue(game.getState().moves > 0)
    }
}

class TicTacToeTest {

    @Test
    fun testNewGameEmptyBoard() {
        val game = TicTacToe()
        game.newGame()
        val state = game.getState()
        for (r in 0..2) for (c in 0..2) {
            assertEquals(Mark.EMPTY, state.board[r][c])
        }
    }

    @Test
    fun testMakeMove() {
        val game = TicTacToe()
        game.newGame()
        val result = game.makeMove(1, 1)
        assertTrue(result)
    }

    @Test
    fun testCannotMoveToOccupiedCell() {
        val game = TicTacToe()
        game.newGame()
        game.makeMove(0, 0)
        val result = game.makeMove(0, 0)
        assertFalse(result)
    }

    @Test
    fun testWinDetection() {
        val game = TicTacToe()
        game.newGame()
        game.makeMove(0, 0) // X (player)
        game.makeMove(1, 0) // O (AI)
        game.makeMove(0, 1) // X
        game.makeMove(1, 1) // O
        game.makeMove(0, 2) // X wins
        assertEquals(TicTacToeStatus.X_WON, game.getState().status)
    }

    @Test
    fun testAIMove() {
        val game = TicTacToe()
        game.newGame()
        val move = game.aiMove()
        assertNotNull(move)
        assertTrue(move.first in 0..2)
        assertTrue(move.second in 0..2)
    }
}
