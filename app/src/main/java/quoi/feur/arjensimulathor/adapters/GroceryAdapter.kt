package quoi.feur.arjensimulathor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.GroceryEntry
import quoi.feur.arjensimulathor.entities.HistoryEntry
import java.util.LinkedList

class GroceryAdapter(entries: LinkedList<GroceryEntry>, applicationContext: Context) : BaseAdapter() {

    private var entries: List<GroceryEntry> = entries
    private var inflater: LayoutInflater
    private var context: Context = applicationContext

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return entries.size
    }

    override fun getItem(position: Int): GroceryEntry {
        return entries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null) {
            view = inflater.inflate(R.layout.grocery_list_item, parent, false)
        }
        view!!

        val heading: TextView = view.findViewById(R.id.groceryHeading)
        val subHeading: TextView = view.findViewById(R.id.grocerySubHeading)

        val item = entries[position]
        heading.text = item.toString()

        val subheadingText = item.prettyDate()
        subHeading.text = subheadingText

        val checkbox = view.findViewById<CheckBox>(R.id.groceriesCheckbox)
        if(item.checked){
            checkbox.isChecked = true
        }

        val pref = context.getSharedPreferences("arjensim", Context.MODE_PRIVATE)!!
        checkbox.setOnClickListener {
            item.checked = checkbox.isChecked
            pref.edit().remove("groceries").putString("groceries", GroceryEntry.allToJSON()).apply()
        }

        return view
    }
}