package com.stennu718.myboardgames.feature.chess.engine

import kotlin.math.max
import kotlin.math.min

class ChessAI(var maxDepth: Int = 4) {

    private val pieceValues = mapOf(
        PieceType.PAWN to 100,
        PieceType.KNIGHT to 320,
        PieceType.BISHOP to 330,
        PieceType.ROOK to 500,
        PieceType.QUEEN to 900,
        PieceType.KING to 20000
    )

    private val pawnTable = intArrayOf(
         0,  0,  0,  0,  0,  0,  0,  0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
         5,  5, 10, 25, 25, 10,  5,  5,
         0,  0,  0, 20, 20,  0,  0,  0,
         5, -5,-10,  0,  0,-10, -5,  5,
         5, 10, 10,-20,-20, 10, 10,  5,
         0,  0,  0,  0,  0,  0,  0,  0
    )

    private val knightTable = intArrayOf(
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    )

    fun findBestMove(board: ChessBoard): Move? {
        val generator = MoveGenerator(board)
        val moves = generator.generateLegalMoves()
        if (moves.isEmpty()) return null

        var bestMove: Move? = null
        var bestScore = Int.MIN_VALUE

        val shuffled = moves.shuffled()
        for (move in shuffled) {
            val newBoard = board.simulateMove(move)
            val score = minimax(newBoard, maxDepth - 1, Int.MIN_VALUE, Int.MAX_VALUE, false)
            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    private fun minimax(board: ChessBoard, depth: Int, alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        val generator = MoveGenerator(board)

        if (depth == 0 || generator.isGameOver()) {
            return evaluate(board)
        }

        val moves = generator.generateLegalMoves()
        if (moves.isEmpty()) {
            if (generator.isCheckmate()) {
                return if (isMaximizing) -99999 else 99999
            }
            return 0
        }

        var a = alpha
        var b = beta

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            for (move in moves) {
                val newBoard = board.simulateMove(move)
                val eval = minimax(newBoard, depth - 1, a, b, false)
                maxEval = max(maxEval, eval)
                a = max(a, eval)
                if (b <= a) break
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            for (move in moves) {
                val newBoard = board.simulateMove(move)
                val eval = minimax(newBoard, depth - 1, a, b, true)
                minEval = min(minEval, eval)
                b = min(b, eval)
                if (b <= a) break
            }
            return minEval
        }
    }

    fun evaluate(board: ChessBoard): Int {
        var score = 0

        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board.board[r][c] ?: continue
                val materialValue = pieceValues[piece.type] ?: 0
                val positionalValue = getPositionalValue(piece, r, c)
                val totalValue = materialValue + positionalValue
                score += if (piece.color == Color.WHITE) totalValue else -totalValue
            }
        }

        return score
    }

    private fun getPositionalValue(piece: Piece, row: Int, col: Int): Int {
        val index = if (piece.color == Color.WHITE) row * 8 + col else (7 - row) * 8 + col
        return when (piece.type) {
            PieceType.PAWN -> pawnTable[index]
            PieceType.KNIGHT -> knightTable[index]
            else -> 0
        }
    }
}