package com.stennu718.myboardgames.feature.puzzlerush

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stennu718.myboardgames.feature.chess.engine.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PuzzleRushViewModel : ViewModel() {
    private val engine = PuzzleRushEngine()

    private val _uiState = MutableStateFlow(PuzzleRushState())
    val uiState: StateFlow<PuzzleRushState> = _uiState.asStateFlow()

    private val _chessBoard = MutableStateFlow(ChessBoard())
    val chessBoard: StateFlow<ChessBoard> = _chessBoard.asStateFlow()

    private val _selectedSquare = MutableStateFlow<Square?>(null)
    val selectedSquare: StateFlow<Square?> = _selectedSquare.asStateFlow()

    private val _legalMoves = MutableStateFlow<List<Move>>(emptyList())
    val legalMoves: StateFlow<List<Move>> = _legalMoves.asStateFlow()

    private var currentGenerator: MoveGenerator? = null

    fun startGame() {
        val state = engine.startGame()
        _uiState.value = state
        _selectedSquare.value = null
        _legalMoves.value = emptyList()
        loadNextPuzzle()
    }

    private fun loadNextPuzzle() {
        val state = _uiState.value
        val puzzle = engine.getNextPuzzle(state)
        val board = ChessBoard.fromFEN(puzzle.fen)
        _chessBoard.value = board
        _uiState.value = state.copy(currentPuzzle = puzzle)
        currentGenerator = MoveGenerator(board)
        _selectedSquare.value = null
        _legalMoves.value = emptyList()
    }

    fun onSquareClick(square: Square) {
        val state = _uiState.value
        if (!state.isActive || state.currentPuzzle == null) return

        val board = _chessBoard.value
        val piece = board.getPiece(square)

        val selected = _selectedSquare.value
        if (selected != null) {
            // Try to make a move
            val move = _legalMoves.value.find { it.to == square }
            if (move != null) {
                onMove(move)
                return
            }
            // If clicking same square, deselect
            if (square == selected) {
                _selectedSquare.value = null
                _legalMoves.value = emptyList()
                return
            }
            // If clicking own piece, re-select
            if (piece != null && piece.color == board.turn) {
                selectSquare(square)
                return
            }
            // Otherwise deselect
            _selectedSquare.value = null
            _legalMoves.value = emptyList()
        } else {
            // Select piece
            if (piece != null && piece.color == board.turn) {
                selectSquare(square)
            }
        }
    }

    private fun selectSquare(square: Square) {
        val gen = currentGenerator ?: return
        val moves = gen.generateLegalMoves().filter { it.from == square }
        _selectedSquare.value = square
        _legalMoves.value = moves
    }

    fun onMove(move: Move) {
        val state = _uiState.value
        if (!state.isActive || state.currentPuzzle == null) return

        val newState = engine.submitMove(state, _chessBoard.value, move)
        _uiState.value = newState
        _selectedSquare.value = null
        _legalMoves.value = emptyList()

        if (newState.isActive) {
            loadNextPuzzle()
        }
    }

    fun tick(deltaMs: Long) {
        val state = _uiState.value
        if (!state.isActive) return
        _uiState.value = engine.tick(state, deltaMs)
    }

    fun getHint(): String {
        val puzzle = _uiState.value.currentPuzzle ?: return ""
        return "Try: ${puzzle.solution.first()}"
    }
}