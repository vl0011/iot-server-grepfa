package com.grepfa.iot.grepfa.data.profile;

//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ProfileRepository : JpaRepository<Profile, UUID> {
    @Query("select p.id from #{#entityName} p")
    fun getAllIds(): List<UUID>?
}