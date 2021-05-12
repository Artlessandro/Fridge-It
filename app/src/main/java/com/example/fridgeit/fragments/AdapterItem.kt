package com.example.fridgeit.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgeit.DatabaseHelper
import com.example.fridgeit.ModelItem
import com.example.fridgeit.R
import kotlinx.android.synthetic.main.add_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class AdapterItem():RecyclerView.Adapter<AdapterItem.HolderItem>(), DatePickerDialog.OnDateSetListener {

    private var context:Context? = null
    private var itemList : ArrayList<ModelItem>? = null
    var day = 0
    var month = 0
    var year = 0
    var date :String = ""
    var name:String = ""
    var choose:Int = 1

    lateinit var dbHelper: DatabaseHelper
    constructor(context: Context, itemList: ArrayList<ModelItem>?):this()
    {
        this.context = context
        this.itemList = itemList
    }

    class HolderItem(itemView: View):RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tvItemName)
        var daysLeft: TextView = itemView.findViewById(R.id.tvDaysLeft)
        var expireDate: TextView = itemView.findViewById(R.id.tvExpireDate)
        var progress: ProgressBar = itemView.findViewById(R.id.progressBar)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderItem {
        return HolderItem(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: HolderItem, position: Int) {
        dbHelper = DatabaseHelper(context)
        val c = Calendar.getInstance().time
        //  println("Current time => $c")

        val df = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
       val currentDate = df.format(c)
        Log.i("currentDate",currentDate)
        val modelItem = itemList!!.get(position)
        val id = modelItem.id
        val name = modelItem.name
        val createDate = modelItem.createDate
        val expireDate = modelItem.expireDate
        holder.name.text = name

        holder.expireDate.text = expireDate
        val totalDays = dateDifference(createDate, expireDate)?.toDouble()
       // holder.daysLeft.text = totalDays?.toInt().toString()+" Days Left"
        val leftDays = dateDifference(currentDate, expireDate)?.toDouble()
        holder.daysLeft.text = leftDays?.toInt().toString()+" Days Left"
        Log.i("leftDays", leftDays.toString())
        holder.progress.max = 100
        val i = leftDays?.div(totalDays!!)


//        val k = m!!.toDouble() / i!!.toDouble()
        val t = (i?.times(100))
        Log.i("n",t.toString())
        if (t != null) {
            holder.progress.progress = t.toInt()
        }
            //leftDays!!.toInt()
        holder.itemView.setOnClickListener {
            Toast.makeText(context,""+modelItem.id,Toast.LENGTH_SHORT).show()
            updateData(id,name,expireDate,position, itemList!!)

        }
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    fun dateDifference(createDate: String, expireDate: String): Int?
    {
        val sdf = SimpleDateFormat("dd-M-yyyy")
        try {

            val d1: Date = sdf.parse(createDate)
            val d2: Date = sdf.parse(expireDate)
            val difference_In_Time = d2.time - d1.time
            val difference_In_Days = ((difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365)
            Log.i("difference", difference_In_Days.toString())
            return difference_In_Days.toInt()

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        return null
    }

    override fun onDateSet(view: DatePicker?, year1: Int, monthD: Int, dayOfMonth: Int) {
        val monthD =month+1;
        date = "$dayOfMonth-$monthD-$year1"
    }
    fun updateData(
        id: String,
        itemName: String,
        dateExpire: String,
        position: Int,
        itemList: ArrayList<ModelItem>
    )
    {


        choose = 1
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.update_item, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mDialogView.etName.setText(itemName)
        date = dateExpire
        mDialogView.btnDate.setOnClickListener {
            getDateCalender()
            DatePickerDialog(context!!, this, year, month, day).show()

        }
        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
            val check = dbHelper.deleteItem(id)
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
        mDialogView.btnOK.setOnClickListener {
            name = mDialogView.etName.text.toString().trim()
            if (date.isNotEmpty() && name.isNotEmpty())
            {
                mAlertDialog.dismiss()
                Log.i("data", "$name $date $choose")

                val check = dbHelper.updateIte(id,name,date)
                notifyDataSetChanged()
                Toast.makeText(context,"Id: "+id+" Name: "+name+"\n"+"Expire :"+date,Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, "Enter name and choose date", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getDateCalender()
    {
        val cal :Calendar= Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

}