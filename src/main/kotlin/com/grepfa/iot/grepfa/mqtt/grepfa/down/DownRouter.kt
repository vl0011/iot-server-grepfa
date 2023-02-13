package com.grepfa.iot.grepfa.mqtt.grepfa.down

import com.grepfa.iot.grepfa.mqtt.grepfa.config.Down
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.coRouter

@EnableWebFlux
@Configuration
class DownRouter {
    @Bean
    fun downCommandRoute(handler: Down) = coRouter {
        "/command/down".nest {
            POST("/{id}", handler::downHandler)
        }
    }
}