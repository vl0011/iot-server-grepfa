package com.grepfa.iot.grepfa.data.device

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*
@Repository
interface DeviceRepository: JpaRepository<Device, UUID> {
    @Query("select p.id from #{#entityName} p")
    fun getAllIds(): List<UUID>?
}