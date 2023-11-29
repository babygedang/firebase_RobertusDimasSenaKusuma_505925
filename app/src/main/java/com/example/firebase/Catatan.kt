package com.example.firebase

import java.io.Serializable

data class Catatan(
    var id: String = "",
    var title: String = "",
    var content: String = ""
) : Serializable