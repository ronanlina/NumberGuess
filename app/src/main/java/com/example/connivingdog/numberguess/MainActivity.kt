package com.example.connivingdog.numberguess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot

class MainActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("simnumbers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //populating spinner
        val arr: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.sim_list,
                android.R.layout.simple_spinner_dropdown_item)
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        simCatSpin.adapter = arr

        button2.setOnClickListener {
            attemptFetchResult()
        }
        //when pressing enter on the keyboard
        numberText.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> //when enter is pressed on the keyboard
            if(event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                attemptFetchResult()
            return@OnKeyListener true
        })
    }

    private fun attemptFetchResult(){
        var focusView: View? = null
        var cancel: Boolean = false

        if(!checkNumberValidity()){
            numberText?.error = getString( R.string.format_warning)
            cancel = true
            focusView = numberText
        }

        if(cancel){ focusView?.requestFocus() }
        else{fetchResult()}
    }
    private fun checkNumberValidity(): Boolean{
        if(numberText.length() < 11){
            return false
        }

        var userPrefix: String = getUserPrefix()

        if(userPrefix.toInt() in 1000..899){
            return false
        }
        return true
    }
    // function with return type
    private fun getUserPrefix(): String {
        var simNumber : String = ""
        for(a in 0..3)
            simNumber += numberText.text[a].toString()
        return simNumber
    }

    private fun fetchResult(){
        var userPrefix: String = getUserPrefix()
        Log.d("usePRef",userPrefix + " x ")

        var sp: SimPrefix
        var query: Query = mDatabaseReference
                .orderByChild("simPref")
                .equalTo(userPrefix)
                .limitToFirst(1)

                query.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach{
                    sp = it.getValue(SimPrefix::class.java)!!

                    resultView.text = sp.simCard
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })
    }
}