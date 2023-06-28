package quoi.feur.arjensimulathor.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.HistoryEntry

class EntryActivity : AppCompatActivity() {

    var entryPosition : Int? = null
    var entry : HistoryEntry? = null
    var pref : SharedPreferences? = null

    var deleteButton : Button? = null
    var detailsHeader : TextView? = null
    var detailsSubheader : TextView? = null
    var fullComment : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        entryPosition = intent.getIntExtra("entryPos", -1)
        entry = HistoryEntry.all[entryPosition!!]
        pref = applicationContext.getSharedPreferences("arjensim", Context.MODE_PRIVATE)


        // Obtaining views
        deleteButton = findViewById(R.id.deleteEntry)
        detailsHeader = findViewById(R.id.detailsHeader)
        detailsSubheader = findViewById(R.id.detailsSubheader)
        fullComment = findViewById(R.id.fullComment)


        initTextViews()

        // Assigning callbacks
        deleteButton!!.setOnClickListener {
            deleteEntry()
        }
    }

    private fun deleteEntry(){
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Cette action est irrévsersible")
                .setTitle("Vousez vous vraiment supprimer cette entrée?")

            builder.apply {

                setPositiveButton("Oui"){ _, _ ->
                    HistoryEntry.all.removeAt(entryPosition!!)
                    pref!!.edit().remove("history").putString("history", HistoryEntry.allToJSON())
                        .apply()
                    finish()
                }

                setNegativeButton("Non, annuler"){ _, _ ->
                }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun initTextViews(){
        val headerText = "${entry!!.person} : ${entry!!.getAmountString()}€"
        detailsHeader!!.text = headerText
        detailsSubheader!!.text = entry!!.getPrettyDate()
        fullComment!!.text = entry!!.getCleanComment()
    }
}