package com.stennu718.myboardgames.feature.puzzlerush

import com.stennu718.myboardgames.feature.chess.engine.*
import kotlin.random.Random

data class Puzzle(
    val fen: String,
    val solution: List<String>,
    val rating: Int,
    val theme: String
)

data class PuzzleRushState(
    val currentPuzzle: Puzzle? = null,
    val puzzlesSolved: Int = 0,
    val puzzlesFailed: Int = 0,
    val score: Int = 0,
    val timeRemainingMs: Long = 180_000,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val isActive: Boolean = false,
    val rating: Int = 1200,
    val moveHistory: List<PuzzleAttempt> = emptyList()
)

data class PuzzleAttempt(
    val puzzle: Puzzle,
    val solved: Boolean,
    val timeTakenMs: Long,
    val movesMade: Int
)

class PuzzleRushEngine {

    private val puzzleDatabase = mutableListOf<Puzzle>()

    init {
        loadPuzzles()
    }

    private fun loadPuzzles() {
        puzzleDatabase.addAll(listOf(
            Puzzle("r1bqkb1r/pppp1ppp/2n2n2/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4", listOf("Qxf7#"), 800, "mateIn1"),
            Puzzle("rnbqkbnr/ppppp2p/5p2/6pQ/4P3/8/PPPP1PPP/RNB1KBNR w KQkq - 0 3", listOf("Qh5#"), 800, "mateIn1"),
            Puzzle("r1bqk2r/ppppbppp/2n2n2/4N3/4P3/2N5/PPPP1PPP/R1BQKB1R w KQkq - 0 5", listOf("Nd5"), 1000, "fork"),
            Puzzle("r1bqkb1r/pppp1ppp/2n2n2/4p3/2B1P3/3P1N2/PPP2PPP/RNBQK2R b KQkq - 0 4", listOf("Nxe4"), 900, "tactics"),
            Puzzle("rnbqkb1r/pp2pppp/5n2/2pp4/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 4", listOf("Bxd5"), 1100, "tactics"),
            Puzzle("r1bqkbnr/1ppp1ppp/p1n5/4p3/2B1P3/5Q2/PPPP1PPP/RNB1K1NR w KQkq - 0 4", listOf("Qxf7#"), 800, "mateIn1"),
            Puzzle("r1bqkb1r/pppppppp/2n2n2/8/3P4/2N5/PPP1PPPP/R1BQKBNR w KQkq - 0 3", listOf("Nd5"), 1200, "fork"),
            Puzzle("rnbqk2r/pppp1ppp/4pn2/8/1bPP4/2N5/PP2PPPP/R1BQKBNR w KQkq - 0 3", listOf("Bd2"), 1000, "development"),
            Puzzle("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 3", listOf("Bc4"), 900, "development"),
            Puzzle("r1bqkb1r/pppppppp/2n2n2/8/4P3/2N5/PPPP1PPP/R1BQKBNR w KQkq - 0 3", listOf("Nd5"), 1100, "fork"),
            Puzzle("rnbqkb1r/pppppppp/5n2/8/4P3/2N5/PPPP1PPP/R1BQKBNR w KQkq - 0 3", listOf("Nd5"), 1200, "fork"),
            Puzzle("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 4", listOf("O-O"), 900, "castling"),
            Puzzle("r1bqkb1r/pppp1ppp/2n2n2/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 3", listOf("Nxe4"), 1000, "tactics"),
            Puzzle("rnbqkbnr/ppp1pppp/8/3p4/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 2", listOf("c4"), 800, "opening"),
            Puzzle("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 3", listOf("Nf6"), 900, "development"),
            Puzzle("r1bqkb1r/pppppppp/2n2n2/8/3P4/5N2/PPP1PPPP/R1BQKB1R b KQkq - 0 3", listOf("e6"), 1000, "opening"),
            Puzzle("rnbqkb1r/pppppppp/5n2/8/3P4/2N5/PPP1PPPP/R1BQKBNR b KQkq - 0 3", listOf("Nbd7"), 1100, "development"),
            Puzzle("r1bqkbnr/pppp1ppp/2n5/4p3/3PP3/5N2/PPP2PPP/RNBQKB1R w KQkq - 0 3", listOf("d4"), 900, "center"),
            Puzzle("r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 4", listOf("Nd5"), 1200, "fork"),
            Puzzle("rnbqk2r/ppp2pbp/3p1np1/4p3/3PPP2/2N2N2/PPP2PPP/R1BQKB1R w KQkq - 0 4", listOf("Bg5"), 1300, "pin")
        ))
    }

