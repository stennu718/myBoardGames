package com.stennu718.myboardgames.feature.chess.engine

class MoveGenerator(private val board: ChessBoard) {

    fun generateLegalMoves(): List<Move> {
        val pseudoLegal = generatePseudoLegalMoves(board.turn)
        return pseudoLegal.filter { move ->
            val newBoard = board.simulateMove(move)
            !newBoard.isInCheck(board.turn)
        }
    }

    fun generatePseudoLegalMoves(color: Color): List<Move> {
        val moves = mutableListOf<Move>()
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board.board[row][col] ?: continue
                if (piece.color != color) continue
                val from = Square(row, col)
                when (piece.type) {
                    PieceType.PAWN -> generatePawnMoves(from, piece, moves)
                    PieceType.KNIGHT -> generateKnightMoves(from, piece, moves)
                    PieceType.BISHOP -> generateSlidingMoves(from, piece, listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1), moves)
                    PieceType.ROOK -> generateSlidingMoves(from, piece, listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1), moves)
                    PieceType.QUEEN -> generateSlidingMoves(from, piece, listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1, -1 to 0, 1 to 0, 0 to -1, 0 to 1), moves)
                    PieceType.KING -> generateKingMoves(from, piece, moves)
                }
            }
        }
        return moves
    }

    private fun generatePawnMoves(from: Square, piece: Piece, moves: MutableList<Move>) {
        val direction = if (piece.color == Color.WHITE) -1 else 1
        val startRow = if (piece.color == Color.WHITE) 6 else 1
        val promotionRow = if (piece.color == Color.WHITE) 0 else 7

        // Single push
        val singleRow = from.row + direction
        if (singleRow in 0..7 && board.board[singleRow][from.col] == null) {
            val to = Square(singleRow, from.col)
            if (singleRow == promotionRow) {
                listOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT).forEach {
                    moves.add(Move(from, to, piece, promotion = it))
                }
            } else {
                moves.add(Move(from, to, piece))
            }

            // Double push
            val doubleRow = from.row + 2 * direction
            if (from.row == startRow && board.board[doubleRow][from.col] == null) {
                moves.add(Move(from, Square(doubleRow, from.col), piece))
            }
        }

        // Captures
        for (dc in listOf(-1, 1)) {
            val toCol = from.col + dc
            val toRow = from.row + direction
            if (toCol !in 0..7 || toRow !in 0..7) continue
            val to = Square(toRow, toCol)
            val target = board.board[toRow][toCol]
            if (target != null && target.color != piece.color) {
                if (toRow == promotionRow) {
                    listOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT).forEach {
                        moves.add(Move(from, to, piece, captured = target, promotion = it))
                    }
                } else {
                    moves.add(Move(from, to, piece, captured = target))
                }
            }

            // En passant
            if (board.enPassantTarget == to) {
                val capturedPawn = board.board[from.row][toCol]
                moves.add(Move(from, to, piece, captured = capturedPawn, isEnPassant = true))
            }
        }
    }

    private fun generateKnightMoves(from: Square, piece: Piece, moves: MutableList<Move>) {
        val offsets = listOf(-2 to -1, -2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2, 2 to -1, 2 to 1)
        for ((dr, dc) in offsets) {
            val toRow = from.row + dr
            val toCol = from.col + dc
            if (toRow !in 0..7 || toCol !in 0..7) continue
            val to = Square(toRow, toCol)
            val target = board.board[toRow][toCol]
            if (target == null) {
                moves.add(Move(from, to, piece))
            } else if (target.color != piece.color) {
                moves.add(Move(from, to, piece, captured = target))
            }
        }
    }

    private fun generateSlidingMoves(from: Square, piece: Piece, directions: List<Pair<Int, Int>>, moves: MutableList<Move>) {
        for ((dr, dc) in directions) {
            var r = from.row + dr
            var c = from.col + dc
            while (r in 0..7 && c in 0..7) {
                val to = Square(r, c)
                val target = board.board[r][c]
                if (target == null) {
                    moves.add(Move(from, to, piece))
                } else {
                    if (target.color != piece.color) {
                        moves.add(Move(from, to, piece, captured = target))
                    }
                    break
                }
                r += dr
                c += dc
            }
        }
    }

    private fun generateKingMoves(from: Square, piece: Piece, moves: MutableList<Move>) {
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val toRow = from.row + dr
                val toCol = from.col + dc
                if (toRow !in 0..7 || toCol !in 0..7) continue
                val to = Square(toRow, toCol)
                val target = board.board[toRow][toCol]
                if (target == null) {
                    moves.add(Move(from, to, piece))
                } else if (target.color != piece.color) {
                    moves.add(Move(from, to, piece, captured = target))
                }
            }
        }

        // Castling
        val row = from.row
        if (piece.color == Color.WHITE && row == 7) {
            if (board.whiteCanCastleKingSide &&
                board.board[7][5] == null && board.board[7][6] == null &&
                board.board[7][7] == Piece(PieceType.ROOK, Color.WHITE) &&
                !board.isSquareAttacked(Square(7, 4), Color.BLACK) &&
                !board.isSquareAttacked(Square(7, 5), Color.BLACK) &&
                !board.isSquareAttacked(Square(7, 6), Color.BLACK)) {
                moves.add(Move(from, Square(7, 6), piece, isCastle = true))
            }
            if (board.whiteCanCastleQueenSide &&
                board.board[7][3] == null && board.board[7][2] == null && board.board[7][1] == null &&
                board.board[7][0] == Piece(PieceType.ROOK, Color.WHITE) &&
                !board.isSquareAttacked(Square(7, 4), Color.BLACK) &&
                !board.isSquareAttacked(Square(7, 3), Color.BLACK) &&
                !board.isSquareAttacked(Square(7, 2), Color.BLACK)) {
                moves.add(Move(from, Square(7, 2), piece, isCastle = true))
            }
        } else if (piece.color == Color.BLACK && row == 0) {
            if (board.blackCanCastleKingSide &&
                board.board[0][5] == null && board.board[0][6] == null &&
                board.board[0][7] == Piece(PieceType.ROOK, Color.BLACK) &&
                !board.isSquareAttacked(Square(0, 4), Color.WHITE) &&
                !board.isSquareAttacked(Square(0, 5), Color.WHITE) &&
                !board.isSquareAttacked(Square(0, 6), Color.WHITE)) {
                moves.add(Move(from, Square(0, 6), piece, isCastle = true))
            }
            if (board.blackCanCastleQueenSide &&
                board.board[0][3] == null && board.board[0][2] == null && board.board[0][1] == null &&
                board.board[0][0] == Piece(PieceType.ROOK, Color.BLACK) &&
                !board.isSquareAttacked(Square(0, 4), Color.WHITE) &&
                !board.isSquareAttacked(Square(0, 3), Color.WHITE) &&
                !board.isSquareAttacked(Square(0, 2), Color.WHITE)) {
                moves.add(Move(from, Square(0, 2), piece, isCastle = true))
            }
        }
    }

    fun isInCheck(color: Color): Boolean {
        val kingSquare = board.findKing(color) ?: return false
        val opponent = if (color == Color.WHITE) Color.BLACK else Color.WHITE
        return board.isSquareAttacked(kingSquare, opponent)
    }

    fun isCheckmate(): Boolean {
        return isInCheck(board.turn) && generateLegalMoves().isEmpty()
    }

    fun isStalemate(): Boolean {
        return !isInCheck(board.turn) && generateLegalMoves().isEmpty()
    }

    fun isGameOver(): Boolean {
        return generateLegalMoves().isEmpty() || halfMoveClock >= 100 || isInsufficientMaterial()
    }

    private fun isInsufficientMaterial(): Boolean {
        val pieces = mutableListOf<Piece>()
        for (r in 0..7) for (c in 0..7) {
            board.board[r][c]?.let { if (it.type != PieceType.KING) pieces.add(it) }
        }
        return when (pieces.size) {
            0 -> true // K vs K
            1 -> pieces[0].type == PieceType.BISHOP || pieces[0].type == PieceType.KNIGHT // K+minor vs K
            2 -> pieces.all { it.type == PieceType.BISHOP } && pieces.map { (it.color.ordinal + (it.row + it.col) % 2) % 2 }.toSet().size == 1
            else -> false
        }
    }

    private val halfMoveClock: Int get() = board.halfMoveClock
}

