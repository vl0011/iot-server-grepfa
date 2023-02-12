package com.grepfa.iot.grepfa.data.profile

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.coRouter

@Configuration
@EnableWebFlux
class ProfileRouter(val handler: ProfileHandler) {


    @Bean
    fun profileRoute() = coRouter {
        "/profile".nest {
            POST("/new", handler::createNewProfile)
            GET("/ids", handler::getAllProfileId)
            GET("/example", handler::getExampleNewProfile)
            GET("/id/{id}", handler::findFromId)
        }
    }
}