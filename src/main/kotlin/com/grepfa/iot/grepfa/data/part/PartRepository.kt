package com.grepfa.iot.grepfa.part

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID


interface PartRepository:JpaRepository<Part, UUID> {
}