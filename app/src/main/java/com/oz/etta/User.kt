package com.oz.etta

data class User(
    var name: String,
    var score: Integer
) {
    override fun toString(): String = "$name:$score"
}

