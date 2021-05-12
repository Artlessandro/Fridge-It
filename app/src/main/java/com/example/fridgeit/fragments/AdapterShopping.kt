package com.example.fridgeit.fragments

import android.app.AlertDialog

import android.content.Context

import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgeit.*
import kotlinx.android.synthetic.main.shopping_item.view.*
import java.util.ArrayList

class AdapterShopping() : RecyclerView.Adapter<AdapterShopping.HolderItem>() {
    private var context: Context? = null
    private var itemList: ArrayList<ModelShopping>? = null
    lateinit var dbHelper: DatabaseHelper

    constructor(context: Context, itemList: ArrayList<ModelShopping>?) : this() {
        this.context = context
        this.itemList = itemList
    }

    class HolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameItem: TextView = itemView.findViewById(R.id.tvName)
        var done: ImageView = itemView.findViewById(R.id.imgOk)
        var linear: LinearLayout = itemView.findViewById(R.id.linear)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderItem {
        return HolderItem(
            LayoutInflater.from(context).inflate(R.layout.item_layout_shoppig, parent, false)
        )

    }

    override fun onBindViewHolder(holder: HolderItem, position: Int) {
        dbHelper = DatabaseHelper(context)

        val modelItem = itemList!!.get(position)
        holder.nameItem.text = modelItem.name
        val id = modelItem.id
        val done = modelItem.done
        if (done == "yes") {
            holder.linear.setBackgroundColor(Color.parseColor("#FF49DF4F"))
            holder.done.setColorFilter(Color.parseColor("#ffffff"));

        } else if (done == "no") {
            holder.linear.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.done.setColorFilter(Color.parseColor("#000000"));

        }
        holder.itemView.setOnClickListener {
            showAddItemDialog(position, id, done)
        }

    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    private fun showAddItemDialog(position: Int, id: String, done: String) {


        val mDialogView = LayoutInflater.from(context).inflate(R.layout.shopping_item, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val mode = itemList?.get(position)
        if (mode?.done == "yes") {
            mDialogView.btnDone.text = "Un mark"
        }
        val mAlertDialog = mBuilder.show()
        mDialogView.btnDelete.setOnClickListener {
            val id = dbHelper.deleteShoppingItem(id)
            itemList?.removeAt(position)
            notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
        mDialogView.btnDone.setOnClickListener {
            if (done == "yes") {
                val h = dbHelper.updateShoppingItem(id, "no")

                mode?.done = "no"
                Toast.makeText(context, "done: no updated", Toast.LENGTH_SHORT).show()
                notifyDataSetChanged()

            } else if (done == "no") {
                val k = dbHelper.updateShoppingItem(id, "yes")
                mode?.done = "yes"
                Toast.makeText(context, "done: yes updated", Toast.LENGTH_SHORT).show()
                notifyDataSetChanged()

            }
            mAlertDialog.dismiss()

        }


    }

}