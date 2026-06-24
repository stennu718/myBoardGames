package com.stennu718.myboardgames.feature.checkers

import androidx.lifecycle.ViewModel
import com.stennu718.myboardgames.feature.checkers.engine.CheckersBoard
import com.stennu718.myboardgames.feature.checkers.engine.CheckersColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CheckersUiState(
    val board: CheckersBoard = CheckersBoard(),
    val selectedRow: Int? = null,
    val selectedCol: Int? = null,
    val validMoves: List<Int> = emptyList(),
    val gameStatus: String = ""
)

class CheckersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CheckersUiState())
    val uiState: StateFlow<CheckersUiState> = _uiState.asStateFlow()

    fun onSquareClick(row: Int, col: Int) {
        val state = _uiState.value
        val piece = state.board.getPiece(row, col)

        if (state.selectedRow != null && state.selectedCol != null) {
            val fromIndex = state.selectedRow!! * 8 + state.selectedCol!!
            val toIndex = row * 8 + col
            if (state.validMoves.contains(toIndex)) {
                state.board.movePiece(state.selectedRow!!, state.selectedCol!!, row, col)
                _uiState.value = state.copy(
                    selectedRow = null,
                    selectedCol = null,
                    validMoves = emptyList(),
                    gameStatus = if (state.board.isGameOver()) {
                        val winner = state.board.getWinner()
                        "${winner} wins!"
                    } else ""
                )
            } else {
                _uiState.value = state.copy(selectedRow = null, selectedCol = null, validMoves = emptyList())
            }
        } else if (piece != null && piece.color == state.board.turn) {
            val moves = state.board.getValidMoves(state.board.turn)
            val fromIndex = row * 8 + col
            val pieceMoves = moves.filter { it.first == fromIndex }.map { it.second }
            _uiState.value = state.copy(
                selectedRow = row,
                selectedCol = col,
                validMoves = pieceMoves
            )
        }
    }

    fun newGame() {
        _uiState.value = CheckersUiState()
    }
}