    fun startGame(): PuzzleRushState {
        return PuzzleRushState(isActive = true, timeRemainingMs = 180_000, rating = 1200)
    }

    fun getNextPuzzle(state: PuzzleRushState): Puzzle {
        val targetRating = state.rating
        val sorted = puzzleDatabase.sortedBy { kotlin.math.abs(it.rating - targetRating) }
        return sorted.take(5).random()
    }

    fun submitMove(state: PuzzleRushState, board: ChessBoard, move: Move): PuzzleRushState {
        val puzzle = state.currentPuzzle ?: return state
        val expectedMove = puzzle.solution.firstOrNull() ?: return state
        val moveStr = moveToAlgebraic(board, move)
        val isCorrect = moveStr.equals(expectedMove, ignoreCase = true) || isEquivalentMove(board, move, expectedMove)

        if (isCorrect) {
            val timeBonus = (state.timeRemainingMs / 1000).toInt().coerceAtMost(30)
            val streakBonus = state.streak * 10
            val puzzlePoints = (puzzle.rating / 100) + timeBonus + streakBonus
            val newRating = calculateNewRating(state.rating, puzzle.rating, true)

            return state.copy(
                puzzlesSolved = state.puzzlesSolved + 1,
                score = state.score + puzzlePoints,
                streak = state.streak + 1,
                bestStreak = maxOf(state.bestStreak, state.streak + 1),
                rating = newRating,
                currentPuzzle = null,
                moveHistory = state.moveHistory + PuzzleAttempt(puzzle, true, 0, 1)
            )
        } else {
            return state.copy(
                puzzlesFailed = state.puzzlesFailed + 1,
                streak = 0,
                currentPuzzle = null,
                moveHistory = state.moveHistory + PuzzleAttempt(puzzle, false, 0, 1)
            )
        }
    }

    fun tick(state: PuzzleRushState, deltaMs: Long): PuzzleRushState {
        if (!state.isActive) return state
        val newTime = state.timeRemainingMs - deltaMs
        if (newTime <= 0) return state.copy(timeRemainingMs = 0, isActive = false)
        return state.copy(timeRemainingMs = newTime)
    }

    private fun calculateNewRating(currentRating: Int, puzzleRating: Int, solved: Boolean): Int {
        val kFactor = 32
        val expected = 1.0 / (1.0 + Math.pow(10.0, (puzzleRating - currentRating) / 400.0))
        val actual = if (solved) 1.0 else 0.0
        return (currentRating + kFactor * (actual - expected)).toInt()
    }

    private fun moveToAlgebraic(board: ChessBoard, move: Move): String {
        val piece = move.piece
        val dest = move.to.get
        val capture = if (move.captured != null) "x" else ""
        return when (piece.type) {
            PieceType.PAWN -> "${if (capture.isNotEmpty()) move.from.get[0] else ""}$capture${dest}${if (move.promotion != null) "=${move.promotion}" else ""}"
            PieceType.KNIGHT -> "N$capture$dest"
            PieceType.BISHOP -> "B$capture$dest"
            PieceType.ROOK -> "R$capture$dest"
            PieceType.QUEEN -> "Q$capture$dest"
            PieceType.KING -> "K$capture$dest"
        }
    }

    private fun isEquivalentMove(board: ChessBoard, move: Move, expected: String): Boolean {
        return moveToAlgebraic(board, move).equals(expected, ignoreCase = true)
    }

    fun getThemeStats(state: PuzzleRushState): Map<String, Pair<Int, Int>> {
        return state.moveHistory.groupBy { it.puzzle.theme }
            .mapValues { (_, attempts) ->
                Pair(attempts.count { it.solved }, attempts.count { !it.solved })
            }
    }
}
