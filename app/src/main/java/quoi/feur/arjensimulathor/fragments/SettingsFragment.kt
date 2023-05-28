package quoi.feur.arjensimulathor.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.json.JSONArray
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.Entry


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)
        val activity = requireActivity()
        val pref = activity.applicationContext!!.getSharedPreferences("arjensim", Context.MODE_PRIVATE)!!
        val clipboardManager = activity.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        view.findViewById<Button>(R.id.exportButton).setOnClickListener {
            val clip = ClipData.newPlainText("Exported Arjen Simulathor history", Entry.allToJSON())
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(activity.applicationContext, "Historique copié dans le presse-papiers.", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.mergeButton).setOnClickListener{
            val editText = activity.findViewById<EditText>(R.id.editTextMerge)
            val mergeText = editText.text.toString()
            editText.text.clear()

            if(mergeText != "") {
                try {
                    val mergeList = Entry.createListFromJSONArray(JSONArray(mergeText))
                    Entry.mergeExternal(mergeList)
                    pref.edit().remove("history").putString("history", Entry.allToJSON()).apply()
                    Toast.makeText(activity.applicationContext, "La fusion a été effectuée!", Toast.LENGTH_SHORT).show()
                }catch (e: org.json.JSONException){
                    Toast.makeText(activity.applicationContext, "La fusion a échoué, essayer de ré-exporter les données.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(activity.applicationContext, "Veuillez entrer des données pour effecter une fusion.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}