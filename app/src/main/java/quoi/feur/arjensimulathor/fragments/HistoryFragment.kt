package quoi.feur.arjensimulathor.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AlertDialog
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.Entry


class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val activity = requireActivity()
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val pref = activity.applicationContext.getSharedPreferences("arjensim", Context.MODE_PRIVATE)
        val list = view.findViewById<ListView>(R.id.historyView)

        val data: ArrayList<Map<String, String>> = ArrayList();
        Entry.all.forEach{
            val datum = HashMap<String, String>()
            datum["first"] = it.toString()
            datum["second"] = "Commentaire : ".plus(it.comment)
            data.add(datum)
        }
        data.reverse()

        val adapter = SimpleAdapter(
            activity.applicationContext,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf<String>("first", "second"),
            intArrayOf(android.R.id.text1, android.R.id.text2)

        )
        list.adapter = adapter



        list.setOnItemClickListener { _, _, position, _ ->
            val alertDialog: AlertDialog = activity.let {
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


        return view
    }
}