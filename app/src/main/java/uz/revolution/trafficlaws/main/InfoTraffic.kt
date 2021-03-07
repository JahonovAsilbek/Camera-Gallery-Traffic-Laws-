package uz.revolution.trafficlaws.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_traffic.view.*
import kotlinx.android.synthetic.main.fragment_add_traffic.view.image
import kotlinx.android.synthetic.main.fragment_add_traffic.view.name
import kotlinx.android.synthetic.main.fragment_info_traffic.view.*
import uz.revolution.trafficlaws.R
import uz.revolution.trafficlaws.models.Traffic
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "item"
private const val ARG_PARAM2 = "param2"

class InfoTraffic : Fragment() {
    // TODO: Rename and change types of parameters
    private var traffic: Traffic? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            traffic = it.getSerializable(ARG_PARAM1) as Traffic
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_info_traffic, container, false)
        (activity as AppCompatActivity).setSupportActionBar(root.toolbar_info).apply {
            root.toolbar_info.setNavigationIcon(R.drawable.ic_arrowleft1)
            root.toolbar_info.setNavigationOnClickListener {
                setHasOptionsMenu(false)
                setMenuVisibility(false)
                findNavController().popBackStack()
            }
        }

        val imgFile = File(traffic?.imagePath)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
            root.image_info.setImageBitmap(myBitmap)
        }
        root.name_info.text=traffic?.name
        root.info_info.text=traffic?.info

        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(traffic: String, param2: String) =
            InfoTraffic().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, traffic)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}