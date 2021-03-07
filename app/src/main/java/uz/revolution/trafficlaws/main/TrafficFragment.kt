package uz.revolution.trafficlaws.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_traffic.view.*
import uz.revolution.trafficlaws.R
import uz.revolution.trafficlaws.daos.TrafficDao
import uz.revolution.trafficlaws.database.AppDatabase
import uz.revolution.trafficlaws.main.adapters.ItemTrafficAdapter
import uz.revolution.trafficlaws.models.Traffic


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "liked"

@Suppress("DEPRECATION")
class TrafficFragment : Fragment() {

    private var categoryID: Int? = null
    private var liked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryID = it.getInt(ARG_PARAM1)
            liked = it.getBoolean(ARG_PARAM2)
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
        Log.d("AAAA", "liked: $liked")
        bottomNavigationClick()
        setToolbar()
        loadData()
        loadAdapters()
        likeClick()
        deleteClick()
        editClick()
        itemClick()

        return root
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationClick()
    }

    private fun bottomNavigationClick() {
        if (liked!!) {
            val menu = root.bottom_navigation.menu
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                if (item.itemId == R.id.liked) {
                    item.isChecked = true
                }
            }
            root.bottom_navigation.visibility = View.VISIBLE
            root.bottom_navigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.startFragment)
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
        } else {
            root.bottom_navigation.visibility = View.GONE
        }

    }

    private fun setToolbar() {
        if (liked!!) {
            root.toolbar.visibility = View.VISIBLE
            (activity as AppCompatActivity).setSupportActionBar(root.toolbar)
            root.toolbar.navigationIcon = null
            setHasOptionsMenu(false)
            setHasOptionsMenu(false)
        } else {
            root.toolbar.visibility = View.GONE
        }
    }

    private fun itemClick() {
        adapter?.setOnItemClick(object : ItemTrafficAdapter.OnItemClick {
            override fun onClick(traffic: Traffic, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("item", traffic)
                findNavController().navigate(R.id.infoTraffic, bundle)
            }
        })
    }

    private fun editClick() {
        adapter?.setOnEditClick(object : ItemTrafficAdapter.OnEditClick {
            override fun onClick(traffic: Traffic, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("traffic", traffic)
                findNavController().navigate(R.id.addTraffic, bundle)
            }
        })
    }

    private fun deleteClick() {
        adapter?.setOnDeleteClick(object : ItemTrafficAdapter.OnDeleteClick {
            override fun onClick(traffic: Traffic, position: Int) {
                val dialog = AlertDialog.Builder(root.context)

                dialog.setTitle("Qoidani o'chirish")
                dialog.setMessage("Rostdan ham bu xabarni o'chirmoqchimisiz?")
                dialog.setPositiveButton("Ha", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        trafficDao?.deleteTraffic(traffic)
                        loadData()
                        loadAdapters()
                        adapter?.notifyItemRemoved(position)
                        adapter?.notifyItemRangeChanged(position, data!!.size)
                        p0?.cancel()
                    }
                })
                dialog.setNegativeButton("Yo'q", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.cancel()
                    }

                })
                dialog.show()
            }

        })
    }

    private fun likeClick() {

        // like click not working*********************************************

        adapter?.setOnLikedClick(object : ItemTrafficAdapter.OnLikeClick {
            override fun onClick(traffic: Traffic, position: Int, imageView: ImageView) {
                if (liked!!) {
                    trafficDao?.updateLiked(false, traffic.id!!)
                    loadData()
                    loadAdapters()
                } else {
                    val liked: Boolean = if (traffic.liked!!) {
                        imageView.setImageResource(R.drawable.ic_heart11)
                        adapter?.notifyItemChanged(position)
                        false
                    } else {
                        imageView.setImageResource(R.drawable.ic_heart1)
                        adapter?.notifyItemChanged(position)
                        true
                    }
                    trafficDao?.updateLiked(liked, traffic.id!!)
                    loadData()
                    loadAdapters()
                    adapter?.notifyItemChanged(position)
                }
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
    }

    private fun loadData() {
        data = ArrayList()
        if (liked!!) {
            data = trafficDao?.getTrafficByLiked(liked!!) as ArrayList
        } else {
            data = trafficDao?.getTrafficByCategory(categoryID!!) as ArrayList
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryID: Int, liked: Boolean) =
            TrafficFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, categoryID)
                    putBoolean(ARG_PARAM2, liked)
                }
            }
    }
}