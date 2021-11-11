package ru.sbrf.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sbrf.springdata.persistance.entity.Book
import ru.sbrf.springdata.persistance.entity.Styles
import ru.sbrf.springdata.persistance.repository.BooksRepository

@SpringBootApplication
class SpringDataApplication(
    private val booksRepository:  BooksRepository
): CommandLineRunner{
    override fun run(vararg args: String?) {
        val bookFantasy1 = Book(author = "John Ronald Reuel Tolkien",
            titleBook = "Lord og Ring", oftenAsked = 100, style = Styles.FANTASY)

        val bookFantasy2 = Book(author = "John Ronald Reuel Tolkien",
            titleBook = "The Hobbit, or There and Back Again", oftenAsked = 97, style = Styles.FANTASY)

        val bookNovel1 = Book(author = "Jane Austen",
            titleBook = "Pride and Prejudice", oftenAsked = 45, style = Styles.NOVEL)

        val bookNovel2 = Book(author = "Charlotte Bronte",
            titleBook = "Jane Eyre", oftenAsked = 87, style = Styles.NOVEL)

        booksRepository.saveAll(listOf(bookFantasy1, bookFantasy2, bookNovel1, bookNovel2))

        var result = booksRepository.findByStyle(Styles.NOVEL)
        println(result)
        result = booksRepository.findPopular(50)
        println(result)
        result = booksRepository.findAll() as List<Book>
        println(result)
        booksRepository.delete(bookFantasy1)
        result = booksRepository.findAll() as List<Book>
        println(result)

    }
}

fun main(args: Array<String>) {

    runApplication<SpringDataApplication>(*args)
}
