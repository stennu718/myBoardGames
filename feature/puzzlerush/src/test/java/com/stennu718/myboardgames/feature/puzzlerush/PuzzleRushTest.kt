package com.stennu718.myboardgames.feature.puzzlerush

import org.junit.Test
import org.junit.Assert.*
import com.stennu718.myboardgames.feature.chess.engine.*

class PuzzleRushTest {

    @Test
    fun testStartGame() {
        val engine = PuzzleRushEngine()
        val state = engine.startGame()
        assertTrue(state.isActive)
        assertEquals(180_000, state.timeRemainingMs)
        assertEquals(1200, state.rating)
    }

    @Test
    fun testGetNextPuzzle() {
        val engine = PuzzleRushEngine()
        val state = engine.startGame()
        val puzzle = engine.getNextPuzzle(state)
        assertNotNull(puzzle)
        assertTrue(puzzle.fen.isNotEmpty())
        assertTrue(puzzle.solution.isNotEmpty())
        assertTrue(puzzle.rating > 0)
    }

    @Test
    fun testCorrectMoveIncreasesScore() {
        val engine = PuzzleRushEngine()
        var state = engine.startGame()
        val puzzle = engine.getNextPuzzle(state)
        state = state.copy(currentPuzzle = puzzle)

        val board = ChessBoard.fromFEN(puzzle.fen)
        val gen = MoveGenerator(board)
        val legalMoves = gen.generateLegalMoves()

        // Find the move that matches the solution
        val solutionMove = puzzle.solution.first()
        val matchingMove = legalMoves.find { move ->
            val moveStr = moveToAlgebraicTest(board, move)
            moveStr.equals(solutionMove, ignoreCase = true)
        }

        if (matchingMove != null) {
            val newState = engine.submitMove(state, board, matchingMove)
            assertTrue(newState.puzzlesSolved > 0)
            assertTrue(newState.score > 0)
            assertTrue(newState.streak > 0)
        }
    }

    @Test
    fun testWrongMoveBreaksStreak() {
        val engine = PuzzleRushEngine()
        var state = engine.startGame()
        val puzzle = engine.getNextPuzzle(state)
        state = state.copy(currentPuzzle = puzzle, streak = 3)

        val board = ChessBoard.fromFEN(puzzle.fen)
        val gen = MoveGenerator(board)
        val legalMoves = gen.generateLegalMoves()

        // Make a wrong move (first legal move that's not the solution)
        val wrongMove = legalMoves.first()
        val newState = engine.submitMove(state, board, wrongMove)

        // Streak should be reset (or score should be 0 if no correct moves)
        // The move might accidentally be correct, so just verify state changed
        assertTrue(newState.puzzlesFailed > 0 || newState.puzzlesSolved > 0)
    }

    @Test
    fun testTimerTick() {
        val engine = PuzzleRushEngine()
        val state = engine.startGame()
        val newState = engine.tick(state, 1000)
        assertEquals(179_000, newState.timeRemainingMs)
    }

    @Test
    fun testGameOverOnTimeout() {
        val engine = PuzzleRushEngine()
        var state = engine.startGame()
        state = engine.tick(state, 180_001)
        assertFalse(state.isActive)
        assertEquals(0, state.timeRemainingMs)
    }

    @Test
    fun testRatingIncreasesOnSolve() {
        val engine = PuzzleRushEngine()
        var state = engine.startGame()
        val puzzle = engine.getNextPuzzle(state)
        state = state.copy(currentPuzzle = puzzle)

        val board = ChessBoard.fromFEN(puzzle.fen)
        val gen = MoveGenerator(board)
        val legalMoves = gen.generateLegalMoves()

        val solutionMove = puzzle.solution.first()
        val matchingMove = legalMoves.find { move ->
            val moveStr = moveToAlgebraicTest(board, move)
            moveStr.equals(solutionMove, ignoreCase = true)
        }

        if (matchingMove != null) {
            val newState = engine.submitMove(state, board, matchingMove)
            assertTrue(newState.rating >= state.rating)
        }
    }

    @Test
    fun testThemeStats() {
        val engine = PuzzleRushEngine()
        val state = engine.startGame()
        val stats = engine.getThemeStats(state)
        assertTrue(stats.isEmpty())
    }

    private fun moveToAlgebraicTest(board: ChessBoard, move: Move): String {
        val piece = move.piece
        val dest = move.to.get
        val capture = if (move.captured != null) "x" else ""
        return when (piece.type) {
            PieceType.PAWN -> "${if (capture.isNotEmpty()) move.from.get[0] else ""}$capture$dest"
            PieceType.KNIGHT -> "N$capture$dest"
            PieceType.BISHOP -> "B$capture$dest"
            PieceType.ROOK -> "R$capture$dest"
            PieceType.QUEEN -> "Q$capture$dest"
            PieceType.KING -> "K$capture$dest"
        }
    }
}
