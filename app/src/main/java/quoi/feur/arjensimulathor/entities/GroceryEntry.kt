package quoi.feur.arjensimulathor.entities

import org.json.JSONArray
import quoi.feur.arjensimulathor.entities.enums.GroceryUnit
import java.time.LocalDateTime
import java.util.*

class GroceryEntry(val amount:Double, val unit:GroceryUnit, val name:String, val datetime: LocalDateTime, var checked: Boolean) {


    companion object{
        var all: LinkedList<GroceryEntry> = LinkedList()

        fun createListFromJSONArray(array: JSONArray) : LinkedList<GroceryEntry> {
            val entryList = LinkedList<GroceryEntry>()
            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val amount = json.getDouble("amount")
                val unit = GroceryUnit.fromString(json.getString("unit"))
                val name = json.getString("name")
                val datetime = LocalDateTime.parse(json.getString("datetime"))
                val checked = json.getBoolean("checked")

                entryList.add(GroceryEntry(amount, unit, name, datetime, checked))
            }
            return entryList
        }

        fun allToJSON(): String{
            var json = "["
            all.forEach{entry ->
                json += "{\"amount\" : ${entry.amount}, \"unit\" : \"${entry.unit.value()}\", \"name\" : \"${entry.name}\", \"datetime\" : \"${entry.datetime}\", \"checked\" : \"${entry.checked}\"}, "
            }

            if(json.length > 5){
                json = json.substring(0, json.length-2)
            }
            json += "]"
            return json
        }

        fun exportUnchecked():String{
            var json = "["
            all.forEach { entry ->
                if (!entry.checked) {
                    json += "{\"amount\" : ${entry.amount}, \"unit\" : \"${entry.unit.value()}\", \"name\" : \"${entry.name}\", \"datetime\" : \"${entry.datetime}\", \"checked\" : \"${entry.checked}\"}, "
                }
            }

            if(json.length > 5){
                json = json.substring(0, json.length-2)
            }
            json += "]"
            return json
        }

        fun addToAll(entry: GroceryEntry): Boolean{
            all.add(entry)
            return true
        }

        fun importExternal(entriesToAdd: LinkedList<GroceryEntry>):Int{
            var counter = 0
            entriesToAdd.forEach{
                if(!isPresent(it)){
                    all.add(it)
                    counter++
                }
            }
            sortByDate()
            return counter
        }

        private fun isPresent(entry:GroceryEntry): Boolean{
            all.forEach {
                if(it.name == entry.name && it.amount == entry.amount && it.unit == entry.unit && it.datetime == entry.datetime)
                    return true
            }
            return false
        }

        private fun sortByDate(){
            all.sortBy {
                it.datetime
            }
        }

    }

    override fun toString(): String{

        val displayAmount = if(amount % 1 == 0.0){amount.toInt().toString()}else{amount.toString()}


        if(unit == GroceryUnit.NO_UNIT) {
            if(amount == 0.0)
                return name
            return "$displayAmount $name"
        }
        return "$displayAmount ${unit.prettyString()} de $name"
    }

    fun prettyDate(): String{
        var date =  "Ajouté le ${HistoryEntry.daysOfWeek[datetime.dayOfWeek.value-1]} ${datetime.dayOfMonth} ${HistoryEntry.months[datetime.month.value-1]} ${datetime.year}, "
        if(datetime.hour < 10){
            date += "0"
        }
        date += datetime.hour
        date += "h"
        if(datetime.minute < 10){
            date += "0"
        }
        date += datetime.minute
        return date
    }

}