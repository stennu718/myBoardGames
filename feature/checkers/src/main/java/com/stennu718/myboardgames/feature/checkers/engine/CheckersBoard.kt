package com.stennu718.myboardgames.feature.checkers.engine

enum class CheckersColor { RED, BLACK }

data class CheckersPiece(val color: CheckersColor, val isKing: Boolean = false)

class CheckersBoard {
    val board: Array<Array<CheckersPiece?>> = Array(8) { Array(8) { null } }
    var turn: CheckersColor = CheckersColor.RED
    var isInMultiJump: Boolean = false
    var multiJumpPieceRow: Int = -1
    var multiJumpPieceCol: Int = -1

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

    private fun getJumpsForPiece(row: Int, col: Int): List<Pair<Int, Int>> {
        val piece = getPiece(row, col) ?: return emptyList()
        val jumps = mutableListOf<Pair<Int, Int>>()
        val directions = if (piece.isKing) listOf(-1, 1) else {
            if (piece.color == CheckersColor.RED) listOf(-1) else listOf(1)
        }
        for (dr in directions) {
            for (dc in listOf(-1, 1)) {
                val jumpRow = row + 2 * dr
                val jumpCol = col + 2 * dc
                if (jumpRow in 0..7 && jumpCol in 0..7 && board[jumpRow][jumpCol] == null) {
                    val midPiece = board[row + dr][col + dc]
                    if (midPiece != null && midPiece.color != piece.color) {
                        jumps.add(Pair(jumpRow, jumpCol))
                    }
                }
            }
        }
        return jumps
    }

    fun hasJumpsAvailable(color: CheckersColor): Boolean {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board[row][col] ?: continue
                if (piece.color != color) continue
                if (getJumpsForPiece(row, col).isNotEmpty()) return true
            }
        }
        return false
    }

    fun canMultiJump(row: Int, col: Int): Boolean {
        return getJumpsForPiece(row, col).isNotEmpty()
    }

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val piece = getPiece(fromRow, fromCol) ?: return false
        if (piece.color != turn) return false

        // If in multi-jump, only allow the same piece to continue jumping
        if (isInMultiJump && (fromRow != multiJumpPieceRow || fromCol != multiJumpPieceCol)) {
            return false
        }

        if (getPiece(toRow, toCol) != null) return false

        val rowDiff = toRow - fromRow
        val colDiff = kotlin.math.abs(toCol - fromCol)
        val isJumpMove: Boolean

        // Simple move
        if (colDiff == 1 && !piece.isKing) {
            val validDir = if (piece.color == CheckersColor.RED) -1 else 1
            if (rowDiff != validDir) return false
            isJumpMove = false
        } else if (colDiff == 1 && piece.isKing) {
            if (kotlin.math.abs(rowDiff) != 1) return false
            isJumpMove = false
        } else if (colDiff == 2 && !piece.isKing) {
            // Jump
            val validDir = if (piece.color == CheckersColor.RED) -2 else 2
            if (rowDiff != validDir) return false
            val midRow = (fromRow + toRow) / 2
            val midCol = (fromCol + toCol) / 2
            val midPiece = getPiece(midRow, midCol) ?: return false
            if (midPiece.color == piece.color) return false
            board[midRow][midCol] = null
            isJumpMove = true
        } else if (colDiff == 2 && piece.isKing) {
            if (kotlin.math.abs(rowDiff) != 2) return false
            val midRow = (fromRow + toRow) / 2
            val midCol = (fromCol + toCol) / 2
            val midPiece = getPiece(midRow, midCol) ?: return false
            if (midPiece.color == piece.color) return false
            board[midRow][midCol] = null
            isJumpMove = true
        } else {
            return false
        }

        // Mandatory jump check: if jumps are available and this is not a jump, reject
        if (!isJumpMove && hasJumpsAvailable(turn)) {
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

        // Multi-jump: if this was a jump and the piece can jump again, continue turn
        if (isJumpMove && canMultiJump(toRow, toCol)) {
            isInMultiJump = true
            multiJumpPieceRow = toRow
            multiJumpPieceCol = toCol
            // Don't switch turns - same player continues with the same piece
        } else {
            isInMultiJump = false
            multiJumpPieceRow = -1
            multiJumpPieceCol = -1
            turn = if (turn == CheckersColor.RED) CheckersColor.BLACK else CheckersColor.RED
        }

        return true
    }

    fun getValidMoves(color: CheckersColor): List<Triple<Int, Int, Int?>> {
        val moves = mutableListOf<Triple<Int, Int, Int?>>()
        val jumps = mutableListOf<Triple<Int, Int, Int?>>()

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
                                // Fix: return captured piece's index, not from square
                                jumps.add(Triple(row * 8 + col, jumpRow * 8 + jumpCol, (row + dr) * 8 + (col + dc)))
                            }
                        }
                    }
                }
            }
        }

        // If jumps are available, only return jumps (mandatory jump rule)
        if (jumps.isNotEmpty()) return jumps
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
