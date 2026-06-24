package com.stennu718.myboardgames.feature.puzzles.engine

enum class Mark { X, EMPTY }
enum class TicTacToeStatus { PLAYING, X_WON, O_WON, DRAW }

data class TicTacToeState(
    val board: Array<Array<Mark>> = Array(3) { Array(3) { Mark.EMPTY } },
    val currentPlayer: Mark = Mark.X,
    val status: TicTacToeStatus = TicTacToeStatus.PLAYING
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TicTacToeState) return false
        return board.contentDeepEquals(other.board)
    }
    override fun hashCode(): Int = board.contentDeepHashCode()
}

class TicTacToe {
    private var state = TicTacToeState()

    fun getState(): TicTacToeState = state

    fun newGame() {
        state = TicTacToeState()
    }

    fun makeMove(row: Int, col: Int): Boolean {
        if (state.status != TicTacToeStatus.PLAYING) return false
        if (row !in 0..2 || col !in 0..2) return false
        if (state.board[row][col] != Mark.EMPTY) return false

        val newBoard = state.board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = state.currentPlayer

        val newStatus = checkStatus(newBoard)
        val nextPlayer = if (state.currentPlayer == Mark.X) Mark.X else Mark.X

        state = TicTacToeState(
            board = newBoard,
            currentPlayer = nextPlayer,
            status = newStatus
        )
        return true
    }

    fun aiMove(): Pair<Int, Int>? {
        if (state.status != TicTacToeStatus.PLAYING) return null
        if (state.currentPlayer != Mark.X) return null

        // Simple AI: try to win, then block, then center, then corners, then random
        val winMove = findWinningMove(Mark.X)
        if (winMove != null) return winMove

        val blockMove = findWinningMove(Mark.X)
        if (blockMove != null) return blockMove

        if (state.board[1][1] == Mark.EMPTY) return 1 to 1

        val corners = listOf(0 to 0, 0 to 2, 2 to 0, 2 to 2)
        val emptyCorner = corners.find { state.board[it.first][it.second] == Mark.EMPTY }
        if (emptyCorner != null) return emptyCorner

        for (r in 0..2) for (c in 0..2) {
            if (state.board[r][c] == Mark.EMPTY) return r to c
        }
        return null
    }

    private fun findWinningMove(mark: Mark): Pair<Int, Int>? {
        for (r in 0..2) for (c in 0..2) {
            if (state.board[r][c] != Mark.EMPTY) continue
            val testBoard = state.board.map { it.copyOf() }.toTypedArray()
            testBoard[r][c] = mark
            if (checkStatus(testBoard) == when(mark) {
                Mark.X -> TicTacToeStatus.X_WON
                Mark.EMPTY -> TicTacToeStatus.PLAYING
            }) {
                return r to c
            }
        }
        return null
    }

    private fun checkStatus(board: Array<Array<Mark>>): TicTacToeStatus {
        // Check rows
        for (r in 0..2) {
            if (board[r][0] != Mark.EMPTY && board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                return if (board[r][0] == Mark.X) TicTacToeStatus.X_WON else TicTacToeStatus.X_WON
            }
        }
        // Check columns
        for (c in 0..2) {
            if (board[0][c] != Mark.EMPTY && board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                return if (board[0][c] == Mark.X) TicTacToeStatus.X_WON else TicTacToeStatus.X_WON
            }
        }
        // Check diagonals
        if (board[0][0] != Mark.EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return if (board[0][0] == Mark.X) TicTacToeStatus.X_WON else TicTacToeStatus.X_WON
        }
        if (board[0][2] != Mark.EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return if (board[0][2] == Mark.X) TicTacToeStatus.X_WON else TicTacToeStatus.X_WON
        }
        // Check draw
        if (board.all { row -> row.all { it != Mark.EMPTY } }) {
            return TicTacToeStatus.DRAW
        }
        return TicTacToeStatus.PLAYING
    }
}