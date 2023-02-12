package com.grepfa.iot.type

import org.springframework.http.HttpStatus

data class GResponse (
    val id: Int,
    val msg: String,
    val obj: Any
)