package quoi.feur.arjensimulathor.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.adapters.GroceryAdapter
import quoi.feur.arjensimulathor.entities.GroceryEntry
import quoi.feur.arjensimulathor.entities.enums.GroceryUnit
import java.time.LocalDateTime

class GroceriesFragment : Fragment() {

    private lateinit var adapter: GroceryAdapter
    private lateinit var optionsMenuButton: ImageButton
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groceries, container, false)

        pref = requireActivity().applicationContext!!.getSharedPreferences("arjensim", Context.MODE_PRIVATE)!!
        var groceriesString = pref.getString("groceries", "")

        if(groceriesString == ""){
            groceriesString = JSONArray(arrayOf<GroceryEntry>()).toString()
            pref.edit().putString("groceries", groceriesString).apply()
        }

        GroceryEntry.all = GroceryEntry.createListFromJSONArray(JSONArray(groceriesString))

        val list = view.findViewById<ListView>(R.id.groceriesView)


        adapter = GroceryAdapter(GroceryEntry.all, requireActivity().applicationContext)
        list.adapter = adapter



        // Assigning callbacks
        list.setOnItemClickListener { _, _, position, _ ->
        }

        view.findViewById<Button>(R.id.addButton).setOnClickListener {
            addNewEntryDialog()
        }

        optionsMenuButton = view.findViewById(R.id.optionsMenu)
        optionsMenuButton.setOnClickListener{
            optionsMenuCallback()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun addNewEntryDialog(){
        val activity = requireActivity()
        val dialog = Dialog(activity)

        dialog.setContentView(R.layout.popup_groceries_add)

        val amountInput = dialog.findViewById<EditText>(R.id.inputGroceryAmount)
        val nameInput =  dialog.findViewById<EditText>(R.id.inputGroceryName)
        val unitSpinner = dialog.findViewById<Spinner>(R.id.spinnerGroceryUnit)
        unitSpinner.adapter = ArrayAdapter(activity.applicationContext, R.layout.spinner_item_home, GroceryUnit.array())

        dialog.findViewById<Button>(R.id.submitButton).setOnClickListener {
            var amount = amountInput.text.toString()
            val unit = unitSpinner.selectedItem as GroceryUnit
            val name = nameInput.text.toString()
            if(name == ""){
                Toast.makeText(activity.applicationContext, "Veuillez remplir la quantité et le nom.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(amount == "" && unit != GroceryUnit.NO_UNIT){
                Toast.makeText(activity.applicationContext, "Une unité ne peut être spécifiée sans quantité.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(amount == ""){
                amount = "0"
            }

            val newEntry = GroceryEntry(amount.toDouble(), unit, name, LocalDateTime.now(), false)
            GroceryEntry.addToAll(newEntry)
            dataChanged()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun optionsMenuCallback(){
        val popupMenu = PopupMenu(requireActivity(), optionsMenuButton)
        popupMenu.menuInflater.inflate(R.menu.menu_groceries, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if(it.itemId == R.id.deleteCheckedEntries) {
                deleteCheckedEntries()
            }
            if(it.itemId == R.id.exportUncheckedEntries){
                exportUncheckedEntries()
            }
            if(it.itemId == R.id.importExternalEntries){
                showImportDialog()
            }


            true
        }
        popupMenu.show()
    }

    private fun deleteCheckedEntries(){
        for (i in GroceryEntry.all.size - 1 downTo 0) {
            if (GroceryEntry.all[i].checked) {
                GroceryEntry.all.removeAt(i)
            }
        }
        dataChanged()
    }

    private fun exportUncheckedEntries(){
        val clipboardManager = requireActivity().applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Exported Arjen Simulathor groceries list", GroceryEntry.exportUnchecked())
        clipboardManager.setPrimaryClip(clip)
    }

    private fun showImportDialog(){
        val activity = requireActivity()


        val dialog = Dialog(activity)

        dialog.setContentView(R.layout.popup_groceries_import)
        dialog.findViewById<Button>(R.id.submitButton).setOnClickListener {
            importExternalEntries(dialog, activity)
        }
        dialog.show()
    }

    private fun importExternalEntries(dialog: Dialog, activity: Activity){
        val editText = dialog.findViewById<EditText>(R.id.groceriesImportData)
        val importText = editText.text.toString()
        editText.text.clear()

        if(importText != "") {
            try {
                val entriesToAdd = GroceryEntry.createListFromJSONArray(JSONArray(importText))
                val count = GroceryEntry.importExternal(entriesToAdd)


                dataChanged()
                Toast.makeText(activity.applicationContext, "$count item(s) ont été importés!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }catch (e: org.json.JSONException){
                Toast.makeText(activity.applicationContext, "L'import a échoué, essayer de ré-exporter les données.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(activity.applicationContext, "Veuillez entrer des données à importer.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dataChanged(){
        pref.edit().remove("groceries").putString("groceries", GroceryEntry.allToJSON()).apply()
        adapter.notifyDataSetChanged()
    }
}