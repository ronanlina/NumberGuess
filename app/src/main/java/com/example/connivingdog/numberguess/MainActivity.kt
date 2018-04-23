package com.example.connivingdog.numberguess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.connivingdog.numberguess.R.id.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot



class MainActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //populating spinner
        val arr: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.sim_list,
                android.R.layout.simple_spinner_dropdown_item)
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        simCatSpin.adapter = arr

//        button2.setOnClickListener {
//            addSimPref()
//        }
    }
    override  fun onResume(){
        super.onResume()
        numberText.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> //when enter is pressed on the keyboard
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                attemptFetchResult()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun attemptFetchResult(){
        var focusView: View? = null
        var cancel: Boolean = false

        if(numberText.length() < 11){
            numberText?.error = "Phone number should atleast have 11 digits"
            cancel = true
            focusView = numberText
        }

        if(cancel){
            focusView?.requestFocus()
        }
        else{
            fetchResult()
        }
    }

    private fun fetchResult(){
        var userPrefix: String = getUserPrefix()
        var query: Query = mDatabaseReference.child("simnumbers").orderByChild("simPref").equalTo(userPrefix)
        var sp: SimPrefix

        var mListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (finalSnap in dataSnapshot.children) {
                    sp = dataSnapshot.getValue(SimPrefix::class.java)!!
                    resultView.text = sp.simCard.toString()
                    Log.d("check","here")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        query.addValueEventListener(mListener)

        Log.d("check","here2")
    }
    // function with return type
    private fun getUserPrefix(): String {
        var simNumber : String = null.toString()
        for(a in 0..3)
            simNumber += numberText.text[a].toString()
        return simNumber
    }
    //Function for adding data to firebase
    //Was used only when adding the number prefixes
//    private fun addSimPref(){
//        var card = simCatSpin.selectedItem.toString()
//        var pref = numberText.text.toString()
//
//        var sp = SimPrefix(card,pref)
//
//        mDatabaseReference.child("simnumbers").push().setValue(sp)
//        Toast.makeText(this,"Added",Toast.LENGTH_LONG).show()
//        Log.d("dup","added here")
//    }

}
