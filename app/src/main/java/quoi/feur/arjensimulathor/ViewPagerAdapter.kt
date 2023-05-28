package quoi.feur.arjensimulathor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import quoi.feur.arjensimulathor.fragments.GroceriesFragment
import quoi.feur.arjensimulathor.fragments.HistoryFragment
import quoi.feur.arjensimulathor.fragments.HomeFragment
import quoi.feur.arjensimulathor.fragments.SettingsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> GroceriesFragment()
            2 -> HistoryFragment()
            3 -> SettingsFragment()
            else -> HomeFragment()
        }
    }

}