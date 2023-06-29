package quoi.feur.arjensimulathor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.HistoryEntry
import java.util.LinkedList

class HistoryAdapter(entries: LinkedList<HistoryEntry>, applicationContext: Context) : BaseAdapter() {

    private var entries: List<HistoryEntry> = entries
    private var inflater: LayoutInflater
    private var context: Context = applicationContext

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return entries.size
    }

    override fun getItem(position: Int): HistoryEntry {
        return entries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null) {
            view = inflater.inflate(R.layout.list_item_history, parent, false)
        }
        view!!

        val heading: TextView = view.findViewById(R.id.historyHeading)
        val subHeading: TextView = view.findViewById(R.id.historySubHeading)

        val item = entries[position]
        heading.text = item.toString()

        val comment = item.getCleanComment()
        val subheadingText = if(item.comment != ""){"Commentaire : ${comment}"}else{comment}
        subHeading.text = subheadingText
        return view
    }
}