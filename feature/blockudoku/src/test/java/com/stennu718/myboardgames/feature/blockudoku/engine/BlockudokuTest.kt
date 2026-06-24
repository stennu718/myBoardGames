package com.stennu718.myboardgames.feature.blockudoku.engine

import org.junit.Test
import org.junit.Assert.*

class BlockudokuTest {

    @Test
    fun testEmptyBoardAllMovesValid() {
        val board = BlockudokuBoard()
        val piece = arrayOf(booleanArrayOf(true))
        assertTrue(board.canPlace(piece, 0, 0))
        assertTrue(board.canPlace(piece, 8, 8))
    }

    @Test
    fun testPlacePiece() {
        val board = BlockudokuBoard()
        val piece = arrayOf(booleanArrayOf(true, true))
        val newBoard = board.place(piece, 0, 0)
        assertTrue(newBoard.grid[0][0])
        assertTrue(newBoard.grid[0][1])
        assertEquals(2, newBoard.score)
    }

    @Test
    fun testCannotPlaceOnOccupiedCell() {
        val board = BlockudokuBoard()
        val piece = arrayOf(booleanArrayOf(true))
        val newBoard = board.place(piece, 0, 0)
        assertFalse(newBoard.canPlace(piece, 0, 0))
    }

    @Test
    fun testCannotPlaceOutOfBounds() {
        val board = BlockudokuBoard()
        val piece = arrayOf(
            booleanArrayOf(true, true, true, true, true)
        )
        assertFalse(board.canPlace(piece, 0, 7)) // would go to col 11
    }

    @Test
    fun testRowClear() {
        val board = BlockudokuBoard()
        // Fill a row manually
        for (c in 0..8) board.grid[0][c] = true
        val piece = arrayOf(booleanArrayOf(true))
        val newBoard = board.place(piece, 1, 0)
        // Row 0 should be cleared
        assertFalse(newBoard.grid[0].any { it })
    }

    @Test
    fun testColumnClear() {
        val board = BlockudokuBoard()
        // Fill a column manually
        for (r in 0..8) board.grid[r][0] = true
        val piece = arrayOf(booleanArrayOf(true))
        val newBoard = board.place(piece, 0, 1)
        // Column 0 should be cleared
        for (r in 0..8) {
            assertFalse(newBoard.grid[r][0])
        }
    }

    @Test
    fun testBoxClear() {
        val board = BlockudokuBoard()
        // Fill a 3x3 box manually
        for (r in 0..2) for (c in 0..2) {
            board.grid[r][c] = true
        }
        val piece = arrayOf(booleanArrayOf(true))
        val newBoard = board.place(piece, 3, 3)
        // Box should be cleared
        for (r in 0..2) for (c in 0..2) {
            assertFalse(newBoard.grid[r][c])
        }
    }

    @Test
    fun testPieceGenerator() {
        val piece = PieceGenerator.generatePiece()
        assertTrue(piece.isNotEmpty())
        assertTrue(piece.any { row -> row.any { it } })
    }

    @Test
    fun testPieceBatch() {
        val batch = PieceGenerator.generatePieceBatch()
        assertEquals(3, batch.size)
    }

    @Test
    fun testGameOverWhenNoValidMoves() {
        val board = BlockudokuBoard()
        // Fill most of the board
        for (r in 0..8) for (c in 0..8) {
            board.grid[r][c] = true
        }
        // Place a small piece to trigger game over check
        val piece = arrayOf(
            booleanArrayOf(true, true),
            booleanArrayOf(true, true)
        )
        // Can't place anywhere, so game should be over
        assertTrue(board.canPlaceAnywhere(piece) || board.isGameOver(listOf(piece)))
    }

    @Test
    fun testScoreIncreasesOnClear() {
        val board = BlockudokuBoard()
        // Fill a row
        for (c in 0..8) board.grid[0][c] = true
        val piece = arrayOf(booleanArrayOf(true))
        val newBoard = board.place(piece, 1, 0)
        // Score should be > 0 (1 for placed + 10 for clearing)
        assertTrue(newBoard.score > 1)
    }
}

private fun BlockudokuBoard.canPlaceAnywhere(piece: Array<BooleanArray>): Boolean {
    for (r in 0..8) for (c in 0..8) {
        if (canPlace(piece, r, c)) return true
    }
    return false
}
