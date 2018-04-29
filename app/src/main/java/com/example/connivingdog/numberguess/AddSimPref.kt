package com.example.connivingdog.numberguess

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_add_sim_pref.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.connivingdog.numberguess.R.id.*
import com.google.firebase.database.*


class AddSimPref : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sim_pref)

        //populating spinner
        val arr: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.sim_list,
                android.R.layout.simple_spinner_dropdown_item)
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cardSpin.adapter = arr

        //Keyboard automatically opens as activty starts
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        prefText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN){
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //attemptAddPrefix()
                    addPrefix()
                    return@OnKeyListener true
                }
            }
            false
        })

    }

    /*private fun attemptAddPrefix(){
        var simPref: String = prefText.text.toString()
        var sp: SimPrefix

        mDatabaseReference.child("simnumbers")
                .orderByChild("simPref")
                .equalTo(simPref)
                .limitToFirst(1)
                .addValueEventListener( object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if(dataSnapshot!=null){
                            dataSnapshot.children.forEach {
                                sp = it.getValue(SimPrefix::class.java)!!
                                if(sp.simPref.toString().equals(simPref)){
                                    Toast.makeText(this@AddSimPref, "already exists", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    addPrefix()
                                }
                            }
                        }
                        else{
                            Toast.makeText(this@AddSimPref, "else", Toast.LENGTH_SHORT).show()
                        }

                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                })
    }*/
    //push to database
    private fun addPrefix(){
        var simCard = cardSpin.selectedItem.toString()
        var simPref: String = prePrefView.text.toString() + prefText.text.toString()
        var sp = SimPrefix(simCard,simPref)
        mDatabaseReference.child("simnumbers").push().setValue(sp)
        Toast.makeText(this,"sim card prefix added!", Toast.LENGTH_SHORT).show()
//        var coordinator: CoordinatorLayout? = R.id.coordinator as CoordinatorLayout?
//        val snackbar = Snackbar
//                .make(coordinator!!, "Snackbar with Callback", Snackbar.LENGTH_LONG)
//                .setAction("OK") {
//                    val snackbar1 = Snackbar.make(coordinator, "Snackbar with Callback called.", Snackbar.LENGTH_SHORT)
//                    snackbar1.show()
//                }
    }
}



//mDatabaseReference.child("simnumbers")
//                .addValueEventListener( object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                        var focusView: View? = null
//
//                        var exists: Boolean = false
//                        dataSnapshot.children.forEach {
//                            sp = it.getValue(SimPrefix::class.java)!!
//                            if(sp.simPref.equals(simPref)){
//                                exists = true
//                                prefText.error = "prefix already exists!"
//                                focusView = prefText
//                            }
//                        }
//
//                        if(exists){
//                            focusView?.requestFocus()
//                        }
//                        else{
//                            addPrefix()
//                        }
//                    }
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        println("loadPost:onCancelled ${databaseError.toException()}")
//                    }
//                })