package com.example.fridgeit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgeit.DatabaseHelper
import com.example.fridgeit.R
import kotlinx.android.synthetic.main.add_shopping_item.view.*


class ShoppingListFragment : Fragment() {
    lateinit var dbHelper: DatabaseHelper
    var adapterItem:AdapterShopping? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbHelper = DatabaseHelper(context)
        // Inflate the layout for this fragment

       val view =inflater.inflate(R.layout.fragment_shopping_list, container, false)
        val addButton: Button = view.findViewById(R.id.btnAddNewItem)
        addButton.setOnClickListener {
           showAddItemDialog(view)
        }
        notifiy(view)



    return view
    }
    private fun notifiy(view: View)
    {  val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val list = dbHelper.getAllShoppingItems()
        adapterItem = AdapterShopping(context!!, list)


        recyclerView.setHasFixedSize(true)
        adapterItem!!.notifyDataSetChanged()
        recyclerView.adapter = adapterItem

    }

    // showing add item pop-up dialog
    private fun showAddItemDialog(view: View)
    {


        val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_shopping_item, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btnAddShoppingItem.setOnClickListener {
            val name = mDialogView.etShoppingItemName.text.toString()
            if (name.isNotEmpty())
            {
                val id = dbHelper.insertShoppigItem(name, "no")
                if (id.toString().isNotEmpty())
                {
                    Toast.makeText(context, "item Added", Toast.LENGTH_SHORT).show()
                }

                mAlertDialog.dismiss()
               notifiy(view)




            }
            else{
                Toast.makeText(context, "please enter the item name", Toast.LENGTH_SHORT).show()
            }

        }
        mDialogView.btnCancelDialog.setOnClickListener {
            mAlertDialog.dismiss()
        }




    }


}