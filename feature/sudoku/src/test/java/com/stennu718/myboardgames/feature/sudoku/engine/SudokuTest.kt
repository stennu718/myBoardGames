package com.stennu718.myboardgames.feature.sudoku.engine

import org.junit.Test
import org.junit.Assert.*

class SudokuTest {

    @Test
    fun testGeneratePuzzle() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.EASY)
        assertNotNull(puzzle)
        assertEquals(9, puzzle.grid.size)
        assertEquals(9, puzzle.grid[0].size)
    }

    @Test
    fun testPuzzleHasZeros() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.EASY)
        val zeroCount = puzzle.grid.sumOf { row -> row.count { it == 0 } }
        assertTrue(zeroCount > 0)
    }

    @Test
    fun testSolutionIsValid() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.MEDIUM)
        // Solution should have no zeros
        val zeroCount = puzzle.solution.sumOf { row -> row.count { it == 0 } }
        assertEquals(0, zeroCount)
    }

    @Test
    fun testSolutionSatisfiesConstraints() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.HARD)

        // Check rows
        for (r in 0..8) {
            val rowSet = puzzle.solution[r].toSet()
            assertEquals(9, rowSet.size)
            assertTrue(rowSet.all { it in 1..9 })
        }

        // Check columns
        for (c in 0..8) {
            val colSet = (0..8).map { puzzle.solution[it][c] }.toSet()
            assertEquals(9, colSet.size)
            assertTrue(colSet.all { it in 1..9 })
        }

        // Check 3x3 boxes
        for (boxR in 0..2) {
            for (boxC in 0..2) {
                val boxSet = mutableSetOf<Int>()
                for (r in boxR * 3 until boxR * 3 + 3) {
                    for (c in boxC * 3 until boxC * 3 + 3) {
                        boxSet.add(puzzle.solution[r][c])
                    }
                }
                assertEquals(9, boxSet.size)
            }
        }
    }

    @Test
    fun testSolve() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.EASY)
        val gridCopy = puzzle.grid.map { it.copyOf() }.toTypedArray()
        val solved = engine.solve(gridCopy)
        assertTrue(solved)
    }

    @Test
    fun testIsValidPlacement() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.MEDIUM)
        // Find a pre-filled cell and try to place same number
        for (r in 0..8) for (c in 0..8) {
            if (puzzle.grid[r][c] != 0) {
                assertTrue(engine.isValidPlacement(puzzle.grid, r, c, puzzle.grid[r][c]))
                break
            }
        }
    }

    @Test
    fun testGetHint() {
        val engine = SudokuEngine()
        val puzzle = engine.generate(Difficulty.EASY)
        val hint = engine.getHint(puzzle.grid, puzzle.solution)
        assertNotNull(hint)
        assertEquals(0, puzzle.grid[hint!!.first][hint.second])
    }

    @Test
    fun testDifficultyLevels() {
        val engine = SudokuEngine()
        val easy = engine.generate(Difficulty.EASY)
        val expert = engine.generate(Difficulty.EXPERT)

        val easyZeros = easy.grid.sumOf { row -> row.count { it == 0 } }
        val expertZeros = expert.grid.sumOf { row -> row.count { it == 0 } }
        assertTrue(expertZeros > easyZeros)
    }
}
