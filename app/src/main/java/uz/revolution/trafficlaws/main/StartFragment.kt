package uz.revolution.trafficlaws.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_start.view.*
import kotlinx.android.synthetic.main.fragment_start.view.bottom_navigation
import kotlinx.android.synthetic.main.fragment_start.view.toolbar
import kotlinx.android.synthetic.main.fragment_traffic.view.*
import kotlinx.android.synthetic.main.tab_item.view.*
import uz.revolution.trafficlaws.R
import uz.revolution.trafficlaws.main.adapters.TrafficMainAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    lateinit var root: View
    private var adapter: TrafficMainAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_start, container, false)

        (activity as AppCompatActivity).setSupportActionBar(root.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        setPermission()
        loadAdapters()
        setTabs()
        bottomNavigationClick()
        return root
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationClick()
    }

    private fun bottomNavigationClick() {
        val menu = root.bottom_navigation.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.home) {
                item.isChecked = true
            }
        }
        root.bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.liked -> {
                    val bundle = Bundle()
                    bundle.putBoolean("liked", true)
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.trafficFragment, bundle)
                    it.isChecked = true
                }
                R.id.info -> {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.aboutFragment)
                    it.isChecked = true
                }
            }

            true
        }
    }

//    private fun setPermission() {
//        Dexter.withContext(root.context)
//            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//            .withListener(object : PermissionListener {
//                override fun onPermissionGranted(response: PermissionGrantedResponse) {
//
//                }
//
//                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permission: PermissionRequest?,
//                    token: PermissionToken?
//                ) {
//
//                }
//            }).check()
//    }

    private fun loadAdapters() {
        adapter = TrafficMainAdapter(childFragmentManager)
        root.view_pager.adapter = adapter
        root.tab_layout.setupWithViewPager(root.view_pager)
    }

    @SuppressLint("SetTextI18n")
    private fun setTabs() {

        val tabCount = root.tab_layout.tabCount

        for (i in 0 until tabCount) {
            val tabBind = LayoutInflater.from(root.context).inflate(R.layout.tab_item, null, false)
            val tab = root.tab_layout?.getTabAt(i)
            tab?.customView = tabBind

            when (i) {
                0 -> tabBind.title_tv.text = "Ogohlantiruvchi"
                1 -> tabBind.title_tv.text = "Imtiyozli"
                2 -> tabBind.title_tv.text = "Ta'qiqlovchi"
                3 -> tabBind.title_tv.text = "Buyuruvchi"
            }
            if (i == 0) {
                tabBind.round.setImageResource(R.drawable.tab_corner_selected)
                tabBind.title_tv.setTextColor(resources.getColor(R.color.main_color))
            } else {
                tabBind.round.setImageResource(R.drawable.tab_corner)
                tabBind.title_tv.setTextColor(resources.getColor(R.color.white))
            }
        }



        root.tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.round?.setImageResource(R.drawable.tab_corner_selected)
                customView?.title_tv?.setTextColor(resources.getColor(R.color.main_color))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.round?.setImageResource(R.drawable.tab_corner)
                customView?.title_tv?.setTextColor(resources.getColor(R.color.white))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}