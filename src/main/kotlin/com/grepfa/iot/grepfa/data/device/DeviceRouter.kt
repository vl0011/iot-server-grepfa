package com.grepfa.iot.grepfa.device

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.coRouter

@EnableWebFlux
@Configuration
class DeviceRouter() {
    @Bean
    fun deviceRoute(handler: DeviceHandler) = coRouter {
        "/device".nest {
            POST("/new", handler::newDevice)
            GET("/ids", handler::getAllDeviceId)
            GET("/example", handler::getExampleNewDevice)
            GET("/id/{id}", handler::findFromUUID)
        }
    }
}