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
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*


class AddSimPref : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var prefListAdapter: PrefixListAdapter
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
                    //addPrefix()
                    attemptAdd()
                    return@OnKeyListener true
                }
            }
            false
        })

    }

    override fun onStart() {
        super.onStart()
        //populate listview
        prefListAdapter = PrefixListAdapter(mDatabaseReference,this@AddSimPref)
        existingPrefListView.adapter = prefListAdapter
    }

    override fun onStop() {
        super.onStop()
        prefListAdapter.cleanup()
    }

    private fun attemptAdd(){
        var focus: View? = null
        var cancel: Boolean = false
        var prefix: String  = prePrefView.text.toString() + prefText.text.toString()
        var sp: SimPrefix

        if(prefText.length()<2){
            prefText.error = "invalid length"
            cancel = true
            focus = prefText
        }

        //checks if prefix already exists
        mDatabaseReference.child("simnumbers").orderByChild("simPref").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    sp = it.getValue(SimPrefix::class.java)!!
                    if(sp.simPref.equals(prefix)){
                        prefText.error = "already exists"
                        cancel = true
                        focus = prefText
                    }
                }
                if(cancel){
                    focus?.requestFocus()
                }
                else{
                    addPrefix()
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
    //push to database
    private fun addPrefix(){
        var simPref: String = prePrefView.text.toString() + prefText.text.toString()
        var simCard = cardSpin.selectedItem.toString()
        var sp = SimPrefix(simCard,simPref)
        mDatabaseReference.child("simnumbers").push().setValue(sp)
        Toast.makeText(this,"sim card prefix added!", Toast.LENGTH_SHORT).show()
    }
}