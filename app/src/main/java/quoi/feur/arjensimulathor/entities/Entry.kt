package quoi.feur.arjensimulathor.entities

import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.math.abs

class Entry(val datetime: LocalDateTime, val person: String, val amount: Double, val comment: String){

    companion object{
        var all: LinkedList<Entry> = LinkedList()

        fun createListFromJSONArray(array: JSONArray) : LinkedList<Entry> {
            val entryList = LinkedList<Entry>()
            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val datetime = LocalDateTime.parse(json.getString("datetime"))
                val person = json.getString("person")
                val amount = json.getDouble("amount")
                val comment = json.getString("comment")

                entryList.add(Entry(datetime, person, amount, comment))
            }
            return entryList
        }

        fun allToJSON(): String{
            var json = "["
            all.forEach{entry ->
                json += "{\"datetime\" : \"${entry.datetime}\", \"person\" : \"${entry.person}\", \"amount\" : ${entry.amount}, \"comment\" : \"${entry.comment}\"}, "
            }

            if(json.length > 5){
                json = json.substring(0, json.length-2)
            }
            json += "]"
            return json
        }

        fun getDifference(): LinkedList<String>{
            var gobiMoney: Double = .0
            var bouefoubiMoney: Double = .0
            all.forEach{
                if(it.person == "Gobi"){
                    gobiMoney += it.amount
                }
                if(it.person == "Bouéfoubi"){
                    bouefoubiMoney += it.amount
                }
            }
            val richer = if(bouefoubiMoney > gobiMoney){"Bouéfoubi"} else {"Gobi"}
            val result = LinkedList<String>()
            result.add(richer)
            result.add(abs(gobiMoney - bouefoubiMoney).toString())
            return result
        }

        fun addToAll(entry: Entry): Boolean{
            if(!checkIfPresent(entry)) {
                all.add(entry)
                sortByDate()
                return true
            }
            return false
        }

        private fun checkIfPresent(entry: Entry) : Boolean{
            all.forEach{
                if(it.person == entry.person && it.amount == entry.amount){
                    if(it.datetime.year == entry.datetime.year && it.datetime.dayOfYear == entry.datetime.dayOfYear){
                        return true
                    }
                }
            }
            return false
        }

        fun mergeExternal(mergeList: LinkedList<Entry>){
            mergeList.forEach{mergeItem ->
                if(!checkIfPresent(mergeItem)){
                    all.add(mergeItem)
                }
            }
            sortByDate()
        }

        private fun sortByDate(){
            all.sortByDescending {
                it.datetime
            }
        }

    }

    override fun toString(): String{
        return "${person}, ${datetime.dayOfMonth} ${datetime.month.getDisplayName(TextStyle.FULL, Locale.FRANCE)} ${datetime.year} : ${amount}€"
    }
}