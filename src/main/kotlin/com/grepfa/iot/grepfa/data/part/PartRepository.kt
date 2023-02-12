package com.grepfa.iot.grepfa.data.part

import com.grepfa.iot.grepfa.part.Part
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface PartRepository:JpaRepository<Part, UUID> {
}