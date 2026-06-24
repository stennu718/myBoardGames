package com.stennu718.myboardgames.feature.checkers.engine

enum class CheckersColor { RED, BLACK }

data class CheckersPiece(val color: CheckersColor, val isKing: Boolean = false)

class CheckersBoard {
    val board: Array<Array<CheckersPiece?>> = Array(8) { Array(8) { null } }
    var turn: CheckersColor = CheckersColor.RED

    init {
        for (row in 0..2) {
            for (col in 0..7) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = CheckersPiece(CheckersColor.BLACK)
                }
            }
        }
        for (row in 5..7) {
            for (col in 0..7) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = CheckersPiece(CheckersColor.RED)
                }
            }
        }
    }

    fun getPiece(row: Int, col: Int): CheckersPiece? {
        if (row !in 0..7 || col !in 0..7) return null
        return board[row][col]
    }

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val piece = getPiece(fromRow, fromCol) ?: return false
        if (piece.color != turn) return false
        if (getPiece(toRow, toCol) != null) return false

        val rowDiff = toRow - fromRow
        val colDiff = kotlin.math.abs(toCol - fromCol)

        // Simple move
        if (colDiff == 1 && !piece.isKing) {
            val validDir = if (piece.color == CheckersColor.RED) -1 else 1
            if (rowDiff != validDir) return false
        } else if (colDiff == 1 && piece.isKing) {
            if (kotlin.math.abs(rowDiff) != 1) return false
        } else if (colDiff == 2 && !piece.isKing) {
            // Jump
            val validDir = if (piece.color == CheckersColor.RED) -2 else 2
            if (rowDiff != validDir) return false
            val midRow = (fromRow + toRow) / 2
            val midCol = (fromCol + toCol) / 2
            val midPiece = getPiece(midRow, midCol) ?: return false
            if (midPiece.color == piece.color) return false
            board[midRow][midCol] = null
        } else {
            return false
        }

        board[toRow][toCol] = piece
        board[fromRow][fromCol] = null

        // King promotion
        if (toRow == 0 && piece.color == CheckersColor.RED) {
            board[toRow][toCol] = CheckersPiece(CheckersColor.RED, isKing = true)
        } else if (toRow == 7 && piece.color == CheckersColor.BLACK) {
            board[toRow][toCol] = CheckersPiece(CheckersColor.BLACK, isKing = true)
        }

        turn = if (turn == CheckersColor.RED) CheckersColor.BLACK else CheckersColor.RED
        return true
    }

    fun getValidMoves(color: CheckersColor): List<Triple<Int, Int, Int?>> {
        val moves = mutableListOf<Triple<Int, Int, Int?>>()
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board[row][col] ?: continue
                if (piece.color != color) continue
                val directions = if (piece.isKing) listOf(-1, 1) else {
                    if (piece.color == CheckersColor.RED) listOf(-1) else listOf(1)
                }
                for (dr in directions) {
                    for (dc in listOf(-1, 1)) {
                        val newRow = row + dr
                        val newCol = col + dc
                        if (newRow in 0..7 && newCol in 0..7 && board[newRow][newCol] == null) {
                            moves.add(Triple(row * 8 + col, newRow * 8 + newCol, null))
                        }
                        // Jump
                        val jumpRow = row + 2 * dr
                        val jumpCol = col + 2 * dc
                        if (jumpRow in 0..7 && jumpCol in 0..7 && board[jumpRow][jumpCol] == null) {
                            val midPiece = board[row + dr][col + dc]
                            if (midPiece != null && midPiece.color != color) {
                                moves.add(Triple(row * 8 + col, jumpRow * 8 + jumpCol, row * 8 + col + col))
                            }
                        }
                    }
                }
            }
        }
        return moves
    }

    fun isGameOver(): Boolean {
        return getValidMoves(CheckersColor.RED).isEmpty() || getValidMoves(CheckersColor.BLACK).isEmpty()
    }

    fun getWinner(): CheckersColor? {
        if (getValidMoves(CheckersColor.RED).isEmpty()) return CheckersColor.BLACK
        if (getValidMoves(CheckersColor.BLACK).isEmpty()) return CheckersColor.RED
        return null
    }
}