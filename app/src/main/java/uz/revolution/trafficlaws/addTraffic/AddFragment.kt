package uz.revolution.trafficlaws.addTraffic

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_traffic.view.*
import kotlinx.android.synthetic.main.fragment_add_traffic.view.image
import kotlinx.android.synthetic.main.fragment_add_traffic.view.name
import kotlinx.android.synthetic.main.item_traffic.view.*
import uz.revolution.trafficlaws.R
import uz.revolution.trafficlaws.addTraffic.adapters.SpinnerAdapter
import uz.revolution.trafficlaws.daos.TrafficDao
import uz.revolution.trafficlaws.database.AppDatabase
import uz.revolution.trafficlaws.models.Traffic
import java.io.File
import java.io.FileOutputStream

private const val ARG_PARAM1 = "traffic"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {

    private var traffic: Traffic? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            traffic = it.getSerializable(ARG_PARAM1) as Traffic?
            param2 = it.getString(ARG_PARAM2)
        }
        database = AppDatabase.get.getDatabse()
        trafficDao = database!!.trafficDao()
    }

    lateinit var root: View
    private var requestCOde = 1
    private var uri: Uri? = null
    private var absolutePath: String? = null
    private var database: AppDatabase? = null
    private var trafficDao: TrafficDao? = null
    private var categoryList:ArrayList<String>?=null
    private var adapter:SpinnerAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_add_traffic, container, false)
        if (traffic != null) {
            loadDataToView()
        }
        setToolbar()
        setImage()
        setSpinner()
        saveClick()

        return root
    }

    private fun loadDataToView() {
        val imgFile = File(traffic?.imagePath)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
            root.image.setImageBitmap(myBitmap)
        }
        absolutePath=traffic?.imagePath
        root.name.setText(traffic?.name)
        root.info.setText(traffic?.info)
    }

    private fun saveClick() {
        root.save.setOnClickListener {
            val imagePath=absolutePath
            val name=root.name.text.toString()
            val info=root.info.text.toString()
            val category= categoryList!![root.spinner.selectedItemPosition]

            if (imagePath!=null && name.isNotEmpty() && info.isNotEmpty() && !category.equals(
                    "Qaysi turga kirishi",
                    true
                )
            ) {
                if (traffic == null) {
                    trafficDao?.insertTraffic(
                        Traffic(
                            name,
                            info,
                            imagePath,
                            categoryList?.indexOf(category)!!,
                            false
                        )
                    )
                } else {
                    trafficDao?.updateTraffic(
                        name,
                        info,
                        imagePath,
                        categoryList?.indexOf(category)!!,
                        traffic?.liked!!,
                        traffic?.id!!
                    )
                }
                findNavController().popBackStack()
            } else {
                Snackbar.make(root,"Barcha maydonlarni to'ldiring",Snackbar.LENGTH_LONG).show()
            }
            Log.d("AAAA", "category: ${categoryList?.indexOf(category)!!}")
        }
    }

    private fun setSpinner() {
        categoryList = ArrayList()
        categoryList?.add("Qaysi turga kirishi")
        categoryList?.add("Ogohlantiruvchi")
        categoryList?.add("Imtiyozli")
        categoryList?.add("Ta'qiqlovchi")
        categoryList?.add("Buyuruvchi")
        adapter = SpinnerAdapter()
        adapter?.setAdapter(categoryList!!)
        root.spinner.adapter=adapter
        if (traffic != null) {
            root.spinner.setSelection(traffic?.category!!,true)
        }

    }

    private fun setImage() {
        root.image.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, requestCOde)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var id = 0
        if (trafficDao?.getAllTraffic()!!.isEmpty()) {
            id = 0
        } else {
            id = trafficDao?.getTraffic(trafficDao?.getAllTraffic()!!.size)?.id!!
        }

        if (requestCode == requestCOde && resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            root.image.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            val file = File(requireActivity().filesDir, "image${id}.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            absolutePath = file.absolutePath
            Toast.makeText(root.context, "$absolutePath", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setToolbar() {
        setHasOptionsMenu(false)
        setMenuVisibility(false)
        (activity as AppCompatActivity).setSupportActionBar(root.toolbar)
        (activity as AppCompatActivity).supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrowleft1)
        }

        root.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(traffic: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, traffic)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}