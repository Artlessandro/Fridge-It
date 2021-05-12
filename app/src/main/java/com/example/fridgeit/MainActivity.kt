package com.example.fridgeit

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() ,DatePickerDialog.OnDateSetListener{
    private val fridgeFragment = com.example.fridgeit.fragments.FridgeFragment()
    private val freezerFragment = com.example.fridgeit.fragments.FreezerFragment()
    private val shoppingListFragment = com.example.fridgeit.fragments.ShoppingListFragment()
    private val pantryFragment = com.example.fridgeit.fragments.PantryFragment()
    var day = 0
    var month = 0
    var year = 0
    var date :String = ""
    var name:String = ""
    var choose:Int = 1
    public  var createdDate = ""

    lateinit var dbHelper:DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(fridgeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.tbFridge -> replaceFragment(fridgeFragment)
                R.id.tbReezer -> replaceFragment(freezerFragment)
                R.id.tbPantry -> replaceFragment(pantryFragment)
                R.id.tbShopping -> replaceFragment(shoppingListFragment)
                R.id.tbAdd -> showAddItemDialog()

            }
            true
        }
        dbHelper = DatabaseHelper(this)

        val c = Calendar.getInstance().time
      //  println("Current time => $c")

        val df = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        createdDate = df.format(c)
        Log.i("date", createdDate)
    }


    private fun replaceFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

    // showing the add item pop-up dialog
    private fun showAddItemDialog()
    {

        date = ""
        name = ""
        choose = 1
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_item, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mDialogView.btnDate.setOnClickListener {
            getDateCalender()
            DatePickerDialog(this, this, year, month, day).show()
            }
        mDialogView.btnCancel.setOnClickListener {
            date = ""
            name = ""
            choose = 1
            mAlertDialog.dismiss()
        }
        mDialogView.btnOK.setOnClickListener {
            name = mDialogView.etName.text.toString().trim()
            if (date.isNotEmpty() && name.isNotEmpty())
            {
                mAlertDialog.dismiss()
                Log.i("data", "$name $date $choose")

                val id = dbHelper.insertInfo(name, choose.toString(), date, createdDate)
                Toast.makeText(
                    applicationContext,
                    "Id: " + id + "Name: " + name + "\n" + "Type: " + choose + "\n" + "Expire :" + date + "\n" + "Create: " + createdDate,
                    Toast.LENGTH_LONG
                ).show()
                val i = Intent(this@MainActivity, MainActivity::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(i)
                overridePendingTransition(0, 0)
            }
            else{
                Toast.makeText(applicationContext, "Enter name and choose date", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rFridge ->
                    if (checked) {
                        choose = 1
                    }
                R.id.rFreezer ->
                    if (checked) {
                        choose = 2
                    }
                R.id.rPantry ->
                    if (checked) {
                        choose = 3
                    }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year1: Int, month1: Int, dayOfMonth: Int) {
       val monthD =month+1
        date = "$dayOfMonth-$monthD-$year1"

    }
    // get the Calendar
    private fun getDateCalender()
    {
        val cal :Calendar= Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }
}
