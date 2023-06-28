package quoi.feur.arjensimulathor.entities.enums

enum class GroceryUnit {
    GRAM, KILOGRAM, LITRE, MILLILITRE, NO_UNIT;

    companion object{
        fun fromString(string: String): GroceryUnit{
            return when(string){
                "GRAM" -> GRAM
                "g" -> GRAM
                "KILOGRAM" -> KILOGRAM
                "kg" -> KILOGRAM
                "LITRE" -> LITRE
                "l" -> LITRE
                "MILLILITRE" -> MILLILITRE
                "ml" -> MILLILITRE
                "NO_UNIT" -> NO_UNIT
                "-" -> NO_UNIT
                else -> {
                    throw IllegalArgumentException("Invalid string passed to GroceryUnit class")
                }
            }
        }

        fun array(): Array<GroceryUnit>{
            return arrayOf(NO_UNIT, GRAM, KILOGRAM, LITRE, MILLILITRE)
        }
    }

    fun value():String{
        return when(this){
            GRAM -> "GRAM"
            KILOGRAM -> "KILOGRAM"
            LITRE -> "LITRE"
            MILLILITRE -> "MILLILITRE"
            NO_UNIT -> "NO_UNIT"
        }
    }

    fun prettyString(): String {
        return when(this){
            GRAM -> "Gramme(s)"
            KILOGRAM -> "Kilogramme(s)"
            LITRE -> "Litre(s)"
            MILLILITRE -> "Millilitre(s)"
            NO_UNIT -> ""
        }
    }

    override fun toString(): String {
        return when(this){
            GRAM -> "g"
            KILOGRAM -> "kg"
            LITRE -> "l"
            MILLILITRE -> "ml"
            NO_UNIT -> "-"
        }
    }


}