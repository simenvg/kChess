package Chess

open class Piece(color: Color) {
    val color = color

    protected open fun move(to: String){
        println("Move to $to")
    }

    fun getPieceChar(): String {
        when (this) {
            is Queen -> return "Q"
            is King -> return "K"
            is Rook -> return "R"
            is Knight -> return "N"
            is Bishop -> return "B"
            is Pawn -> return "P"
        }
        return "0"
    }

    fun getPiecePngFileName(): String? {
        val firstLetter = if (this.color == Color.WHITE) "w" else "b"
        val secondLetter = this.getPieceChar()
        return if (secondLetter != "0") "pieces/$firstLetter$secondLetter.png"
        else {
            null
        }
    }
}

class Queen(color: Color) : Piece(color) {
    public override fun move(to: String) {
        println("MOVE Queen to: $to")
    }
}

class Pawn(color: Color) : Piece(color) {
    public override fun move(to: String) {
        println("MOVE Queen to: $to")
    }
}

class King(color: Color) : Piece(color) {
    public override fun move(to: String) {
        println("MOVE Queen to: $to")
    }
}

class Bishop(color: Color) : Piece(color) {
    public override fun move(to: String) {
        println("MOVE Queen to: $to")
    }
}

class Knight(color: Color) : Piece(color) {
    public override fun move(to: String) {
        println("MOVE Queen to: $to")
    }
}

class Rook(color: Color) : Piece(color) {
    public override fun move(to: String) {
        println("MOVE Queen to: $to")
    }
}

class None() : Piece(Color.NONE)
