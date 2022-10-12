package com.opencourse.chapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.opencourse.chapter.security.CustomAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain authorisationServerFilterChain(HttpSecurity http)throws Exception{
        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().mvcMatchers("/api/v1/chapter/valid").permitAll()
        .mvcMatchers(HttpMethod.GET,"/api/v1/chapter/**").authenticated()
        .mvcMatchers("/api/v1/chapter/**").hasAuthority("TEACHER")
        .mvcMatchers(HttpMethod.GET,"/api/v1/element/**").authenticated()
        .mvcMatchers("/api/v1/element/**").hasAuthority("TEACHER")
        .and().addFilterBefore(filter, AnonymousAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration(){
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }
}
