package quoi.feur.arjensimulathor.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.activities.EntryActivity
import quoi.feur.arjensimulathor.adapters.HistoryAdapter
import quoi.feur.arjensimulathor.entities.HistoryEntry


class HistoryFragment : Fragment() {

    private var adapter: HistoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity()
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val list = view.findViewById<ListView>(R.id.historyView)


        adapter = HistoryAdapter(HistoryEntry.all, activity.applicationContext)
        list.adapter = adapter



        list.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(activity.applicationContext, EntryActivity::class.java)
            intent.putExtra("entryPos", position)
            startActivity(intent)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }
}