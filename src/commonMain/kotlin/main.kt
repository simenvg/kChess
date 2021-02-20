import Chess.Board
import Chess.None
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onDown
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*


suspend fun main() = Korge(width = 1024, height = 1024, bgcolor = Colors["#2b2b2b"]) {
	val white = Colors["#c7c6bd"]
	val black = Colors["#5b7b87"]


	var selectedCell: BoardGraphicsState? = null

	val board = BooleanArray(64) { false }
	board[5] = true
	board[37] = true

	val boardSize = 650.0
	val cellSize = boardSize / 8

	val boardLeft = (width - boardSize) / 2
	val boardTop = (height - boardSize) / 2


	val bgField = SolidRect(boardSize, boardSize, Colors["#b9aea0"])
	bgField.x = boardLeft
	bgField.y = boardTop
	addChild(bgField)

	val b = Board(boardSize, cellSize, Point(boardLeft, boardTop))
	b.setStartPosition()
	b.print()

	fun setSelectedCell(row: Int, col: Int) {
		val newSelectedCell = RoundRect(cellSize+3, cellSize+3, 10.0, fill = RGBA(0xFF, 0xFF, 0xFF, 0x00), stroke = Colors.RED, strokeThickness = 3.0)
		newSelectedCell.x = boardLeft + col * cellSize
		newSelectedCell.y = boardTop + row * cellSize
		addChild(newSelectedCell)
		selectedCell = BoardGraphicsState(newSelectedCell, row, col)
	}

	suspend fun cellClicked(row: Int, col: Int) {
		if (selectedCell == null) {
			setSelectedCell(row, col)
			return
		}
		if (selectedCell!!.row == row && selectedCell!!.col == col) {
			return
		}
		b.movePiece(selectedCell!!.row, selectedCell!!.col, row, col)
		removeChild(selectedCell!!.selectedCellView)
		selectedCell = null
	}

	fun renderClickCells() {
		for (row in 0..7) {
			for (col in 0..7) {
				val cell = SolidRect(cellSize, cellSize, Colors.TRANSPARENT_WHITE)
				cell.x = boardLeft + col * cellSize
				cell.y = boardTop + row * cellSize
				cell.onDown {
					cellClicked(row, col)
					renderClickCells()
				}
				addChild(cell)
			}
		}
	}

	fun renderBoard() {
		for (row in 0..7) {
			for (col in 0..7) {
				val color = if ((row + col) % 2 == 0) white else black
				val cell = SolidRect(cellSize, cellSize, color)
				cell.x = boardLeft + col * cellSize
				cell.y = boardTop + row * cellSize
				addChild(cell)
			}
		}
	}

	suspend fun renderPieces() {
		for (cell in b.cells ) {
			val filepath = cell.piece.getPiecePngFileName()
			if (filepath != null) {
				cell.pieceIllustration = image(resourcesVfs[filepath].readBitmap()) {
					position(cell.coordinates)
				}
			}
		}
	}

	renderBoard()
	renderPieces()
	renderClickCells()




}