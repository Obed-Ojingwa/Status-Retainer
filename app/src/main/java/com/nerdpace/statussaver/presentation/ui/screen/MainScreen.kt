package com.nerdpace.statussaver.presentation.ui.screen

import android.widget.Toast

class MainScreen {
    val sayer = Sayer()
    val say = sayer.sayHello()



}


class Sayer(){
    open fun sayHello(){
        println("Hello")
    }
}