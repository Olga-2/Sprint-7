package ru.sber.orm.entity

import javax.persistence.*

@Entity
class Author(
    @Id
    @Column(name="auth")
    var id: Long,

    @Column(name ="full_name", length=127)
    var name: String,

    var country: String,

    @Enumerated(value = EnumType.STRING)
    var gender: Gender

)
{
    override fun toString(): String {
        return "Author(id=$id, name='$name', country='$country', gender='$gender')"
    }
}

enum class Gender{
    MAN,
    WOMAN
}