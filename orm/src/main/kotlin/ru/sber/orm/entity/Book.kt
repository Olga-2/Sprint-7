package ru.sber.orm.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class Book(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @NaturalId
    var title: String,
    @Enumerated(EnumType.STRING)
    var style: Styles,
    @ManyToOne(targetEntity = Author::class)
    @JoinColumn(name = "auth")
    var author: Author,
    @OneToOne(cascade = [CascadeType.ALL])
    var detail: BookDetails,
    @CreationTimestamp
    var creationTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updateTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Book(id=$id, title='$title', author='$author', detail='$detail')"
    }
}
enum class Styles{
    HISTORIC,
    FANTASY,
    NOVEL,
    ANIMALS,
    SCIENTIFIC
}
