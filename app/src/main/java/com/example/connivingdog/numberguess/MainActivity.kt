package com.example.connivingdog.numberguess

import android.content.Intent
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.connivingdog.numberguess.R.array.sim_list
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
        //grid button onClick listeners (not simplified)
        b1.setOnClickListener{numberText.text.append(b1.text) }
        b2.setOnClickListener{numberText.text.append(b2.text) }
        b3.setOnClickListener{numberText.text.append(b3.text) }
        b4.setOnClickListener{numberText.text.append(b4.text) }
        b5.setOnClickListener{numberText.text.append(b5.text) }
        b6.setOnClickListener{numberText.text.append(b6.text) }
        b7.setOnClickListener{numberText.text.append(b7.text) }
        b8.setOnClickListener{numberText.text.append(b8.text) }
        b9.setOnClickListener{numberText.text.append(b9.text) }
        b0.setOnClickListener{numberText.text.append(b0.text) }
        clear.setOnClickListener{
            numberText.text.clear()
            numberText.text.append("09")
            simLogoImage.setImageResource(R.drawable.simdefault)
        }

        numberText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                Toast.makeText(this@MainActivity,
//                            "value :" + numberText.text,
//                            Toast.LENGTH_SHORT).show() //for testing
                            fetchResult(numberText.text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        fab.setOnClickListener {
            var intent= Intent(this,AddSimPref::class.java)
            startActivity(intent)
        }

    }

    //fetch result from database
    private fun fetchResult(userPrefix: String){
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
                    //resultView.text = sp.simCard
                    displaySimLogo(sp.simCard.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })
    }

    // displaying the images
    private fun displaySimLogo(simPrefix: String){
            val res: Resources = resources
            var sim = res.getStringArray(R.array.sim_list)

            if(simPrefix.equals(sim[0])){
                //globe tm
                simLogoImage.setImageResource(R.drawable.globetm)
            }
            else if(simPrefix.equals(sim[1])){
                //smart tnt
                simLogoImage.setImageResource(R.drawable.smarttnt)
            }
            else if(simPrefix.equals(sim[2])){
                //sun
                simLogoImage.setImageResource(R.drawable.sun)
            }
            else if(simPrefix.equals(sim[3])){
                //abscbn
                simLogoImage.setImageResource(R.drawable.abscbnmobile)
            }
    }
}