package Chess

import com.soywiz.klock.seconds
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korio.util.isNumeric
import com.soywiz.korma.geom.Point
import com.soywiz.korma.interpolation.Easing

class Board(size: Double, cellSize: Double, topLeft: Point) {
    val topLeft = topLeft
    val cellSize = cellSize
    var cells = List<Cell>(64) { Cell(Point(topLeft.x + ((it % 8) * cellSize), topLeft.y + ((it / 8) * cellSize)), it / 8, it % 8)}

    var whiteToPlay = true


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

    suspend fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        print(" $fromRow, $fromCol")
        print(" $toRow, $toCol")
        val fromCell = getCell(fromRow, fromCol)
        val toCell = getCell(toRow, toCol)
        if (fromCell.piece is None){
            return
        }
        if(toCell.piece !is None) {
            toCell.pieceIllustration!!.removeFromParent()
        }
        toCell.piece = fromCell.piece
        fromCell.piece = None()
        toCell.pieceIllustration = fromCell.pieceIllustration
        fromCell.pieceIllustration = null
        toCell.pieceIllustration!!.moveTo(topLeft.x + toCell.col * cellSize, topLeft.y + toCell.row * cellSize, 0.1.seconds, easing = Easing.LINEAR)
        whiteToPlay = !whiteToPlay
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
                this.getCell(row, colIndex).piece = getPieceFromFenChar(char)
                colIndex++
            }

        }

    }

    fun setStartPosition() {
        this.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }

}