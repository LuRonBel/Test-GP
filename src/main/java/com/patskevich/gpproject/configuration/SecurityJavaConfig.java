package com.patskevich.gpproject.configuration;

import com.vaadin.spring.access.SecuredViewAccessControl;
import com.vaadin.spring.access.ViewAccessControl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authEntryPoint;
    private AuthenticationSuccessHandler successHandler;


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
         http.csrf().disable().authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("swagger-ui.html").permitAll()
                    .anyRequest().authenticated()
                 /*.and()
                    .httpBasic()
                    .authenticationEntryPoint(authEntryPoint)*/
                 .and()
                    .formLogin().successHandler(successHandler)
                 .and()
                    .logout();
     }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ViewAccessControl accessControl() {
        return new SecuredViewAccessControl();
    }
}
