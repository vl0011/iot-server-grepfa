package com.grepfa.iot.grepfa.mqtt.grepfa.config

import java.io.Serializable

data class RawDownDto(
    val name: String,
    val value: String
) : Serializable
