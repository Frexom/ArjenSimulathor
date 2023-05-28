package quoi.feur.arjensimulathor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import quoi.feur.arjensimulathor.entities.Entry
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.setBackgroundColor(getColor(R.color.black))
        val pref = applicationContext.getSharedPreferences("arjensim", Context.MODE_PRIVATE)

        var historyString = pref.getString("history", "")

        if(historyString == ""){
            historyString = JSONArray(arrayOf<Entry>()).toString()
            pref.edit().putString("history", historyString).apply()
        }

        Entry.all = Entry.createListFromJSONArray(JSONArray(historyString))


        val commentEdit = findViewById<EditText>(R.id.comment)
        val amount = findViewById<EditText>(R.id.expenseAmount)

        val roller = findViewById<Spinner>(R.id.expensePerson)
        val personsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("Bouéfoubi", "Gobi"))
        roller.adapter = personsAdapter

        checkWhoIsRicher()

        findViewById<Button>(R.id.submit).setOnClickListener{
            val moneyAmount = amount.text.toString()
            val person = roller.selectedItem.toString()
            if(moneyAmount ==  "" || person == ""){
                Toast.makeText(applicationContext, "Please input the person and the amount.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Entry.all.add(Entry(LocalDate.now(), person, moneyAmount.toDouble(), commentEdit.text.toString()))

            pref.edit().remove("history").putString("history", Entry.allToJSON()).apply()

            checkWhoIsRicher()

            amount.text.clear()
            commentEdit.text.clear()
        }

        findViewById<Button>(R.id.history).setOnClickListener{
            val intent = Intent(this, History::class.java)
            startActivity(intent)
        }



    }

    private fun checkWhoIsRicher(){
        val differenceList = Entry.getDifference()
        val richer = differenceList[0]
        val difference = differenceList[1].toDouble()
        val text = findViewById<TextView>(R.id.theWinner)
        val yellow = ForegroundColorSpan(resources.getColor(R.color.yellow, theme))

        if(difference != .0) {
            val message = SpannableString(String.format("%s est à +%.2f€", richer, difference))
            message.setSpan(
                yellow,
                message.indexOf("+"),
                message.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text.text = message
        }
        else{
            text.text = String.format("Égalité! Tout le monde a dépensé pareil!")
        }

    }

    override fun onResume() {
        super.onResume()
        checkWhoIsRicher()
    }
}