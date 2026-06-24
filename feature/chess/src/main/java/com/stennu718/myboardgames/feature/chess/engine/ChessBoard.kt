package com.stennu718.myboardgames.feature.chess.engine

enum class PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }
enum class Color { WHITE, BLACK }

data class Piece(val type: PieceType, val color: Color) {
    companion object {
        fun fromChar(c: Char): Piece? {
            val color = if (c.isUpperCase()) Color.WHITE else Color.BLACK
            val type = when (c.uppercaseChar()) {
                'K' -> PieceType.KING
                'Q' -> PieceType.QUEEN
                'R' -> PieceType.ROOK
                'B' -> PieceType.BISHOP
                'N' -> PieceType.KNIGHT
                'P' -> PieceType.PAWN
                else -> return null
            }
            return Piece(type, color)
        }
    }

    fun toChar(): Char {
        val c = when (type) {
            PieceType.KING -> 'K'
            PieceType.QUEEN -> 'Q'
            PieceType.ROOK -> 'R'
            PieceType.BISHOP -> 'B'
            PieceType.KNIGHT -> 'N'
            PieceType.PAWN -> 'P'
        }
        return if (color == Color.WHITE) c else c.lowercaseChar()
    }
}

data class Square(val row: Int, val col: Int) {
    val get: String get() = "${('a' + col)}${8 - row}"

    companion object {
        fun fromAlgebraic(algebraic: String): Square? {
            if (algebraic.length != 2) return null
            val col = algebraic[0] - 'a'
            val row = 8 - (algebra[1] - '0')
            if (row !in 0..7 || col !in 0..7) return null
            return Square(row, col)
        }

        fun fromIndex(index: Int): Square = Square(index / 8, index % 8)
    }

    fun toIndex(): Int = row * 8 + col
}

data class Move(
    val from: Square,
    val to: Square,
    val piece: Piece,
    val captured: Piece? = null,
    val promotion: PieceType? = null,
    val isCastle: Boolean = false,
    val isEnPassant: Boolean = false
)

class ChessBoard {
    val board: Array<Array<Piece?>> = Array(8) { Array(8) { null } }
    var turn: Color = Color.WHITE
    var whiteCanCastleKingSide: Boolean = true
    var whiteCanCastleQueenSide: Boolean = true
    var blackCanCastleKingSide: Boolean = true
    var blackCanCastleQueenSide: Boolean = true
    var enPassantTarget: Square? = null
    var halfMoveClock: Int = 0
    var fullMoveNumber: Int = 1

    init {
        setupInitialPosition()
    }

    private fun setupInitialPosition() {
        board[0][0] = Piece(PieceType.ROOK, Color.BLACK)
        board[0][1] = Piece(PieceType.KNIGHT, Color.BLACK)
        board[0][2] = Piece(PieceType.BISHOP, Color.BLACK)
        board[0][3] = Piece(PieceType.QUEEN, Color.BLACK)
        board[0][4] = Piece(PieceType.KING, Color.BLACK)
        board[0][5] = Piece(PieceType.BISHOP, Color.BLACK)
        board[0][6] = Piece(PieceType.KNIGHT, Color.BLACK)
        board[0][7] = Piece(PieceType.ROOK, Color.BLACK)
        for (col in 0..7) board[1][col] = Piece(PieceType.PAWN, Color.BLACK)

        board[7][0] = Piece(PieceType.ROOK, Color.WHITE)
        board[7][1] = Piece(PieceType.KNIGHT, Color.WHITE)
        board[7][2] = Piece(PieceType.BISHOP, Color.WHITE)
        board[7][3] = Piece(PieceType.QUEEN, Color.WHITE)
        board[7][4] = Piece(PieceType.KING, Color.WHITE)
        board[7][5] = Piece(PieceType.BISHOP, Color.WHITE)
        board[7][6] = Piece(PieceType.KNIGHT, Color.WHITE)
        board[7][7] = Piece(PieceType.ROOK, Color.WHITE)
        for (col in 0..7) board[6][col] = Piece(PieceType.PAWN, Color.WHITE)
    }

    fun getPiece(square: Square): Piece? {
        if (square.row !in 0..7 || square.col !in 0..7) return null
        return board[square.row][square.col]
    }

    fun setPiece(square: Square, piece: Piece?) {
        board[square.row][square.col] = piece
    }

    fun findKing(color: Color): Square {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board[row][col]
                if (piece?.type == PieceType.KING && piece.color == color) {
                    return Square(row, col)
                }
            }
        }
        throw IllegalStateException("King not found for $color")
    }

    fun toFEN(): String {
        val sb = StringBuilder()
        for (row in 0..7) {
            var empty = 0
            for (col in 0..7) {
                val piece = board[row][col]
                if (piece == null) {
                    empty++
                } else {
                    if (empty > 0) { sb.append(empty); empty = 0 }
                    sb.append(piece.toChar())
                }
            }
            if (empty > 0) sb.append(empty)
            if (row < 7) sb.append('/')
        }

        sb.append(' ')
        sb.append(if (turn == Color.WHITE) 'w' else 'b')

        sb.append(' ')
        val castle = buildString {
            if (whiteCanCastleKingSide) append('K')
            if (whiteCanCastleQueenSide) append('Q')
            if (blackCanCastleKingSide) append('k')
            if (blackCanCastleQueenSide) append('q')
        }
        sb.append(if (castle.isEmpty()) "-" else castle)

        sb.append(' ')
        sb.append(enPassantTarget?.get ?: "-")
        sb.append(' ')
        sb.append(halfMoveClock)
        sb.append(' ')
        sb.append(fullMoveNumber)

        return sb.toString()
    }

    companion object {
        fun fromFEN(fen: String): ChessBoard {
            val board = ChessBoard()
            val parts = fen.split(" ")
            val rows = parts[0].split('/')

            for (r in 0..7) for (c in 0..7) board[r][c] = null

            for ((rowIdx, rowStr) in rows.withIndex()) {
                var col = 0
                for (c in rowStr) {
                    if (c.isDigit()) {
                        col += c.digitToInt()
                    } else {
                        Piece.fromChar(c)?.let { board.board[rowIdx][col] = it }
                        col++
                    }
                }
            }

            board.turn = if (parts[1] == "w") Color.WHITE else Color.BLACK

            val castleStr = parts[2]
            board.whiteCanCastleKingSide = 'K' in castleStr
            board.whiteCanCastleQueenSide = 'Q' in castleStr
            board.blackCanCastleKingSide = 'k' in castleStr
            board.blackCanCastleQueenSide = 'q' in castleStr

            board.enPassantTarget = if (parts[3] != "-") Square.fromAlgebraic(parts[3]) else null
            board.halfMoveClock = parts[4].toIntOrNull() ?: 0
            board.fullMoveNumber = parts[5].toIntOrNull() ?: 1

            return board
        }
    }
}