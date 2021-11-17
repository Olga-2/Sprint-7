package org.spring.mvc.config

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
import org.springframework.security.web.access.AccessDeniedHandler
import javax.sql.DataSource


@Order(1)
@Configuration
@EnableWebSecurity(debug = true)
class SpringSecurityConfig (
    @Autowired val accessDeniedHandler: AccessDeniedHandler?,
    @Autowired val dataSource: DataSource) : WebSecurityConfigurerAdapter() {

    // роль admin всегда есть доступ к /admin/**
    // роль user всегда есть доступ к /user/**
    // Наш кастомный "403 access denied" обработчик
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/login").permitAll()
            .antMatchers("/api/**").hasAnyRole("ADMIN")
            .antMatchers("/app/**").hasAnyRole("USER")
//            .antMatchers("/app/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
    }

    // создаем пользоватлелей, admin и user
    override fun configure(builder: AuthenticationManagerBuilder) {
//        auth.inMemoryAuthentication()
//            .withUser("user").password("password").roles("USER")
//            .and()
//            .withUser("admin").password("password").roles("ADMIN")
//        println()
        builder.jdbcAuthentication()
            .dataSource(dataSource).withDefaultSchema()
            .withUser(
                User.withUsername("user")
                    .password(passwordEncoder()!!.encode("pass"))
                    .roles("USER"))
            .withUser( User.withUsername("admin")
                .password(passwordEncoder()!!.encode("password"))
                .roles("ADMIN"))
    }

//    fun configureGlobal(@Autowired builder: AuthenticationManagerBuilder) {
//        builder.jdbcAuthentication()
//            .dataSource(dataSource).withDefaultSchema()
//            .withUser(
//                User.withUsername("user")
//                .password(passwordEncoder()!!.encode("pass"))
//                .roles("USER"))
//            .withUser( User.withUsername("admin")
//                            .password(passwordEncoder()!!.encode("password"))
//                .roles("ADMIN"))
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}