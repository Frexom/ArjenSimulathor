package quoi.feur.arjensimulathor.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.Entry

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)



        val position : Int = intent.getIntExtra("entryPos", -1)
        val entry = Entry.all[position]
        val deleteButton = findViewById<Button>(R.id.deleteEntry)
        val detailsHeader = findViewById<TextView>(R.id.detailsHeader)
        val detailsSubheader = findViewById<TextView>(R.id.detailsSubheader)
        val fullComment = findViewById<TextView>(R.id.fullComment)

        val headerText = "${entry.person} : ${entry.getAmountString()}€"
        detailsHeader.text = headerText
        detailsSubheader.text = entry.getPrettyDate()
        fullComment.text = entry.getCleanComment()



        val pref = applicationContext.getSharedPreferences("arjensim", Context.MODE_PRIVATE)

        deleteButton.setOnClickListener {

            val alertDialog: AlertDialog = this.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Cette action est irrévsersible")
                    .setTitle("Vousez vous vraiment supprimer cette entrée?")

                builder.apply {

                    setPositiveButton("Oui"){ _, _ ->
                        Entry.all.removeAt(position)
                        pref.edit().remove("history").putString("history", Entry.allToJSON())
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
    }



}