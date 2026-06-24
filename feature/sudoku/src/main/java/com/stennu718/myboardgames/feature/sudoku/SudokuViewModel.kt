package com.stennu718.myboardgames.feature.sudoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stennu718.myboardgames.feature.sudoku.engine.Difficulty
import com.stennu718.myboardgames.feature.sudoku.engine.SudokuEngine
import com.stennu718.myboardgames.feature.sudoku.engine.SudokuPuzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SudokuUiState(
    val puzzle: SudokuPuzzle? = null,
    val selectedRow: Int? = null,
    val selectedCol: Int? = null,
    val isComplete: Boolean = false,
    val isGenerating: Boolean = false,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val timerSeconds: Long = 0,
    val hintCell: Pair<Int, Int>? = null
)

class SudokuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SudokuUiState())
    val uiState: StateFlow<SudokuUiState> = _uiState.asStateFlow()

    private val engine = SudokuEngine()

    init {
        newGame()
    }

    fun newGame() {
        _uiState.value = _uiState.value.copy(isGenerating = true)
        viewModelScope.launch {
            val puzzle = withContext(Dispatchers.Default) {
                engine.generate(_uiState.value.difficulty)
            }
            _uiState.value = _uiState.value.copy(
                puzzle = puzzle,
                isGenerating = false,
                isComplete = false,
                timerSeconds = 0,
                selectedRow = null,
                selectedCol = null,
                hintCell = null
            )
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(difficulty = difficulty)
        newGame()
    }

    fun selectCell(row: Int, col: Int) {
        val state = _uiState.value
        // Don't allow selecting filled cells (pre-filled)
        if (state.puzzle?.grid?.get(row)?.get(col) != 0) return
        _uiState.value = state.copy(selectedRow = row, selectedCol = col)
    }

    fun enterNumber(num: Int) {
        val state = _uiState.value
        val row = state.selectedRow ?: return
        val col = state.selectedCol ?: return
        val puzzle = state.puzzle ?: return
        if (puzzle.grid[row][col] != 0) return // pre-filled

        val newGrid = puzzle.grid.map { it.copyOf() }.toTypedArray()
        newGrid[row][col] = num

        val isComplete = engine.isComplete(newGrid) && isCorrect(newGrid, puzzle.solution)
        _uiState.value = state.copy(
            puzzle = puzzle.copy(grid = newGrid),
            isComplete = isComplete
        )
    }

    fun clearCell() {
        val state = _uiState.value
        val row = state.selectedRow ?: return
        val col = state.selectedCol ?: return
        val puzzle = state.puzzle ?: return
        if (puzzle.grid[row][col] != 0) return

        val newGrid = puzzle.grid.map { it.copyOf() }.toTypedArray()
        newGrid[row][col] = 0
        _uiState.value = state.copy(puzzle = puzzle.copy(grid = newGrid))
    }

    fun getHint() {
        val state = _uiState.value
        val puzzle = state.puzzle ?: return
        val hint = engine.getHint(puzzle.grid, puzzle.solution)
        if (hint != null) {
            _uiState.value = state.copy(hintCell = hint)
        }
    }

    fun incrementTimer() {
        _uiState.value = _uiState.value.copy(timerSeconds = _uiState.value.timerSeconds + 1)
    }

    private fun isCorrect(grid: Array<IntArray>, solution: Array<IntArray>): Boolean {
        for (r in 0..8) {
            for (c in 0..8) {
                if (grid[r][c] != solution[r][c]) return false
            }
        }
        return true
    }
}