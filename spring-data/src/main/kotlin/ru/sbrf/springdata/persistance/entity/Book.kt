package ru.sbrf.springdata.persistance.entity

import javax.persistence.*

@Entity
@Table(name = "spring_data_book")
class Book (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "full_name_author")
    var author: String,
    @Column(name = "title_book")
    var titleBook: String,
    @Column(name = "often_asked")
    var oftenAsked: Int,
    @Enumerated(EnumType.STRING)
    var style: Styles
){
    override fun toString(): String {
        return "Bookreader(id=$id, author='$author', titleBook='$titleBook', oftenAsked=$oftenAsked, style='$style')"
    }
}

enum class Styles{
    HISTORIC,
    FANTASY,
    NOVEL,
    ANIMALS,
    SCIENTIFIC
}
