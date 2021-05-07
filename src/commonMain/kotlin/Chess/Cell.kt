package Chess

import com.soywiz.korge.view.View

class Cell(row: Int, col: Int) {
    var piece: Piece = None();
    val row = row
    val col = col

    var pieceIllustration: View? = null

    override fun toString(): String {
        val rowNew = 8-row
        val colNew = 'A' + col
        return "$colNew${rowNew}"
    }

    fun getCoordinate(): Int {
        return row*8+col
    }
}