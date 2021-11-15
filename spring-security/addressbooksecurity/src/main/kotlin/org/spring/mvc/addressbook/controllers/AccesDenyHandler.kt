package org.spring.mvc.addressbook.controllers

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AccessDenyHandler : AccessDeniedHandler {

    @Throws(IOException::class, ServletException::class)
    override fun handle(
        httpServletRequest: HttpServletRequest?,
        httpServletResponse: HttpServletResponse?,
        e: org.springframework.security.access.AccessDeniedException?
    ) {
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            logger.info(
                "User '" + auth.getName()
                    .toString() + "' attempted to access the protected URL: " + httpServletRequest!!.requestURI
            )
        }
        httpServletResponse!!.sendRedirect(httpServletRequest!!.contextPath + "/403")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AccessDenyHandler::class.java)
    }


}