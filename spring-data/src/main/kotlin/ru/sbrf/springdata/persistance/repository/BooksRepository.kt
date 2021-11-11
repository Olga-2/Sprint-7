package ru.sbrf.springdata.persistance.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sbrf.springdata.persistance.entity.Book
import ru.sbrf.springdata.persistance.entity.Styles

@Repository
interface BooksRepository: CrudRepository<Book,Long>{
    fun findByStyle(style: Styles): List<Book>

    @Query("Select b from Book b where b.oftenAsked >= :exp ")
    fun findPopular(@Param("exp") much : Int): List<Book>
}