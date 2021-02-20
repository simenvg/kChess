package Chess

import com.soywiz.korge.view.View
import com.soywiz.korma.geom.Point

class Cell(coordinates: Point, row: Int, col: Int) {
    var piece: Piece = None();
    val row = row
    val col = col
    val coordinates = coordinates
    var pieceIllustration: View? = null


}