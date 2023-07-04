package quoi.feur.arjensimulathor.fragments

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import org.json.JSONArray
import android.widget.ArrayAdapter
import quoi.feur.arjensimulathor.R
import quoi.feur.arjensimulathor.entities.HistoryEntry
import java.time.LocalDateTime

class HomeFragment : Fragment() {

    private lateinit var amount:EditText
    private lateinit var roller:Spinner
    private lateinit var commentEdit:EditText
    private lateinit var pref:SharedPreferences
    private lateinit var activity:Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        activity = requireActivity()
        pref = activity.applicationContext!!.getSharedPreferences("arjensim", Context.MODE_PRIVATE)!!

        var historyString = pref.getString("history", "")

        if(historyString == ""){
            historyString = JSONArray(arrayOf<HistoryEntry>()).toString()
            pref.edit().putString("history", historyString).apply()
        }

        HistoryEntry.all = HistoryEntry.createListFromJSONArray(JSONArray(historyString))


        commentEdit = view.findViewById(R.id.comment)
        amount = view.findViewById(R.id.expenseAmount)
        roller = view.findViewById(R.id.expensePerson)

        val personsAdapter = ArrayAdapter(activity.applicationContext, R.layout.spinner_item_home, arrayOf("Bouéfoubi", "Gobi"))
        roller.adapter = personsAdapter

        checkWhoIsRicher(view)

        commentEdit.setOnEditorActionListener{_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addNewEntry(view)
            }
            false
        }

        view.findViewById<Button>(R.id.submit).setOnClickListener{
            addNewEntry(view)
        }


        return view
    }

    private fun addNewEntry(view: View): Boolean{
        val moneyAmount = amount.text.toString()
        val person = roller.selectedItem.toString()
        if(moneyAmount ==  "" || person == ""){
            Toast.makeText(requireActivity().applicationContext, "Please input the person and the amount.", Toast.LENGTH_SHORT).show()
            return false
        }
        val newEntry = HistoryEntry(LocalDateTime.now(), person, moneyAmount.toDouble(), commentEdit.text.toString())
        if(HistoryEntry.addToAll(newEntry)) {
            pref.edit().remove("history").putString("history", HistoryEntry.allToJSON()).apply()

            checkWhoIsRicher(view)

            amount.text.clear()
            commentEdit.text.clear()
            return true
        }
        Toast.makeText(activity.applicationContext, "Il y a déjà une entrée similaire dans l'historique.", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun checkWhoIsRicher(view: View){
        val differenceList = HistoryEntry.getDifference()
        val richer = differenceList[0]
        val difference = differenceList[1].toDouble()
        val text = view.findViewById<TextView>(R.id.theWinner)
        val yellow = ForegroundColorSpan(resources.getColor(R.color.yellow, requireActivity().theme))

        if(difference != .0) {
            val message = SpannableString(String.format("%s est à +%.2f€", richer, difference))
            message.setSpan(
                yellow,
                message.indexOf("+"),
                message.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text.text = message
        }
        else{
            text.text = String.format("Égalité! Tout le monde a dépensé pareil!")
        }

    }

    override fun onResume() {
        super.onResume()
        checkWhoIsRicher(requireView())
    }
}