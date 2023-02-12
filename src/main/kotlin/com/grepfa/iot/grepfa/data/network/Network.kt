package com.grepfa.iot.grepfa.data.network

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "network")
data class Network(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    // chirpstack, grepfa_mqtt ...
    val type: String,

    // lora - eui64
    // grepfa_mqtt - ...
    val address: String,
)