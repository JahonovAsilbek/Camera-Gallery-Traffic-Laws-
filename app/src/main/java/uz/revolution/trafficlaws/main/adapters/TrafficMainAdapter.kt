package uz.revolution.trafficlaws.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.revolution.trafficlaws.main.TrafficFragment
import uz.revolution.trafficlaws.models.Traffic

class TrafficMainAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT

    ) {

    override fun getCount(): Int = 4


    override fun getItem(position: Int): Fragment {
        return TrafficFragment.newInstance(position + 1, "")
    }
}