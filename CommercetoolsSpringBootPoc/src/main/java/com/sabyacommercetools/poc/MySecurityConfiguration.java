package com.sabyacommercetools.poc;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests((requests) -> requests.anyRequest().permitAll())
                .cors().disable()
                .csrf().disable();



    }

}
