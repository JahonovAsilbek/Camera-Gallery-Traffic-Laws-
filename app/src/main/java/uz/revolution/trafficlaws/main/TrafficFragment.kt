package uz.revolution.trafficlaws.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_traffic.view.*
import uz.revolution.trafficlaws.R
import uz.revolution.trafficlaws.daos.TrafficDao
import uz.revolution.trafficlaws.database.AppDatabase
import uz.revolution.trafficlaws.main.adapters.ItemTrafficAdapter
import uz.revolution.trafficlaws.models.Traffic


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
class TrafficFragment : Fragment() {

    private var categoryID: Int? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryID = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
        database = AppDatabase.get.getDatabse()
        trafficDao = database!!.trafficDao()
        adapter = ItemTrafficAdapter()
    }

    lateinit var root: View
    private var database: AppDatabase? = null
    private var trafficDao: TrafficDao? = null
    private var data: ArrayList<Traffic>? = null
    private var adapter: ItemTrafficAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_traffic, container, false)
        loadData()
        loadAdapters()
        likeClick()

        return root
    }

    private fun likeClick() {
        
        // like click not working*********************************************

        adapter?.setOnLikedClick(object :ItemTrafficAdapter.OnLikeClick{
            override fun onClick(traffic: Traffic, position: Int, imageView: ImageView) {
                Log.d("AAAA", "onLikeClick: ${traffic.liked}")
                val liked: Boolean = if (traffic.liked!!) {
                    imageView.setImageResource(R.drawable.ic_heart11)
                    false
                } else {
                    imageView.setImageResource(R.drawable.ic_heart1)
                    true
                }
                trafficDao?.updateLiked(liked, traffic.id!!)
                adapter?.notifyItemChanged(position)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_btn, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_btn -> findNavController().navigate(R.id.addTraffic)
        }
        return true
    }

    private fun loadAdapters() {
        adapter!!.setAdapter(data!!)
        root.traffic_rv.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    private fun loadData() {
        data = ArrayList()
        data = trafficDao?.getTrafficByCategory(categoryID!!) as ArrayList
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryID: Int, param2: String) =
            TrafficFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, categoryID)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}