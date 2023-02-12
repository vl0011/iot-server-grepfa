package com.grepfa.iot.grepfa.mqtt.grepfa.up

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.coRouter

@EnableWebFlux
@Configuration
class UpHttpRouter {
    @Bean
    fun upEventRoute(handler: UpForwarder) = coRouter {
        "/event/up".nest {
            GET("/example", handler::getUpPayloadExample)
        }
    }
}