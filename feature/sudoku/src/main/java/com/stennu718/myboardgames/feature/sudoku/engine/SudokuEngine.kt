package com.stennu718.myboardgames.feature.sudoku.engine

import kotlin.random.Random

enum class Difficulty(val cellsToRemove: Int) {
    EASY(35), MEDIUM(45), HARD(52), EXPERT(58)
}

data class SudokuPuzzle(
    val grid: Array<IntArray>,
    val solution: Array<IntArray>,
    val difficulty: Difficulty
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SudokuPuzzle) return false
        return grid.contentDeepEquals(other.grid) && solution.contentDeepEquals(other.solution)
    }
    override fun hashCode(): Int = grid.contentDeepHashCode()
}

class SudokuEngine {

    fun generate(difficulty: Difficulty = Difficulty.MEDIUM): SudokuPuzzle {
        val solution = generateFullGrid()
        val puzzle = removeCells(solution, difficulty.cellsToRemove)
        return SudokuPuzzle(puzzle, solution, difficulty)
    }

    fun solve(grid: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (grid[row][col] == 0) {
                    for (num in 1..9) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num
                            if (solve(grid)) return true
                            grid[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    fun isValidPlacement(grid: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        return isValid(grid, row, col, num)
    }

    fun isComplete(grid: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (grid[row][col] == 0) return false
            }
        }
        return true
    }

    fun getHint(grid: Array<IntArray>, solution: Array<IntArray>): Pair<Int, Int>? {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (row in 0..8) {
            for (col in 0..8) {
                if (grid[row][col] == 0) {
                    emptyCells.add(row to col)
                }
            }
        }
        return emptyCells.randomOrNull()
    }

    private fun generateFullGrid(): Array<IntArray> {
        val grid = Array(9) { IntArray(9) }
        fillGrid(grid)
        return grid
    }

    private fun fillGrid(grid: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (grid[row][col] == 0) {
                    val numbers = (1..9).shuffled()
                    for (num in numbers) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num
                            if (fillGrid(grid)) return true
                            grid[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValid(grid: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        // Check row
        for (c in 0..8) {
            if (grid[row][c] == num) return false
        }
        // Check column
        for (r in 0..8) {
            if (grid[r][col] == num) return false
        }
        // Check 3x3 box
        val boxRow = (row / 3) * 3
        val boxCol = (col / 3) * 3
        for (r in boxRow until boxRow + 3) {
            for (c in boxCol until boxCol + 3) {
                if (grid[r][c] == num) return false
            }
        }
        return true
    }

    private fun removeCells(solution: Array<IntArray>, count: Int): Array<IntArray> {
        val grid = solution.map { it.copyOf() }.toTypedArray()
        var removed = 0
        val cells = mutableListOf<Pair<Int, Int>>()
        for (r in 0..8) for (c in 0..8) cells.add(r to c)
        cells.shuffle()

        for ((row, col) in cells) {
            if (removed >= count) break
            val backup = grid[row][col]
            grid[row][col] = 0

            // Ensure unique solution
            val testGrid = grid.map { it.copyOf() }.toTypedArray()
            if (countSolutions(testGrid) == 1) {
                removed++
            } else {
                grid[row][col] = backup
            }
        }
        return grid
    }

    private fun countSolutions(grid: Array<IntArray>, limit: Int = 2): Int {
        var count = 0
        fun solve(): Boolean {
            for (row in 0..8) {
                for (col in 0..8) {
                    if (grid[row][col] == 0) {
                        for (num in 1..9) {
                            if (isValid(grid, row, col, num)) {
                                grid[row][col] = num
                                if (solve()) return true
                                grid[row][col] = 0
                            }
                        }
                        return false
                    }
                }
            }
            count++
            return count >= limit
        }
        solve()
        return count
    }
}