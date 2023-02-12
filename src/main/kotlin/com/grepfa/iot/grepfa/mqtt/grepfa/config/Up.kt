package com.grepfa.iot.grepfa.mqtt.grepfa.config

import com.grepfa.iot.grepfa.mqtt.grepfa.topicParser
import com.grepfa.iot.grepfa.mqtt.grepfa.up.UpForwarder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.MessageHandler

@Configuration
class Up() {
    @Bean
    @ServiceActivator(inputChannel = "upChannel")
    fun upInboundMessageHandler(e: UpForwarder): MessageHandler? {
        return MessageHandler {
            val topic =
            e.handle(topicParser(it.headers[MqttHeaders.RECEIVED_TOPIC] as String), it.payload.toString())
        }
    }
}