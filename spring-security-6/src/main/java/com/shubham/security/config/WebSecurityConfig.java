package com.shubham.security.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtAutheticationFilter jwtAutheticationFilter;

    public WebSecurityConfig(UserDetailsService userDetailsService, JwtAutheticationFilter jwtAutheticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAutheticationFilter = jwtAutheticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                request -> request
                        .requestMatchers("register","login").permitAll()
                        .anyRequest()
                        .authenticated()) //authenticate request
//                .csrf(Customizer.withDefaults())      //CSRF with default configuration /by default present
                .csrf(csrf -> csrf.disable())  //to disable the csrf.
//                .formLogin(Customizer.withDefaults()) //Add form based login
                .httpBasic(Customizer.withDefaults())  //have basic authetication
                .addFilterBefore(jwtAutheticationFilter, UsernamePasswordAuthenticationFilter.class) //this will for jwt filter should be first work before Usernamepassword filter.
        ;
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
         UserDetails shubham = User.withUsername("shubham").password("{noop}123").roles("USER").build();
        UserDetails pranit = User.withUsername("pranit").password("{noop}123").roles("USER").build();

        return new InMemoryUserDetailsManager(shubham,pranit);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(14);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

