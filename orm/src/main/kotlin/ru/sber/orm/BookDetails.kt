package ru.sber.orm

import java.time.Year
import javax.persistence.*

@Entity
class BookDetails (
    @Id
    @GeneratedValue
    var id: Long = 0,
    var bookData: BookData,
    @Column(name = "year_edition")
    var year: Year
){
    override fun toString(): String{
        return "BookDetails(id=$id, bookData='$bookData', year='$year')"
    }
}


@Embeddable
class BookData(
    var heroes: String? = null,
    @Column(name = "SUMMARY_OF_BOOK")
    var summaryOfBook: String? = null,
)
{
    override fun toString(): String {
        return "Author(heroes=$heroes, summaryOfBook='$summaryOfBook')"
    }
}
