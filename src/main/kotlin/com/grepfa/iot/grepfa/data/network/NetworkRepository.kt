package com.grepfa.iot.grepfa.data.network

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface NetworkRepository : JpaRepository<Network, UUID> {
}