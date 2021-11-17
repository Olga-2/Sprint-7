package org.spring.mvc.springsecurity.config

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource


@Order(1)
@Configuration
@EnableWebSecurity(debug = true)
class SpringSecurityConfig (@Autowired val dataSource: DataSource) : WebSecurityConfigurerAdapter() {

    // роль admin всегда есть доступ к /admin/**
    // роль user всегда есть доступ к /user/**
    // Наш кастомный "403 access denied" обработчик
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/login").permitAll()
            .antMatchers("/api/**").hasAnyRole("API")
            .antMatchers("/app/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
    }

    // создаем пользоватлелей, admin и user
    override fun configure(@Autowired  builder: AuthenticationManagerBuilder) {
        builder.jdbcAuthentication()
            .dataSource(dataSource).withDefaultSchema()
            .withUser(
                User.withUsername("user")
                    .password(passwordEncoder()!!.encode("pass"))
                    .roles("APP"))
            .withUser( User.withUsername("bob")
                .password(passwordEncoder()!!.encode("Bob123"))
                .roles("API"))
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}
