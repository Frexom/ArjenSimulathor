package quoi.feur.arjensimulathor.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.Entry
import java.util.LinkedList

class HistoryAdapter: BaseAdapter {

    private var entries: List<Entry>
    private var inflater: LayoutInflater
    private var context: Context

    constructor(entries: LinkedList<Entry>, applicationContext: Context){
        this.entries = entries
        this.context = applicationContext
        this.inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return entries.size
    }

    override fun getItem(position: Int): Entry {
        return entries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView = inflater.inflate(R.layout.history_list_item, null)
        val heading: TextView = convertView.findViewById(R.id.historyHeading)
        val subHeading: TextView = convertView.findViewById(R.id.historySubHeading)

        val item = entries[entries.size-1-position]
        heading.text = item.toString()
        subHeading.text = "Commentaire : ".plus(item.comment)
        return convertView
    }
}