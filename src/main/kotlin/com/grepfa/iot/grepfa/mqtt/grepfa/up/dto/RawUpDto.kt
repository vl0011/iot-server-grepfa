package com.grepfa.iot.grepfa.mqtt.grepfa.up.dto

import com.fasterxml.jackson.databind.BeanDescription
import java.io.Serializable
import java.util.UUID

// 기기가 보내는 메세지
data class RawUpDto(

    // Device 에서 사용하는기기 식별자
    val deviceId: UUID,



    // Network 에서 사용하는 기기 주소
    val address: String,

    // 센서값 리스트
    val values: List<RawUpElement>
) : Serializable


data class RawUpElement(
    // Part 에서 사용하는 센서 이름
    val name: String,

    // 센서 값
    val value: String
) : Serializable


data class EventUpDto(
    val eventId: UUID,
    val deviceId: UUID,
    val profileId: UUID,
    val address: String,
    val networkType: String,
    val data: List<EventUpElementDto>
) : Serializable

data class EventUpElementDto(
    val name: String,
    val summary: String,
    val description: String,
    val partId: UUID,
    val unit: String,
    val max: Double,
    val min: Double,
    val value: String,
    val varType: String,
    val type: String
): Serializable