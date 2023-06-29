package quoi.feur.arjensimulathor.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.activities.EntryActivity
import quoi.feur.arjensimulathor.adapters.HistoryAdapter
import quoi.feur.arjensimulathor.entities.HistoryEntry


class HistoryFragment : Fragment() {

    private var adapter: HistoryAdapter? = null
    private var optionsMenuButton: ImageButton? = null
    private var pref:SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity()
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        pref = activity.applicationContext!!.getSharedPreferences("arjensim", Context.MODE_PRIVATE)!!


        val list = view.findViewById<ListView>(R.id.historyView)


        adapter = HistoryAdapter(HistoryEntry.all, activity.applicationContext)
        list.adapter = adapter



        list.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(activity.applicationContext, EntryActivity::class.java)
            intent.putExtra("entryPos", position)
            startActivity(intent)
            dataChanged()
        }

        optionsMenuButton = view.findViewById<ImageButton>(R.id.optionsMenu)
        optionsMenuButton!!.setOnClickListener{
            optionsMenuCallback(activity)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }

    private fun optionsMenuCallback(activity: Activity){
        val popupMenu = PopupMenu(activity, optionsMenuButton)
        popupMenu.menuInflater.inflate(R.menu.menu_history, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if(it.itemId == R.id.exportHistory) {
                exportHistory(activity)
            }
            if(it.itemId == R.id.mergeHistory){
                showMergeHistoryDialog(activity)
            }

            true
        }
        popupMenu.show()
    }

    private fun exportHistory(activity: Activity){
        val clipboardManager = activity.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Exported Arjen Simulathor history", HistoryEntry.allToJSON())
        clipboardManager.setPrimaryClip(clip)
    }

    private fun showMergeHistoryDialog(activity: Activity){
        val dialog = Dialog(activity)

        dialog.setContentView(R.layout.popup_history_merge)
        dialog.findViewById<Button>(R.id.submitButton).setOnClickListener {
            mergeHistory(dialog, activity)
        }
        dialog.show()
    }
    private fun mergeHistory(dialog: Dialog, activity: Activity){
        val editText = dialog.findViewById<EditText>(R.id.historyMergeData)
        val mergeText = editText.text.toString()
        editText.text.clear()

        if(mergeText != "") {
            try {
                val mergeList = HistoryEntry.createListFromJSONArray(JSONArray(mergeText))
                val count = HistoryEntry.mergeExternal(mergeList)
                dataChanged()
                Toast.makeText(activity.applicationContext, "$count item(s) ont été importés!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }catch (e: org.json.JSONException){
                Toast.makeText(activity.applicationContext, "La fusion a échoué, essayer de ré-exporter les données.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(activity.applicationContext, "Veuillez entrer des données à fusionner.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dataChanged(){
        pref!!.edit().remove("history").putString("history", HistoryEntry.allToJSON()).apply()
        adapter!!.notifyDataSetChanged()
    }
}