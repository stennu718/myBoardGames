package com.stennu718.myboardgames.feature.checkers.engine

import org.junit.Test
import org.junit.Assert.*

class CheckersTest {

    @Test
    fun testInitialBoardSetup() {
        val board = CheckersBoard()
        // 12 black pieces on dark squares in rows 0-2
        var blackCount = 0
        for (r in 0..2) for (c in 0..7) {
            if (board.board[r][c]?.color == CheckersColor.BLACK) blackCount++
        }
        assertEquals(12, blackCount)

        // 12 red pieces on dark squares in rows 5-7
        var redCount = 0
        for (r in 5..7) for (c in 0..7) {
            if (board.board[r][c]?.color == CheckersColor.RED) redCount++
        }
        assertEquals(12, redCount)
    }

    @Test
    fun testInitialMove() {
        val board = CheckersBoard()
        // Red can move from (5,0) to (4,1) or (5,2) to (4,1) etc
        val moves = board.getValidMoves(CheckersColor.RED)
        assertTrue(moves.isNotEmpty())
    }

    @Test
    fun testSimpleMove() {
        val board = CheckersBoard()
        val result = board.movePiece(5, 0, 4, 1)
        assertTrue(result)
        assertNotNull(board.getPiece(4, 1))
        assertNull(board.getPiece(5, 0))
    }

    @Test
    fun testTurnAlternation() {
        val board = CheckersBoard()
        assertEquals(CheckersColor.RED, board.turn)
        board.movePiece(5, 0, 4, 1)
        assertEquals(CheckersColor.BLACK, board.turn)
    }

    @Test
    fun testInvalidMoveWrongColor() {
        val board = CheckersBoard()
        // Try to move black piece on red's turn
        val result = board.movePiece(2, 1, 3, 0)
        assertFalse(result)
    }

    @Test
    fun testKingPromotion() {
        val board = CheckersBoard()
        // Clear the board
        for (r in 0..7) for (c in 0..7) board.board[r][c] = null
        // Place red pawn at (1, 2)
        board.board[1][2] = CheckersPiece(CheckersColor.RED)
        board.turn = CheckersColor.RED
        // Move to row 0
        val result = board.movePiece(1, 2, 0, 1)
        assertTrue(result)
        assertTrue(board.getPiece(0, 1)?.isKing == true)
    }

    @Test
    fun testJumpCapture() {
        val board = CheckersBoard()
        // Clear the board
        for (r in 0..7) for (c in 0..7) board.board[r][c] = null
        // Set up jump scenario
        board.board[4][3] = CheckersPiece(CheckersColor.RED)
        board.board[3][2] = CheckersPiece(CheckersColor.BLACK)
        board.turn = CheckersColor.RED
        val result = board.movePiece(4, 3, 2, 1)
        assertTrue(result)
        assertNull(board.getPiece(3, 2)) // captured piece removed
    }

    @Test
    fun testGameNotOverInitially() {
        val board = CheckersBoard()
        assertFalse(board.isGameOver())
    }
}
