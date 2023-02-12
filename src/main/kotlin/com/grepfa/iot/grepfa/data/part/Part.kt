package com.grepfa.iot.grepfa.part

import com.grepfa.iot.grepfa.profile.Profile
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.LocalDateTime
import java.util.UUID




// 프로파일에 조합되는 파츠의 정보를 정의 합니다.
@Entity
@Table
data class Part (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    // 파츠의 이름
    val name: String,

    // 파츠의 타입 [sensor or actuator]
    val type: String,
    val varType: String,



    val description: String,
    val summary: String,

    val unit: String,
    val min: Double,
    val max: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Part

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}

