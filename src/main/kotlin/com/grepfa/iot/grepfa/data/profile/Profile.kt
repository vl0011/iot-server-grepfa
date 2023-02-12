package com.grepfa.iot.grepfa.data.profile

import com.grepfa.iot.grepfa.data.part.Part
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*
import kotlin.collections.ArrayList

@Entity
@Table
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val name: String,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val contains: List<Part>// = ArrayList<Part>()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Profile

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}