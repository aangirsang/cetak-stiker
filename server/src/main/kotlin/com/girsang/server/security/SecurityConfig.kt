package com.girsang.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {


    @Bean
    fun users(): UserDetailsService {
        val user: UserDetails = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("secret")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable() // untuk demo; pertimbangkan CSRF untuk form


// allow H2 console frames
        http.headers().frameOptions().sameOrigin()


        http.authorizeHttpRequests()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
            .and()
            .httpBasic()
        return http.build()
    }
}