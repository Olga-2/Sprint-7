package ru.sber.orm

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.time.Year


fun main() {

    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(BookDetails::class.java)
        .buildSessionFactory()
    sessionFactory.use { sessionFactory ->

        val dao = LibraryDAO(sessionFactory)

        val author1 = Author(id=1L, name = "John Ronald Reuel Tolkien", country = "England", gender = Gender.MAN)
        val author2 = Author(id=2L, name = "Jane Austen", country = "England", gender = Gender.WOMAN)



        val book1 = Book(
            title = "Lord of Ring",

            style = Styles.FANTASY,
            author = author1,
            detail = BookDetails(year = Year.parse("1954"),
                bookData = BookData(heroes = "Bilbo, Frodo, Gendalf, Gorlum and others", summaryOfBook = "Something about The Elves")))

        val book2 = Book(
            title = "The Hobbit, or There and Back Again",

            style = Styles.FANTASY,
      //      author = Author(name = "John Ronald Reuel Tolkien", country = "England", gender = Gender.MAN),
            author = author1,
            detail = BookDetails(year = Year.parse("1937"),
                bookData = BookData(heroes = "Bilbo, dwarves, Gendalf, Dragon and others", summaryOfBook = "Something about The Spiders")))

        val book3 = Book(
            title = "Pride and Prejudice",
            style = Styles.NOVEL,
        //    author = Author(name = "Jane Austen", country = "England", gender = Gender.WOMAN),
            author = author2,
            detail = BookDetails(year = Year.parse("1813"),
                bookData = BookData(heroes = "The Bennets, Bingly, Darsy and others", summaryOfBook = "The exquisite love affair")))

        dao.save(author1)
        dao.save(author2)
        dao.save(book1)
        dao.save(book2)
        dao.save(book3)

        val found1 = dao.find(book1.id)
        println("Найдена книга $found1")

        val found2 = dao.find(book2.title)
        println("Найдена книга $found2")

        val found3 = dao.findAll()
        println("Список книг: $found3")

        val found4 = dao.findStyle(book2.style.name)
        println("Найдена книга  $found4")
    }
}
class LibraryDAO (
    private val sessionFactory: SessionFactory
    )
{
    fun save(author: Author){
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(author)
            session.transaction.commit()
        }
    }
    fun save(book: Book){
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(book)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Book? {
        var result: Book?
        sessionFactory.openSession().use{
            it.beginTransaction()
            result = it.get(Book::class.java, id)
            it.transaction.commit()
        }
        return result
    }

    fun find(title: String): Book? {
        val result: Book?
        sessionFactory.openSession().use{
            it.beginTransaction()
            result = it.byNaturalId(Book::class.java).using("title", title).loadOptional().orElse(null)
            it.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Book> {
        val result : List<Book>
        sessionFactory.openSession().use{
            it.beginTransaction()
            result = it.createQuery("from Book").list() as List<Book>
            it.transaction.commit()
        }
        return result
    }

    fun findStyle(style: String): List<Book> {
        val result : List<Book>
        sessionFactory.openSession().use{
            it.beginTransaction()
            result = it.createQuery("from Book where style = '$style'").list() as List<Book>
            it.transaction.commit()
        }
        return result
    }
}