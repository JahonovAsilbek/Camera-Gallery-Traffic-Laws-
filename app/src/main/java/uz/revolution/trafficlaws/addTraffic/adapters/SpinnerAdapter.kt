package uz.revolution.trafficlaws.addTraffic.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_spinner.view.*
import uz.revolution.trafficlaws.R

class SpinnerAdapter : BaseAdapter() {

    private var data: ArrayList<String>? = null

    fun setAdapter(data: ArrayList<String>) {
        this.data = data
    }

    override fun getCount(): Int = data!!.size

    override fun getItem(p0: Int): String {
        return data!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2?.context).inflate(R.layout.item_spinner, p2, false)

        view.tv_spinner.text = data!![p0]

        if (p0 == 0) {
            view.tv_spinner.setTextColor(Color.parseColor("#A8A3A3"))
        }

        return view
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }
}