package com.grepfa.iot.grepfa.mqtt.grepfa

fun topicParser(topic: String) : TopicInfo {
    val el = topic.split("/")
    if (el.size != 7) throw InvalidTopicMessage()
    return TopicInfo(
        full = topic,
        appId = el[2],
        devId = el[4],
        method = el[6]
    )
}

class InvalidTopicMessage: Exception(){}

data class TopicInfo(
    val full: String,
    val appId: String,
    val devId: String,
    val method: String
)