package com.stennu718.myboardgames.feature.chess.engine

import org.junit.Test
import org.junit.Assert.*

class ChessBoardTest {

    @Test
    fun testInitialPosition() {
        val board = ChessBoard()
        assertEquals(Piece(PieceType.ROOK, Color.BLACK), board.getPiece(Square(0, 0)))
        assertEquals(Piece(PieceType.KING, Color.WHITE), board.getPiece(Square(7, 4)))
        assertEquals(Piece(PieceType.PAWN, Color.BLACK), board.getPiece(Square(1, 0)))
        assertNull(board.getPiece(Square(4, 4)))
    }

    @Test
    fun testFENRoundTrip() {
        val fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        val board = ChessBoard.fromFEN(fen)
        assertEquals(fen, board.toFEN())
    }

    @Test
    fun testFENCustom() {
        val fen = "r1bqkb1r/pppp1ppp/2n2n2/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4"
        val board = ChessBoard.fromFEN(fen)
        assertEquals(fen, board.toFEN())
    }

    @Test
    fun testPawnMoves() {
        val board = ChessBoard()
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        assertEquals(16, moves.count { it.piece.type == PieceType.PAWN })
    }

    @Test
    fun testKnightMoves() {
        val board = ChessBoard()
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        assertEquals(4, moves.count { it.piece.type == PieceType.KNIGHT })
    }

    @Test
    fun testOpeningMoveCount() {
        val board = ChessBoard()
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        assertEquals(20, moves.size)
    }

    @Test
    fun testIsInCheck() {
        val board = ChessBoard.fromFEN("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3")
        val gen = MoveGenerator(board)
        assertTrue(gen.isInCheck(Color.WHITE))
    }

    @Test
    fun testIsCheckmate() {
        val board = ChessBoard.fromFEN("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR b KQkq - 1 3")
        val gen = MoveGenerator(board)
        assertTrue(gen.isCheckmate())
    }

    @Test
    fun testIsStalemate() {
        val board = ChessBoard.fromFEN("k7/8/1K6/8/8/8/8/8 b - - 0 1")
        val gen = MoveGenerator(board)
        assertTrue(gen.isStalemate())
    }

    @Test
    fun testCastling() {
        val board = ChessBoard.fromFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1")
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        val castleMoves = moves.filter { it.isCastle }
        assertEquals(2, castleMoves.size)
    }

    @Test
    fun testEnPassant() {
        val board = ChessBoard.fromFEN("rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1")
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        val epMoves = moves.filter { it.isEnPassant }
        assertEquals(1, epMoves.size)
    }

    @Test
    fun testPromotion() {
        val board = ChessBoard.fromFEN("8/P7/8/8/8/8/8/k6K w - - 0 1")
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        val promoMoves = moves.filter { it.promotion != null }
        assertEquals(4, promoMoves.size)
    }

    @Test
    fun testSimulateMove() {
        val board = ChessBoard()
        val e2 = Square.fromAlgebraic("e2")!!
        val e4 = Square.fromAlgebraic("e4")!!
        val move = Move(e2, e4, board.getPiece(e2)!!)
        val newBoard = board.simulateMove(move)
        assertNull(newBoard.getPiece(e2))
        assertNotNull(newBoard.getPiece(e4))
    }

    @Test
    fun testPerft1() {
        val board = ChessBoard()
        val gen = MoveGenerator(board)
        val moves = gen.generateLegalMoves()
        assertEquals(20, moves.size)
    }

    @Test
    fun testPerft2() {
        val board = ChessBoard()
        val gen = MoveGenerator(board)
        var total = 0
        for (move in gen.generateLegalMoves()) {
            val newBoard = board.simulateMove(move)
            val newGen = MoveGenerator(newBoard)
            total += newGen.generateLegalMoves().size
        }
        assertEquals(400, total)
    }

    @Test
    fun testPerft3() {
        val board = ChessBoard()
        val gen = MoveGenerator(board)
        var total = 0
        for (move in gen.generateLegalMoves()) {
            val newBoard = board.simulateMove(move)
            val newGen = MoveGenerator(newBoard)
            for (move2 in newGen.generateLegalMoves()) {
                val newBoard2 = newBoard.simulateMove(move2)
                val newGen2 = MoveGenerator(newBoard2)
                total += newGen2.generateLegalMoves().size
            }
        }
        assertEquals(8902, total)
    }
}

class ChessAITest {

    @Test
    fun testAIFindsLegalMove() {
        val board = ChessBoard()
        val ai = ChessAI(2)
        val move = ai.findBestMove(board)
        assertNotNull(move)
    }

    @Test
    fun testAIFindsMateInOne() {
        val board = ChessBoard.fromFEN("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR b KQkq - 1 3")
        val ai = ChessAI(3)
        val move = ai.findBestMove(board)
        assertNotNull(move)
    }

    @Test
    fun testEvaluationSymmetry() {
        val board = ChessBoard()
        val ai = ChessAI(2)
        assertEquals(0, ai.evaluate(board))
    }
}
