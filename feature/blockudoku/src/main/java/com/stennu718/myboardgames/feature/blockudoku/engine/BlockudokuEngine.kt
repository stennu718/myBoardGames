package com.stennu718.myboardgames.feature.blockudoku.engine

import kotlin.random.Random

data class BlockudokuBoard(
    val grid: Array<BooleanArray> = Array(9) { BooleanArray(9) },
    val score: Int = 0
) {
    fun canPlace(piece: Array<BooleanArray>, row: Int, col: Int): Boolean {
        for (r in piece.indices) {
            for (c in piece[r].indices) {
                if (piece[r][c]) {
                    val boardRow = row + r
                    val boardCol = col + c
                    if (boardRow !in 0..8 || boardCol !in 0..8) return false
                    if (grid[boardRow][boardCol]) return false
                }
            }
        }
        return true
    }

    fun place(piece: Array<BooleanArray>, row: Int, col: Int): BlockudokuBoard {
        val newGrid = grid.map { it.copyOf() }.toTypedArray()
        var cellsPlaced = 0
        for (r in piece.indices) {
            for (c in piece[r].indices) {
                if (piece[r][c]) {
                    newGrid[row + r][col + c] = true
                    cellsPlaced++
                }
            }
        }

        val cleared = clearLines(newGrid)
        val newScore = score + cellsPlaced + cleared * 10

        return BlockudokuBoard(newGrid, newScore)
    }

    private fun clearLines(grid: Array<BooleanArray>): Int {
        var cleared = 0

        // Check rows
        for (r in 0..8) {
            if (grid[r].all { it }) {
                for (c in 0..8) grid[r][c] = false
                cleared++
            }
        }

        // Check columns
        for (c in 0..8) {
            if ((0..8).all { grid[it][c] }) {
                for (r in 0..8) grid[r][c] = false
                cleared++
            }
        }

        // Check 3x3 boxes
        for (boxRow in 0..2) {
            for (boxCol in 0..2) {
                var full = true
                for (r in boxRow * 3 until boxRow * 3 + 3) {
                    for (c in boxCol * 3 until boxCol * 3 + 3) {
                        if (!grid[r][c]) { full = false; break }
                    }
                    if (!full) break
                }
                if (full) {
                    for (r in boxRow * 3 until boxRow * 3 + 3) {
                        for (c in boxCol * 3 until boxCol * 3 + 3) {
                            grid[r][c] = false
                        }
                    }
                    cleared++
                }
            }
        }

        return cleared
    }

    fun isGameOver(pieces: List<Array<BooleanArray>>): Boolean {
        return pieces.all { piece ->
            !canPlaceAnywhere(piece)
        }
    }

    private fun canPlaceAnywhere(piece: Array<BooleanArray>): Boolean {
        for (r in 0..8) {
            for (c in 0..8) {
                if (canPlace(piece, r, c)) return true
            }
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlockudokuBoard) return false
        return grid.contentDeepEquals(other.grid)
    }
    override fun hashCode(): Int = grid.contentDeepHashCode()
}

object PieceGenerator {
    private val pieces = listOf(
        // Single
        arrayOf(booleanArrayOf(true)),
        // Line 2
        arrayOf(booleanArrayOf(true, true)),
        // Line 3
        arrayOf(booleanArrayOf(true, true, true)),
        // Line 4
        arrayOf(booleanArrayOf(true, true, true, true)),
        // Line 5
        arrayOf(booleanArrayOf(true, true, true, true, true)),
        // L-shape
        arrayOf(
            booleanArrayOf(true, false),
            booleanArrayOf(true, false),
            booleanArrayOf(true, true)
        ),
        // Reverse L
        arrayOf(
            booleanArrayOf(false, true),
            booleanArrayOf(false, true),
            booleanArrayOf(true, true)
        ),
        // T-shape
        arrayOf(
            booleanArrayOf(true, true, true),
            booleanArrayOf(false, true, false)
        ),
        // S-shape
        arrayOf(
            booleanArrayOf(false, true),
            booleanArrayOf(true, true),
            booleanArrayOf(true, false)
        ),
        // Z-shape
        arrayOf(
            booleanArrayOf(true, false),
            booleanArrayOf(true, true),
            booleanArrayOf(false, true)
        ),
        // 2x2 square
        arrayOf(
            booleanArrayOf(true, true),
            booleanArrayOf(true, true)
        ),
        // 3x3 square
        arrayOf(
            booleanArrayOf(true, true, true),
            booleanArrayOf(true, true, true),
            booleanArrayOf(true, true, true)
        ),
        // Corner 2x2 L
        arrayOf(
            booleanArrayOf(true, true),
            booleanArrayOf(true, false)
        ),
        // Corner 2x2 reverse L
        arrayOf(
            booleanArrayOf(true, true),
            booleanArrayOf(false, true)
        ),
        // Vertical L
        arrayOf(
            booleanArrayOf(true, false, false),
            booleanArrayOf(true, true, true)
        ),
        // Plus
        arrayOf(
            booleanArrayOf(false, true, false),
            booleanArrayOf(true, true, true),
            booleanArrayOf(false, true, false)
        )
    )

    fun generatePiece(): Array<BooleanArray> {
        return pieces[Random.nextInt(pieces.size)].map { it.copyOf() }.toTypedArray()
    }

    fun generatePieceBatch(): List<Array<BooleanArray>> {
        return listOf(generatePiece(), generatePiece(), generatePiece())
    }
}