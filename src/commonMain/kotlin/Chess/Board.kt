package Chess

import com.soywiz.korio.util.isNumeric
import com.soywiz.korma.geom.Point

class Board(size: Double, cellSize: Double, topLeft: Point) {
    var cells = List<Cell>(64) { Cell(Point(topLeft.x + ((it % 8) * cellSize), topLeft.y + ((it / 8) * cellSize)), it / 8, it % 8)}


    fun print() {
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val piece = getCell(row, col).piece
                val notation = if (piece.color == Color.WHITE) piece.getPieceChar() else piece.getPieceChar().toLowerCase()
                print(" $notation ")
            }
            println()
        }
        println()
    }

    fun getCell(row: Int, col: Int): Cell {
        return cells[col + row * 8]
    }

    fun setCell(row: Int, col: Int, piece: Piece) {
        getCell(row, col).piece = piece
    }

    private fun isUpperCase(char: Char): Boolean {
        val upper = char.toUpperCase()
        return (upper == char)
    }

    private fun getPieceFromFenChar(char: Char): Piece {
        val color = if (isUpperCase(char)) Color.WHITE else Color.BLACK
        when (char.toUpperCase()) {
            'Q' -> return Queen(color)
            'K' -> return King(color)
            'R' -> return Rook(color)
            'N' -> return Knight(color)
            'B' -> return Bishop(color)
            'P' -> return Pawn(color)
        }
        return None()
    }

    fun loadFen(fen: String) {
        val boardState = fen.split(" ").toTypedArray()[0].split("/").toTypedArray()
        for (row in 0 until 8) {
            var colIndex = 0
            for (char in boardState[row]) {
                if (colIndex >= 8) {
                    break
                }
                if (char.isNumeric) {
                    colIndex += char.toInt()
                    continue
                }
                this.setCell(row, colIndex, getPieceFromFenChar(char))
                colIndex++
            }

        }

    }

    fun setStartPosition() {
        this.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }

}