fun ChessBoard.simulateMove(move: Move): ChessBoard {
    val newBoard = this.copy()
    newBoard.setPiece(move.from, null)
    val placedPiece = if (move.promotion != null) Piece(move.promotion, move.piece.color) else move.piece
    newBoard.setPiece(move.to, placedPiece)

    if (move.isEnPassant) {
        newBoard.setPiece(Square(move.from.row, move.to.col), null)
    }

    if (move.isCastle) {
        val row = move.from.row
        if (move.to.col == 6) {
            newBoard.setPiece(Square(row, 5), newBoard.board[row][7])
            newBoard.setPiece(Square(row, 7), null)
        } else if (move.to.col == 2) {
            newBoard.setPiece(Square(row, 3), newBoard.board[row][0])
            newBoard.setPiece(Square(row, 0), null)
        }
    }

    newBoard.turn = if (move.piece.color == Color.WHITE) Color.BLACK else Color.WHITE

    return newBoard
}

fun ChessBoard.isSquareAttacked(square: Square, byColor: Color): Boolean {
    for (r in 0..7) {
        for (c in 0..7) {
            val piece = board[r][c] ?: continue
            if (piece.color != byColor) continue
            val from = Square(r, c)
            when (piece.type) {
                PieceType.PAWN -> {
                    val direction = if (piece.color == Color.WHITE) -1 else 1
                    if (r + direction == square.row && (c - 1 == square.col || c + 1 == square.col)) return true
                }
                PieceType.KNIGHT -> {
                    val dr = kotlin.math.abs(r - square.row)
                    val dc = kotlin.math.abs(c - square.col)
                    if ((dr == 2 && dc == 1) || (dr == 1 && dc == 2)) return true
                }
                PieceType.BISHOP -> {
                    if (kotlin.math.abs(r - square.row) == kotlin.math.abs(c - square.col)) {
                        if (isPathClear(from, square, board)) return true
                    }
                }
                PieceType.ROOK -> {
                    if (r == square.row || c == square.col) {
                        if (isPathClear(from, square, board)) return true
                    }
                }
                PieceType.QUEEN -> {
                    if (r == square.row || c == square.col || kotlin.math.abs(r - square.row) == kotlin.math.abs(c - square.col)) {
                        if (isPathClear(from, square, board)) return true
                    }
                }
                PieceType.KING -> {
                    if (kotlin.math.abs(r - square.row) <= 1 && kotlin.math.abs(c - square.col) <= 1) return true
                }
            }
        }
    }
    return false
}

private fun isPathClear(from: Square, to: Square, board: Array<Array<Piece?>>): Boolean {
    val dr = (to.row - from.row).sign
    val dc = (to.col - from.col).sign
    var r = from.row + dr
    var c = from.col + dc
    while (r != to.row || c != to.col) {
        if (board[r][c] != null) return false
        r += dr
        c += dc
    }
    return true
}

fun ChessBoard.copy(): ChessBoard {
    val newBoard = ChessBoard()
    for (r in 0..7) {
        for (c in 0..7) {
            newBoard.board[r][c] = this.board[r][c]
        }
    }
    newBoard.turn = this.turn
    newBoard.whiteCanCastleKingSide = this.whiteCanCastleKingSide
    newBoard.whiteCanCastleQueenSide = this.whiteCanCastleQueenSide
    newBoard.blackCanCastleKingSide = this.blackCanCastleKingSide
    newBoard.blackCanCastleQueenSide = this.blackCanCastleQueenSide
    newBoard.enPassantTarget = this.enPassantTarget
    newBoard.halfMoveClock = this.halfMoveClock
    newBoard.fullMoveNumber = this.fullMoveNumber
    return newBoard
}