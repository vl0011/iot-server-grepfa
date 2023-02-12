package com.grepfa.iot.grepfa.network

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table
data class Network(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    // chirpstack, grepfa_mqtt ...
    val type: String,

    // lora - eui64
    // grepfa_mqtt - ...
    val address: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Network

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}