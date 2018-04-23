package com.example.connivingdog.numberguess

class SimPrefix {
    var simCard: String? = null // properties,
    var simPref: String? = null // also acts as the getter and setter at the same time

    constructor(simCard: String, simPref: String) { //constructor with parameters
        this.simCard = simCard
        this.simPref = simPref
    }

    constructor() {} // empty constructor
}
