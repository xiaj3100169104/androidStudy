package com.style.entity

data class UserTest(val name: String, val age: Int):Cloneable{

    override fun clone(): Any {
        return super.clone()
    }
}