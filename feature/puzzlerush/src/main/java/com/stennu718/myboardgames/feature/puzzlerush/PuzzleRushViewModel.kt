package com.stennu718.myboardgames.feature.puzzlerush

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stennu718.myboardgames.feature.chess.engine.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PuzzleRushViewModel : ViewModel() {
    private val engine = PuzzleRushEngine()
    private val moveGenerator = MoveGenerator(ChessBoard())

    private val _uiState = MutableStateFlow(PuzzleRushState())
    val uiState: StateFlow<PuzzleRushState> = _uiState.asStateFlow()

    private val _chessBoard = MutableStateFlow(ChessBoard())
    val chessBoard: StateFlow<ChessBoard> = _chessBoard.asStateFlow()

    private var timerJob: kotlinx.coroutines.Job? = null

    fun startGame() {
        val state = engine.startGame()
        _uiState.value = state
        loadNextPuzzle()
        startTimer()
    }

    private fun loadNextPuzzle() {
        val state = _uiState.value
        val puzzle = engine.getNextPuzzle(state)
        val board = ChessBoard.fromFEN(puzzle.fen)
        _chessBoard.value = board
        _uiState.value = state.copy(currentPuzzle = puzzle)
        moveGenerator = MoveGenerator(board)
    }

    fun onMove(move: Move) {
        val state = _uiState.value
        if (!state.isActive || state.currentPuzzle == null) return

        val newState = engine.submitMove(state, _chessBoard.value, move)
        _uiState.value = newState

        if (newState.isActive) {
            loadNextPuzzle()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isActive) {
                delay(100)
                _uiState.value = engine.tick(_uiState.value, 100)
            }
        }
    }

    fun getHint(): String {
        val puzzle = _uiState.value.currentPuzzle ?: return ""
        return "Try: ${puzzle.solution.first()}"
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
