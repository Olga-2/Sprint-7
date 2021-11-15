package org.spring.mvc.addressbook.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.access.AccessDeniedHandler

@Order(1)
@Configuration
open class SpringSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private val accessDeniedHandler: AccessDeniedHandler? = null

    // роль admin всегда есть доступ к /admin/**
    // роль user всегда есть доступ к /user/**
    // Наш кастомный "403 access denied" обработчик
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/login").permitAll()
            .antMatchers("/api/**").hasAnyRole("ADMIN")
            .antMatchers("/app/**").hasAnyRole("USER")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
    }

    // создаем пользоватлелей, admin и user

    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("user").password("password").roles("USER")
            .and()
            .withUser("admin").password("password").roles("ADMIN")
    }
}