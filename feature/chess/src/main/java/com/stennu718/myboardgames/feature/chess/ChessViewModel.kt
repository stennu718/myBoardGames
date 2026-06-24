package com.stennu718.myboardgames.feature.chess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stennu718.myboardgames.feature.chess.engine.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChessUiState(
    val board: ChessBoard = ChessBoard(),
    val selectedSquare: Square? = null,
    val legalMoves: List<Move> = emptyList(),
    val lastMove: Move? = null,
    val isThinking: Boolean = false,
    val gameStatus: String = "",
    val moveHistory: List<Move> = emptyList(),
    val difficulty: Int = 3
)

class ChessViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChessUiState())
    val uiState: StateFlow<ChessUiState> = _uiState.asStateFlow()

    private val ai = ChessAI(3)

    fun onSquareClick(square: Square) {
        val state = _uiState.value
        val piece = state.board.getPiece(square)

        if (state.selectedSquare != null) {
            val move = state.legalMoves.find { it.to == square }
            if (move != null) {
                makeMove(move)
                return
            }
            if (piece != null && piece.color == state.board.turn) {
                selectSquare(square)
                return
            }
            deselect()
        } else {
            if (piece != null && piece.color == state.board.turn) {
                selectSquare(square)
            }
        }
    }

    private fun selectSquare(square: Square) {
        val state = _uiState.value
        val piece = state.board.getPiece(square) ?: return
        val moveGenerator = MoveGenerator(state.board)
        val allMoves = moveGenerator.generateLegalMoves()
        val pieceMoves = allMoves.filter { it.from == square }

        _uiState.value = state.copy(
            selectedSquare = square,
            legalMoves = pieceMoves
        )
    }

    private fun deselect() {
        _uiState.value = _uiState.value.copy(
            selectedSquare = null,
            legalMoves = emptyList()
        )
    }

    private fun makeMove(move: Move) {
        val state = _uiState.value
        val newBoard = state.board.simulateMove(move)

        updateCastlingRights(newBoard, move)

        newBoard.enPassantTarget = if (move.piece.type == PieceType.PAWN &&
            kotlin.math.abs(move.to.row - move.from.row) == 2) {
            Square((move.from.row + move.to.row) / 2, move.from.col)
        } else null

        newBoard.halfMoveClock = if (move.piece.type == PieceType.PAWN || move.captured != null) 0
        else state.board.halfMoveClock + 1
        if (newBoard.turn == Color.WHITE) newBoard.fullMoveNumber++

        newBoard.turn = if (newBoard.turn == Color.WHITE) Color.BLACK else Color.WHITE

        val updatedHistory = state.moveHistory + move
        val status = getGameStatus(newBoard)

        _uiState.value = state.copy(
            board = newBoard,
            selectedSquare = null,
            legalMoves = emptyList(),
            lastMove = move,
            moveHistory = updatedHistory,
            gameStatus = status
        )

        if (newBoard.turn == Color.BLACK && !status.contains("Checkmate") && !status.contains("Stalemate")) {
            makeAIMove(newBoard)
        }
    }

    private fun makeAIMove(board: ChessBoard) {
        _uiState.value = _uiState.value.copy(isThinking = true)
        viewModelScope.launch {
            val aiMove = ai.findBestMove(board)
            if (aiMove != null) {
                val newBoard = board.simulateMove(aiMove)
                updateCastlingRights(newBoard, aiMove)
                newBoard.enPassantTarget = if (aiMove.piece.type == PieceType.PAWN &&
                    kotlin.math.abs(aiMove.to.row - aiMove.from.row) == 2) {
                    Square((aiMove.from.row + aiMove.to.row) / 2, aiMove.from.col)
                } else null
                newBoard.halfMoveClock = if (aiMove.piece.type == PieceType.PAWN || aiMove.captured != null) 0
                else board.halfMoveClock + 1
                if (newBoard.turn == Color.WHITE) newBoard.fullMoveNumber++
                newBoard.turn = if (newBoard.turn == Color.WHITE) Color.BLACK else Color.WHITE

                val updatedHistory = _uiState.value.moveHistory + aiMove
                val status = getGameStatus(newBoard)

                _uiState.value = _uiState.value.copy(
                    board = newBoard,
                    lastMove = aiMove,
                    moveHistory = updatedHistory,
                    isThinking = false,
                    gameStatus = status
                )
            } else {
                _uiState.value = _uiState.value.copy(isThinking = false)
            }
        }
    }

    private fun updateCastlingRights(board: ChessBoard, move: Move) {
        if (move.piece.type == PieceType.KING) {
            if (move.piece.color == Color.WHITE) {
                board.whiteCanCastleKingSide = false
                board.whiteCanCastleQueenSide = false
            } else {
                board.blackCanCastleKingSide = false
                board.blackCanCastleQueenSide = false
            }
        }
        if (move.piece.type == PieceType.ROOK) {
            if (move.from == Square(7, 0)) board.whiteCanCastleQueenSide = false
            if (move.from == Square(7, 7)) board.whiteCanCastleKingSide = false
            if (move.from == Square(0, 0)) board.blackCanCastleQueenSide = false
            if (move.from == Square(0, 7)) board.blackCanCastleKingSide = false
        }
    }

    private fun getGameStatus(board: ChessBoard): String {
        val gen = MoveGenerator(board)
        return when {
            gen.isCheckmate() -> "Checkmate! ${if (board.turn == Color.WHITE) "Black" else "White"} wins"
            gen.isStalemate() -> "Stalemate"
            gen.isInsufficientMaterial() -> "Draw - Insufficient material"
            gen.isInCheck(board.turn) -> "Check!"
            else -> ""
        }
    }

    fun newGame() {
        _uiState.value = ChessUiState(difficulty = _uiState.value.difficulty)
    }

    fun setDifficulty(level: Int) {
        ai.maxDepth = level
        _uiState.value = _uiState.value.copy(difficulty = level)
    }

    fun undoMove() {
        val state = _uiState.value
        if (state.moveHistory.size >= 2) {
            val newHistory = state.moveHistory.dropLast(2)
            val newBoard = ChessBoard()
            for (move in newHistory) {
                val gen = MoveGenerator(newBoard)
                val legalMoves = gen.generateLegalMoves()
                val matching = legalMoves.find {
                    it.from == move.from && it.to == move.to && it.promotion == move.promotion
                } ?: continue
                newBoard.simulateMove(matching)
                updateCastlingRights(newBoard, matching)
                newBoard.turn = if (newBoard.turn == Color.WHITE) Color.BLACK else Color.WHITE
            }
            _uiState.value = state.copy(
                board = newBoard,
                moveHistory = newHistory,
                selectedSquare = null,
                legalMoves = emptyList(),
                lastMove = newHistory.lastOrNull(),
                gameStatus = ""
            )
        }
    }
}