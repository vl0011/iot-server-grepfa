package com.grepfa.iot.grepfa.mqtt.grepfa.up

import com.grepfa.iot.grepfa.data.device.DeviceRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.MessageHandler

@Configuration
class Up() {
    @Bean
    @ServiceActivator(inputChannel = "upChannel")
    fun inboundMessageHandler(e: UpForwarder): MessageHandler? {
        return MessageHandler { message ->
            val topic = message.headers[MqttHeaders.RECEIVED_TOPIC] as String
            println("Topic:$topic")

            e.handle()
        }
    }
}