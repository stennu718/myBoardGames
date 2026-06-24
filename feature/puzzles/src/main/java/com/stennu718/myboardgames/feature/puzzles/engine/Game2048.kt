package com.stennu718.myboardgames.feature.puzzles.engine

import kotlin.random.Random

data class Game2048State(
    val grid: Array<IntArray> = Array(4) { IntArray(4) },
    val score: Int = 0,
    val gameOver: Boolean = false,
    val won: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Game2048State) return false
        return grid.contentDeepEquals(other.grid)
    }
    override fun hashCode(): Int = grid.contentDeepHashCode()
}

class Game2048 {
    private var state = Game2048State()

    fun getState(): Game2048State = state

    fun newGame() {
        state = Game2048State()
        addRandomTile()
        addRandomTile()
    }

    private fun addRandomTile() {
        val empty = mutableListOf<Pair<Int, Int>>()
        for (r in 0..3) for (c in 0..3) {
            if (state.grid[r][c] == 0) empty.add(r to c)
        }
        if (empty.isEmpty()) return
        val (r, c) = empty.random()
        state.grid[r][c] = if (Random.nextFloat() < 0.9f) 2 else 4
    }

    fun move(direction: Direction): Boolean {
        val oldGrid = state.grid.map { it.copyOf() }.toTypedArray()
        var moved = false
        var scoreGain = 0

        when (direction) {
            Direction.LEFT -> {
                for (r in 0..3) {
                    val row = state.grid[r].filter { it != 0 }.toMutableList()
                    val merged = mutableListOf<Int>()
                    var i = 0
                    while (i < row.size) {
                        if (i + 1 < row.size && row[i] == row[i + 1]) {
                            merged.add(row[i] * 2)
                            scoreGain += row[i] * 2
                            i += 2
                        } else {
                            merged.add(row[i])
                            i++
                        }
                    }
                    while (merged.size < 4) merged.add(0)
                    for (c in 0..3) {
                        if (state.grid[r][c] != merged[c]) moved = true
                        state.grid[r][c] = merged[c]
                    }
                }
            }
            Direction.RIGHT -> {
                for (r in 0..3) {
                    val row = state.grid[r].filter { it != 0 }.toMutableList()
                    val merged = mutableListOf<Int>()
                    var i = row.size - 1
                    while (i >= 0) {
                        if (i - 1 >= 0 && row[i] == row[i - 1]) {
                            merged.add(0, row[i] * 2)
                            scoreGain += row[i] * 2
                            i -= 2
                        } else {
                            merged.add(0, row[i])
                            i--
                        }
                    }
                    while (merged.size < 4) merged.add(0, 0)
                    for (c in 0..3) {
                        if (state.grid[r][c] != merged[c]) moved = true
                        state.grid[r][c] = merged[c]
                    }
                }
            }
            Direction.UP -> {
                for (c in 0..3) {
                    val col = mutableListOf<Int>()
                    for (r in 0..3) if (state.grid[r][c] != 0) col.add(state.grid[r][c])
                    val merged = mutableListOf<Int>()
                    var i = 0
                    while (i < col.size) {
                        if (i + 1 < col.size && col[i] == col[i + 1]) {
                            merged.add(col[i] * 2)
                            scoreGain += col[i] * 2
                            i += 2
                        } else {
                            merged.add(col[i])
                            i++
                        }
                    }
                    while (merged.size < 4) merged.add(0)
                    for (r in 0..3) {
                        if (state.grid[r][c] != merged[r]) moved = true
                        state.grid[r][c] = merged[r]
                    }
                }
            }
            Direction.DOWN -> {
                for (c in 0..3) {
                    val col = mutableListOf<Int>()
                    for (r in 0..3) if (state.grid[r][c] != 0) col.add(state.grid[r][c])
                    val merged = mutableListOf<Int>()
                    var i = col.size - 1
                    while (i >= 0) {
                        if (i - 1 >= 0 && col[i] == col[i - 1]) {
                            merged.add(0, col[i] * 2)
                            scoreGain += col[i] * 2
                            i -= 2
                        } else {
                            merged.add(0, col[i])
                            i--
                        }
                    }
                    while (merged.size < 4) merged.add(0, 0)
                    for (r in 0..3) {
                        if (state.grid[r][c] != merged[r]) moved = true
                        state.grid[r][c] = merged[r]
                    }
                }
            }
        }

        if (moved) {
            state = state.copy(score = state.score + scoreGain)
            addRandomTile()
            checkGameOver()
        }

        return moved
    }

    private fun checkGameOver() {
        // Check for 2048
        for (r in 0..3) for (c in 0..3) {
            if (state.grid[r][c] == 2048) state = state.copy(won = true)
        }

        // Check if no moves possible
        for (r in 0..3) for (c in 0..3) {
            if (state.grid[r][c] == 0) return
            if (c < 3 && state.grid[r][c] == state.grid[r][c + 1]) return
            if (r < 3 && state.grid[r][c] == state.grid[r + 1][c]) return
        }
        state = state.copy(gameOver = true)
    }

    enum class Direction { LEFT, RIGHT, UP, DOWN }
}