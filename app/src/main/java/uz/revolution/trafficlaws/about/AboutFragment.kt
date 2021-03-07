package uz.revolution.trafficlaws.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_about.view.*
import kotlinx.android.synthetic.main.fragment_traffic.view.*
import kotlinx.android.synthetic.main.fragment_traffic.view.bottom_navigation
import kotlinx.android.synthetic.main.fragment_traffic.view.toolbar
import uz.revolution.trafficlaws.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AboutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_about, container, false)
        (activity as AppCompatActivity).setSupportActionBar(root.toolbar)

        val menu = root.bottom_navigation.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.info) {
                item.isChecked = true
            }
        }
        root.bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.startFragment)
                    it.isChecked = true
                }
                R.id.liked -> {
                    val bundle = Bundle()
                    bundle.putBoolean("liked",true)
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.trafficFragment,bundle)
                    it.isChecked = true
                }
            }

            true
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        val menu = root.bottom_navigation.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.info) {
                item.isChecked = true
            }
        }
    }


}