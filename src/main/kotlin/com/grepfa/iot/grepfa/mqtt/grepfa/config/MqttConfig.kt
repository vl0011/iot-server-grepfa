package com.grepfa.iot.grepfa.mqtt.grepfa

import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.StandardIntegrationFlow
import org.springframework.integration.dsl.Transformers
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.stereotype.Component


@Configuration
class MqttConfig(private val sampleMessageHandler: SampleMessageHandler, private val objectMapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Bean
    fun mqttPahoClientFactory(): MqttPahoClientFactory { // (1)
        return DefaultMqttPahoClientFactory()
            .apply {
                connectionOptions = connectOptions()
            }
    }

    private fun connectOptions(): MqttConnectOptions {
        return MqttConnectOptions()
            .apply { // (2)
                serverURIs = arrayOf("ssl://cs.grepfa.com:8883")
                userName = "grepfa"
                password = "grepfa".toCharArray()
            }
    }

    @Bean
    fun mqttInboundFlow(): StandardIntegrationFlow {
        return integrationFlow(mqttChannelAdapter()) { // (3)
//            transform(Transformers.fromJson(SampleMessage::class.java)) // (4)
            handle {

                val topic = it.headers[MqttHeaders.RECEIVED_TOPIC] as String
//                sampleMessageHandler.handle(it.payload as SampleMessage) // (5)

                logger.info(topic)
                logger.info(it.payload as String)
            }
        }!!
    }

    private fun mqttChannelAdapter(): MqttPahoMessageDrivenChannelAdapter { // (6)
        return MqttPahoMessageDrivenChannelAdapter(
            MqttClient.generateClientId(),
            mqttPahoClientFactory(),
            "/#")
            .apply {
                setConverter(DefaultPahoMessageConverter())
                setQos(2)
            }
    }

//    @Bean
//    fun mqttOutboundFlow() = integrationFlow(MQTT_OUTBOUND_CHANNEL) { // (7)
//        transform<Any> { // (8)
//            when (it) {
//                is SampleMessage -> objectMapper.writeValueAsString(it)
//                else -> it
//            }
//        }
//        handle(mqttOutboundMessageHandler()) // (9)
//    }
//
//    private fun mqttOutboundMessageHandler(): MessageHandler { // (10)
//        return MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory())
//            .apply {
//                setAsync(true)
//                setDefaultTopic(mqttProperties.topic)
//                setDefaultQos(mqttProperties.qos)
//            }
//    }
//
//    @MessagingGateway(defaultRequestChannel = MQTT_OUTBOUND_CHANNEL)
//    interface MqttOutboundGateway { // (11)
//
//        @Gateway
//        fun publish(@Header(MqttHeaders.TOPIC) topic: String, data: String) // (12)
//
//        @Gateway
//        fun publish(data: SampleMessage) // (13)
//    }

    companion object {
        const val MQTT_OUTBOUND_CHANNEL = "outboundChannel"
    }

}

@Component
class SampleMessageHandler { // (1)

    private val log = LoggerFactory.getLogger(javaClass)

    fun handle(message: SampleMessage) {
        log.info("message arrived : $message")
    }
}
data class SampleMessage(val title: String, val content: String)