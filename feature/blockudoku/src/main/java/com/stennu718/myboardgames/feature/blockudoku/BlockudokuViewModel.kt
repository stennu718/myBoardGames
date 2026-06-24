package com.stennu718.myboardgames.feature.blockudoku

import androidx.lifecycle.ViewModel
import com.stennu718.myboardgames.feature.blockudoku.engine.BlockudokuBoard
import com.stennu718.myboardgames.feature.blockudoku.engine.PieceGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BlockudokuUiState(
    val board: BlockudokuBoard = BlockudokuBoard(),
    val availablePieces: List<Array<BooleanArray>> = emptyList(),
    val selectedPieceIndex: Int? = null,
    val dragRow: Int? = null,
    val dragCol: Int? = null,
    val isGameOver: Boolean = false,
    val score: Int = 0
)

class BlockudokuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BlockudokuUiState())
    val uiState: StateFlow<BlockudokuUiState> = _uiState.asStateFlow()

    init {
        newGame()
    }

    fun newGame() {
        val pieces = PieceGenerator.generatePieceBatch()
        _uiState.value = BlockudokuUiState(
            board = BlockudokuBoard(),
            availablePieces = pieces
        )
    }

    fun selectPiece(index: Int) {
        _uiState.value = _uiState.value.copy(selectedPieceIndex = index)
    }

    fun placePiece(row: Int, col: Int) {
        val state = _uiState.value
        val pieceIndex = state.selectedPieceIndex ?: return
        val piece = state.availablePieces.getOrNull(pieceIndex) ?: return

        if (!state.board.canPlace(piece, row, col)) return

        val newBoard = state.board.place(piece, row, col)
        val remainingPieces = state.availablePieces.toMutableList()
        remainingPieces.removeAt(pieceIndex)

        // Generate new batch if all used
        val newPieces = if (remainingPieces.isEmpty()) {
            PieceGenerator.generatePieceBatch()
        } else {
            remainingPieces
        }

        val isGameOver = newBoard.isGameOver(newPieces)

        _uiState.value = state.copy(
            board = newBoard,
            availablePieces = newPieces,
            selectedPieceIndex = null,
            score = newBoard.score,
            isGameOver = isGameOver
        )
    }
}