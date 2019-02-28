package com.oz.etta

import android.content.Context
import android.widget.Button

class UserButton(context: Context?, user: User) : Button(context) {

    val user: User = user

    fun updateText() {
        this.text = user.toString()
    }
}

