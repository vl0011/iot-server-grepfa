package com.grepfa.iot.grepfa.mqtt.grepfa.down

import java.io.Serializable

data class DownRequestDto (
    val partId: String,
    val value: String
) : Serializable

data class DownResponseDto(
    val request: DownRequestDto,
)