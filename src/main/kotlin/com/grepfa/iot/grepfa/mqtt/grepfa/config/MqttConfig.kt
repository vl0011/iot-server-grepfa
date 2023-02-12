package com.grepfa.iot.grepfa.mqtt.grepfa.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.Router
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.integration.core.MessageProducer
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
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
    fun upChannel():MessageChannel = PublishSubscribeChannel()
    @Bean
    fun mqttInputChannel():MessageChannel = PublishSubscribeChannel()



    @Router(inputChannel = "mqttInputChannel")
    fun route(message: Message<MqttMessage>): MessageChannel {
        val topic = message.headers[MqttHeaders.RECEIVED_TOPIC] as String
        val method = topic.split("/")[6]
        logger.info("method: $method")
        logger.info(topic)
        return upChannel()
    }

    @Bean
    fun mqttChannelAdapter(): MessageProducer { // (6)
        return MqttPahoMessageDrivenChannelAdapter(
            MqttClient.generateClientId(),
            mqttPahoClientFactory(),
            "/grepfa/+/device/+/event/+")
            .apply {
                setConverter(DefaultPahoMessageConverter())
                setQos(2)
                outputChannel = mqttInputChannel()
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


class UpConsumer{

}