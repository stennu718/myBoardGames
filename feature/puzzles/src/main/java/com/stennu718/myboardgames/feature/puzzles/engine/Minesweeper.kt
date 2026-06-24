package com.stennu718.myboardgames.feature.puzzles.engine

import kotlin.random.Random

enum class CellState { HIDDEN, REVEALED, FLAGGED }
enum class MineGameStatus { PLAYING, WON, LOST }

data class MineCell(
    val hasMine: Boolean = false,
    val neighborMines: Int = 0,
    val state: CellState = CellState.HIDDEN
)

data class MinesweeperState(
    val grid: Array<Array<MineCell>>,
    val rows: Int,
    val cols: Int,
    val mineCount: Int,
    val status: MineGameStatus = MineGameStatus.PLAYING,
    val flagCount: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MinesweeperState) return false
        return grid.contentDeepEquals(other.grid)
    }
    override fun hashCode(): Int = grid.contentDeepHashCode()
}

class MinesweeperGame(
    private val rows: Int = 9,
    private val cols: Int = 9,
    private val mineCount: Int = 10
) {
    private var state = createEmptyState()

    private fun createEmptyState(): MinesweeperState {
        val grid = Array(rows) { Array(cols) { MineCell() } }
        return MinesweeperState(grid = grid, rows = rows, cols = cols, mineCount = mineCount)
    }

    fun newGame() {
        state = createEmptyState()
        placeMines()
        calculateNeighborMines()
    }

    fun getState(): MinesweeperState = state

    private fun placeMines() {
        var placed = 0
        while (placed < mineCount) {
            val r = Random.nextInt(rows)
            val c = Random.nextInt(cols)
            if (!state.grid[r][c].hasMine) {
                state.grid[r][c] = state.grid[r][c].copy(hasMine = true)
                placed++
            }
        }
    }

    private fun calculateNeighborMines() {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (state.grid[r][c].hasMine) continue
                var count = 0
                for (dr in -1..1) for (dc in -1..1) {
                    if (dr == 0 && dc == 0) continue
                    val nr = r + dr
                    val nc = c + dc
                    if (nr in 0 until rows && nc in 0 until cols && state.grid[nr][nc].hasMine) {
                        count++
                    }
                }
                state.grid[r][c] = state.grid[r][c].copy(neighborMines = count)
            }
        }
    }

    fun reveal(row: Int, col: Int): Boolean {
        if (state.status != MineGameStatus.PLAYING) return false
        val cell = state.grid[row][col]
        if (cell.state != CellState.HIDDEN) return false

        if (cell.hasMine) {
            // Game over - reveal all mines
            for (r in 0 until rows) for (c in 0 until cols) {
                if (state.grid[r][c].hasMine) {
                    state.grid[r][c] = state.grid[r][c].copy(state = CellState.REVEALED)
                }
            }
            state = state.copy(status = MineGameStatus.LOST)
            return true
        }

        floodReveal(row, col)
        checkWin()
        return true
    }

    private fun floodReveal(row: Int, col: Int) {
        if (row !in 0 until rows || col !in 0 until cols) return
        if (state.grid[row][col].state != CellState.HIDDEN) return
        if (state.grid[row][col].hasMine) return

        state.grid[row][col] = state.grid[row][col].copy(state = CellState.REVEALED)

        if (state.grid[row][col].neighborMines == 0) {
            for (dr in -1..1) for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                floodReveal(row + dr, col + dc)
            }
        }
    }

    fun toggleFlag(row: Int, col: Int) {
        if (state.status != MineGameStatus.PLAYING) return
        val cell = state.grid[row][col]
        when (cell.state) {
            CellState.HIDDEN -> {
                state.grid[row][col] = cell.copy(state = CellState.FLAGGED)
                state = state.copy(flagCount = state.flagCount + 1)
            }
            CellState.FLAGGED -> {
                state.grid[row][col] = cell.copy(state = CellState.HIDDEN)
                state = state.copy(flagCount = state.flagCount - 1)
            }
            else -> {}
        }
    }

    private fun checkWin() {
        var unrevealedSafe = 0
        for (r in 0 until rows) for (c in 0 until cols) {
            if (state.grid[r][c].state == CellState.HIDDEN && !state.grid[r][c].hasMine) {
                unrevealedSafe++
            }
        }
        if (unrevealedSafe == 0) {
            state = state.copy(status = MineGameStatus.WON)
        }
    }
}