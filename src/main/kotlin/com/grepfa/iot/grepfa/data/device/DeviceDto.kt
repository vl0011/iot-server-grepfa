package com.grepfa.iot.grepfa.data.device

import com.grepfa.iot.grepfa.network.NetworkDto
import com.grepfa.iot.grepfa.profile.GetProfileDto
import com.grepfa.iot.grepfa.profile.Profile

import java.io.Serializable
import java.util.*

/**
 * A DTO for the {@link com.grepfa.iot.grepfa.data.device.Device} entity
 */
data class DeviceDto(
    val id: UUID? = null,
    val name: String,
    val network: List<NetworkDto>,
    val profile: Profile
) : Serializable {
    fun toEntity() : Device {
        return Device(
            name=name, network = network.map { it.toEntity() }, profile = profile
        )
    }

    constructor(d: Device) : this(d.id, d.name, d.network.map { NetworkDto(it) }, d.profile)

}

data class GetDeviceDto(
    val id: UUID?,
    val name: String,
    val network: List<NetworkDto>,
    val profile: GetProfileDto
) : Serializable {
    constructor(d: Device) : this(d.id, d.name, d.network.map { NetworkDto(it) }, GetProfileDto(d.profile))
}

data class CreateDeviceDto(
    val name: String,
    val network: List<NetworkDto>,
    val profile: UUID
) :Serializable

