import Chess.Board
import Chess.None
import com.soywiz.klock.seconds
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onDown
import com.soywiz.korge.input.onOver
import com.soywiz.korge.tween.V2
import com.soywiz.korge.tween.tween
import com.soywiz.korge.tween.tweenAsync
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.PNG.readImage
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.paint.Paint
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*


suspend fun main() = Korge(width = 1024, height = 1024, bgcolor = Colors["#2b2b2b"]) {
	val white = Colors["#c7c6bd"]
	val black = Colors["#5b7b87"]

	var selectedCell: RoundRect? = null

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

	fun renderSelectedCell(row: Int, col: Int, board: Board) {
		if (board.getCell(row, col).piece is None){
			return
		}


		println("SelectedCell: $selectedCell")
		removeChild(selectedCell)
		val newSelectedCell = RoundRect(cellSize+3, cellSize+3, 10.0, fill = RGBA(0xFF, 0xFF, 0xFF, 0x00), stroke = Colors.RED, strokeThickness = 3.0)
		newSelectedCell.x = boardLeft + col * cellSize
		newSelectedCell.y = boardTop + row * cellSize
		newSelectedCell.onDown {
			println("$row, $col")
		}
		addChild(newSelectedCell)
		selectedCell = newSelectedCell



	}

	fun renderBoard() {
		for (row in 0..7) {
			for (col in 0..7) {
				val color = if ((row + col) % 2 == 0) white else black
				val cell = SolidRect(cellSize, cellSize, color)
				cell.x = boardLeft + col * cellSize
				cell.y = boardTop + row * cellSize
				cell.onDown {
					println(" $row , $col")
					renderSelectedCell(row, col, b)
				}
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
				cell.pieceIllustration.onDown {
					println("${cell.row}, ${cell.col}")
					renderSelectedCell(cell.row, cell.col, b)
				}
			}
		}
	}




	renderBoard()
	renderPieces()




}