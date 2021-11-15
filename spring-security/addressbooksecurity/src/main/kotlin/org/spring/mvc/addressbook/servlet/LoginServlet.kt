package org.spring.mvc.addressbook.servlet

import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class LoginServlet: HttpServlet() {

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        request.getRequestDispatcher("authView").forward(request, response)

//        response.contentType = "text/html"
//        response.status = 200
//        val text ="<!DOCTYPE html>\n" +
//                "<html lang=\"en\"\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <title>Auth</title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "    <h1>My auth:</h1>\n" +
//                "    <form action=\"/login\" method=\"POST\" >\n" +
//                "       <h2> Login:\n" +
//                "        <input type=\"text\" name =\"login\" />\n" +
//                "        Password:\n" +
//                "        <input type=\"password\" name =\"password\" />\n" +
//                "       </h2>\n" +
//                "        <br/>\n" +
//                "        <input type=\"submit\" value=\"Go!\" />\n" +
//                "    </form>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>"
//        val printWriter = response.writer
//        printWriter.println(text)
//        printWriter.close()
    }


}

@Configuration
class ServletConfig {
    @Bean
    fun servletRegistrationBean(): ServletRegistrationBean<*> {
        return ServletRegistrationBean(LoginServlet(), "/login").also{it.setLoadOnStartup(2)}
        }

}