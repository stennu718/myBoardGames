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

    // Piece-square tables (from White's perspective, index = row*8+col)
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

    private val bishopTable = intArrayOf(
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20
    )

    private val rookTable = intArrayOf(
         0,  0,  0,  0,  0,  0,  0,  0,
         5, 10, 10, 10, 10, 10, 10,  5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
         0,  0,  0,  5,  5,  0,  0,  0
    )

    private val queenTable = intArrayOf(
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
         -5,  0,  5,  5,  5,  5,  0, -5,
          0,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20
    )

    private val kingTableOpening = intArrayOf(
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -20,-30,-30,-40,-40,-30,-30,-20,
        -10,-20,-20,-20,-20,-20,-20,-10,
         20, 20,  0,  0,  0,  0, 20, 20,
         20, 30, 10,  0,  0, 10, 30, 20
    )

    private val kingTableEndgame = intArrayOf(
        -50,-40,-30,-20,-20,-30,-40,-50,
        -30,-20,-10,  0,  0,-10,-20,-30,
        -30,-10, 20, 30, 30, 20,-10,-30,
        -30,-10, 30, 40, 40, 30,-10,-30,
        -30,-10, 30, 40, 40, 30,-10,-30,
        -30,-10, 20, 30, 30, 20,-10,-30,
        -30,-30,  0,  0,  0,  0,-30,-30,
        -50,-30,-30,-30,-30,-30,-30,-50
    )

    private var nodesSearched = 0

    fun findBestMove(board: ChessBoard): Move? {
        val generator = MoveGenerator(board)
        val moves = generator.generateLegalMoves()
        if (moves.isEmpty()) return null

        nodesSearched = 0
        var bestMove: Move? = null
        var bestScore = Int.MIN_VALUE

        // Move ordering: evaluate captures first for better pruning
        val orderedMoves = orderMoves(moves)

        for (move in orderedMoves) {
            val newBoard = board.simulateMove(move)
            val score = minimax(newBoard, maxDepth - 1, Int.MIN_VALUE, Int.MAX_VALUE, false)
            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    fun getNodesSearched(): Int = nodesSearched

    private fun orderMoves(moves: List<Move>): List<Move> {
        return moves.sortedByDescending { move ->
            var score = 0
            if (move.captured != null) {
                score += 10 * (pieceValues[move.captured.type] ?: 0) - (pieceValues[move.piece.type] ?: 0)
            }
            if (move.promotion != null) {
                score += pieceValues[move.promotion] ?: 0
            }
            if (move.isCastle) score += 50
            score
        }
    }

    private fun minimax(board: ChessBoard, depth: Int, alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        val generator = MoveGenerator(board)
        nodesSearched++

        if (depth == 0) {
            return quiescenceSearch(board, alpha, beta, isMaximizing)
        }

        val moves = generator.generateLegalMoves()
        if (moves.isEmpty()) {
            if (generator.isCheckmate()) {
                // Prefer faster mate: add depth to encourage quicker wins
                return if (isMaximizing) -99999 + (maxDepth - depth) else 99999 - (maxDepth - depth)
            }
            return 0 // Stalemate
        }

        // Check draw by 50-move rule
        if (board.halfMoveClock >= 100) return 0

        val orderedMoves = orderMoves(moves)

        var a = alpha
        var b = beta

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            for (move in orderedMoves) {
                val newBoard = board.simulateMove(move)
                val eval = minimax(newBoard, depth - 1, a, b, false)
                maxEval = max(maxEval, eval)
                a = max(a, eval)
                if (b <= a) break
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            for (move in orderedMoves) {
                val newBoard = board.simulateMove(move)
                val eval = minimax(newBoard, depth - 1, a, b, true)
                minEval = min(minEval, eval)
                b = min(b, eval)
                if (b <= a) break
            }
            return minEval
        }
    }

    /**
     * Quiescence search: keep searching until position is quiet (no captures)
     * Prevents the horizon effect where AI misses obvious captures
     */
    private fun quiescenceSearch(board: ChessBoard, alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        val generator = MoveGenerator(board)
        val standPat = evaluate(board)

        if (isMaximizing) {
            if (standPat >= beta) return beta
            val newAlpha = max(alpha, standPat)
            val captureMoves = generator.generatePseudoLegalMoves(board.turn)
                .filter { it.captured != null || it.promotion != null }
                .let { orderMoves(it) }

            if (captureMoves.isEmpty()) return standPat

            var a = newAlpha
            for (move in captureMoves) {
                val newBoard = board.simulateMove(move)
                if (newBoard.isInCheck(board.turn)) continue
                val eval = quiescenceSearch(newBoard, a, beta, false)
                a = max(a, eval)
                if (a >= beta) break
            }
            return a
        } else {
            if (standPat <= alpha) return alpha
            val newBeta = min(beta, standPat)
            val captureMoves = generator.generatePseudoLegalMoves(board.turn)
                .filter { it.captured != null || it.promotion != null }
                .let { orderMoves(it) }

            if (captureMoves.isEmpty()) return standPat

            var b = newBeta
            for (move in captureMoves) {
                val newBoard = board.simulateMove(move)
                if (newBoard.isInCheck(board.turn)) continue
                val eval = quiescenceSearch(newBoard, alpha, b, true)
                b = min(b, eval)
                if (b <= alpha) break
            }
            return b
        }
    }

    fun evaluate(board: ChessBoard): Int {
        var score = 0
        var whiteBishops = 0
        var blackBishops = 0
        var isEndgame = false

        // Count material for endgame detection
        var whiteMinorMajor = 0
        var blackMinorMajor = 0

        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board.board[r][c] ?: continue
                when (piece.type) {
                    PieceType.BISHOP -> if (piece.color == Color.WHITE) whiteBishops++ else blackBishops
                    PieceType.KNIGHT, PieceType.ROOK, PieceType.QUEEN -> {
                        if (piece.color == Color.WHITE) whiteMinorMajor++ else blackMinorMajor++
                    }
                    else -> {}
                }
            }
        }

        // Endgame: no queens, or queens with at most 1 minor/major piece each
        isEndgame = (whiteMinorMajor <= 1 && blackMinorMajor <= 1)

        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board.board[r][c] ?: continue
                val materialValue = pieceValues[piece.type] ?: 0
                val positionalValue = getPositionalValue(piece, r, c, isEndgame)
                val mobilityValue = getMobilityBonus(board, piece, r, c)
                val totalValue = materialValue + positionalValue + mobilityValue
                score += if (piece.color == Color.WHITE) totalValue else -totalValue
            }
        }

        // Bishop pair bonus
        if (whiteBishops >= 2) score += 30
        if (blackBishops >= 2) score -= 30

        return score
    }

    private fun getMobilityBonus(board: ChessBoard, piece: Piece, row: Int, col: Int): Int {
        // Simple mobility bonus for knights and bishops
        return when (piece.type) {
            PieceType.KNIGHT -> {
                var count = 0
                val offsets = intArrayOf(-2, -1, 1, 2)
                for (dr in offsets) for (dc in offsets) {
                    if (kotlin.math.abs(dr) + kotlin.math.abs(dc) == 3) {
                        val nr = row + dr
                        val nc = col + dc
                        if (nr in 0..7 && nc in 0..7) count++
                    }
                }
                count * 2
            }
            PieceType.BISHOP -> {
                // Bonus for not being blocked at start
                if ((row == 7 || row == 6) && (col == 2 || col == 5)) -20
                else 0
            }
            else -> 0
        }
    }

    private fun getPositionalValue(piece: Piece, row: Int, col: Int, isEndgame: Boolean): Int {
        // Flip index for black pieces (tables are from White's perspective)
        val index = if (piece.color == Color.WHITE) (7 - row) * 8 + col else row * 8 + col
        return when (piece.type) {
            PieceType.PAWN -> pawnTable[index]
            PieceType.KNIGHT -> knightTable[index]
            PieceType.BISHOP -> bishopTable[index]
            PieceType.ROOK -> rookTable[index]
            PieceType.QUEEN -> queenTable[index]
            PieceType.KING -> {
                if (isEndgame) kingTableEndgame[index] else kingTableOpening[index]
            }
        }
    }
}
