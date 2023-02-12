package com.grepfa.iot.grepfa.data.device

import com.grepfa.iot.grepfa.data.device.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*
@Repository
interface DeviceRepository: JpaRepository<Device, UUID> {
    @Query("select p.id from #{#entityName} p")
    fun getAllIds(): List<UUID>?
}