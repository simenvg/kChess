package Chess

import com.soywiz.klock.seconds
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korio.util.isNumeric
import com.soywiz.korma.geom.Point
import com.soywiz.korma.interpolation.Easing

class Board(size: Double, cellSize: Double, topLeft: Point) {
    val topLeft = topLeft
    val cellSize = cellSize
    var cells = List<Cell>(64) { Cell(it / 8, it % 8) }

    var whiteToPlay = true


    fun print() {
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val piece = getCell(row, col).piece
                val notation =
                    if (piece.color == Color.WHITE) piece.getPieceChar() else piece.getPieceChar().toLowerCase()
                print(" $notation ")
            }
            println()
        }
        println()
    }

    fun getCell(row: Int, col: Int): Cell {
        return cells[col + row * 8]
    }

    fun generatePossibleMoves(row: Int, col: Int): List<Cell> {
        val cell = getCell(row, col)
        return when (cell.piece) {
            is Queen -> getSlidingMoves(cell, true, true)
            is King -> getSlidingMoves(cell, true, true)
            is Rook -> getSlidingMoves(cell, false, true)
            is Knight -> getSlidingMoves(cell, true, true)
            is Bishop -> getSlidingMoves(cell, true, false)
            is Pawn -> getSlidingMoves(cell, true, true)
            else -> listOf()
        }
    }

    fun isValidMove(current: Cell, direction: Direction): Boolean {
        if (current.col == 0 && direction.isWestward()) {
            return false
        } else if (current.col == 7 && direction.isEastward()) {
            return false
        } else if (current.row == 0 && direction.isNorthward()) {
            return false
        } else if (current.row == 7 && direction.isSouthward()) {
            return false
        }
        return true
    }

    fun getSlidingMoves(originCell: Cell, diagonal: Boolean, orthogonal: Boolean): List<Cell> {
        val res = mutableListOf<Cell>()
        if (diagonal) {
            res += getMovesInDirection(originCell, Direction.NORTH_WEST())
            res += getMovesInDirection(originCell, Direction.NORTH_EAST())
            res += getMovesInDirection(originCell, Direction.SOUTH_WEST())
            res += getMovesInDirection(originCell, Direction.SOUTH_EAST())
        }

        if (orthogonal) {
            res += getMovesInDirection(originCell, Direction.WEST())
            res += getMovesInDirection(originCell, Direction.EAST())
            res += getMovesInDirection(originCell, Direction.SOUTH())
            res += getMovesInDirection(originCell, Direction.NORTH())
        }
        return res
    }

    fun getMovesInDirection(originCell: Cell, direction: Direction): List<Cell> {

        var result = mutableListOf<Cell>()
        val playerColor = originCell.piece.color
        var current = originCell

        while (isValidMove(current, direction)) {
            current = step(current, direction)
            when (current.piece.color){
                Color.NONE -> result.add(current)
                playerColor -> break
                else -> {
                    result.add(current)
                    break
                }
            }
        }
        return result
    }

    fun step(originCell: Cell, direction: Direction): Cell {
        return cells[originCell.getCoordinate() + direction.inc]
    }

    suspend fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        println(generatePossibleMoves(fromRow, fromCol))
        val fromCell = getCell(fromRow, fromCol)
        val toCell = getCell(toRow, toCol)
        if (fromCell.piece is None) {
            return
        }
        if (toCell.piece !is None) {
            toCell.pieceIllustration!!.removeFromParent()
        }
        toCell.piece = fromCell.piece
        fromCell.piece = None()
        toCell.pieceIllustration = fromCell.pieceIllustration
        fromCell.pieceIllustration = null
        toCell.pieceIllustration!!.moveTo(
            topLeft.x + toCell.col * cellSize,
            topLeft.y + toCell.row * cellSize,
            0.1.seconds,
            easing = Easing.LINEAR
        )
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