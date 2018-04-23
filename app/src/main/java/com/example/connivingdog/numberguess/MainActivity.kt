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
    }
    //when pressing enter on the keyboard
    override  fun onResume(){
        super.onResume()
        numberText.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> //when enter is pressed on the keyboard
            if(event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                attemptFetchResult()
            return@OnKeyListener true

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
        Log.d("usePRef",userPrefix + " x ")

        var sp: SimPrefix //SimPrefix Data Class
        var query: Query = mDatabaseReference
                .orderByChild("simPref")
                .equalTo(userPrefix)
                .limitToFirst(1)

                query.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach{
                    //resultView.text = it.child("simCard").value.toString()
                    sp = it.getValue(SimPrefix::class.java)!!

                    resultView.text = sp.simCard
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })
    }

    // function with return type
    private fun getUserPrefix(): String {
        var simNumber : String = ""
        for(a in 0..3)
            simNumber += numberText.text[a].toString()
        return simNumber
    }
}
/*
Function for adding data to firebase
Was used only when adding the number prefixes
private fun addSimPref(){
    var card = simCatSpin.selectedItem.toString()
    var pref = numberText.text.toString()

    var sp = SimPrefix(card,pref)

    mDatabaseReference.child("simnumbers").push().setValue(sp)
    Toast.makeText(this,"Added",Toast.LENGTH_LONG).show()
    Log.d("dup","added here")
}*/
