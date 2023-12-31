package quoi.feur.arjensimulathor.entities

import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.math.abs

class HistoryEntry(val datetime: LocalDateTime, val person: String, val amount: Double, val comment: String){

    companion object{
        var all: LinkedList<HistoryEntry> = LinkedList()
        val daysOfWeek = arrayOf("lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche")
        val months = arrayOf("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Spetembre", "Octobre", "Novembre", "Décembre")
        fun createListFromJSONArray(array: JSONArray) : LinkedList<HistoryEntry> {
            val entryList = LinkedList<HistoryEntry>()
            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val datetime = LocalDateTime.parse(json.getString("datetime"))
                val person = json.getString("person")
                val amount = json.getDouble("amount")
                val comment = json.getString("comment")

                entryList.add(HistoryEntry(datetime, person, amount, comment))
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
            var gobiMoney = .0
            var bouefoubiMoney = .0
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

        fun addToAll(entry: HistoryEntry): Boolean{
            if(!checkIfPresent(entry)) {
                all.add(entry)
                sortByDate()
                return true
            }
            return false
        }

        private fun checkIfPresent(entry: HistoryEntry) : Boolean{
            all.forEach{
                if(it.person == entry.person && it.amount == entry.amount){
                    if(it.datetime.year == entry.datetime.year && it.datetime.dayOfYear == entry.datetime.dayOfYear){
                        return true
                    }
                }
            }
            return false
        }

        fun mergeExternal(mergeList: LinkedList<HistoryEntry>):Int{
            var counter = 0
            mergeList.forEach{mergeItem ->
                if(!checkIfPresent(mergeItem)){
                    all.add(mergeItem)
                    counter++
                }
            }
            sortByDate()
            return counter
        }

        private fun sortByDate(){
            all.sortByDescending {
                it.datetime
            }
        }

    }

    override fun toString(): String{
        return "${person}, ${datetime.dayOfMonth} ${datetime.month.getDisplayName(TextStyle.FULL, Locale.FRANCE)} ${datetime.year} : ${getAmountString()}€"
    }

    fun getAmountString(): String {
        return "%.2f".format(amount)
    }

    fun getPrettyDate(): String{
        var date =  "${daysOfWeek[datetime.dayOfWeek.value-1]} ${datetime.dayOfMonth} ${months[datetime.month.value-1]} ${datetime.year}, "
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

    fun getCleanComment(): String{
        return if(comment != ""){comment}else{"Pas de commentaire"}
    }
}