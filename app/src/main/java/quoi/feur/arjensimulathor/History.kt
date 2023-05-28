package quoi.feur.arjensimulathor

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import quoi.feur.arjensimulathor.entities.Entry


class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val pref = applicationContext.getSharedPreferences("arjensim", Context.MODE_PRIVATE)
        val list = findViewById<ListView>(R.id.historyView)

        val data: ArrayList<Map<String, String>> = ArrayList();
        Entry.all.forEach{
            val datum = HashMap<String, String>()
            datum["first"] = it.toString()
            datum["second"] = "Commentaire : ".plus(it.comment)
            data.add(datum)
        }
        data.reverse()

        val adapter = SimpleAdapter(
            this,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf<String>("first", "second"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        list.adapter = adapter



        list.setOnItemClickListener { _, _, position, _ ->
            val alertDialog: AlertDialog = this.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Cette action est irrévsersible")
                    .setTitle("Vousez vous vraiment supprimer cette entrée?")

                builder.apply {

                    setPositiveButton("Oui", DialogInterface.OnClickListener { _, _ ->

                        Entry.all.removeAt(data.size - position - 1)
                        data.removeAt(position)
                        adapter.notifyDataSetChanged()
                        pref.edit().remove("history").putString("history", Entry.allToJSON())
                            .apply()
                    })

                    setNegativeButton("Non, annuler", DialogInterface.OnClickListener { _, _ ->
                    })
                }
                builder.create()
            }
            alertDialog.show()
        }
    }
}