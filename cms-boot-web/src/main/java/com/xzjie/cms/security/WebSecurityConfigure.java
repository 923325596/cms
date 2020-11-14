package com.xzjie.cms.security;

import com.xzjie.cms.security.filter.SecurityAuthenticationFilter;
import com.xzjie.cms.security.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Autowired
    private SecurityAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private SecurityAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private SecurityAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private SecurityAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private SecurityLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/*", "/js/**", "/image/**", "/fonts/**", "/i/**", "/img/**", "/logo.png", "favicon.ico", "/demo", "/api/hot/data").permitAll()
                .antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .antMatchers("/", "/index", "/article/**", "/login**", "/oauth/**", "/swagger-ui.html", "/doc.html", "/error**").permitAll()
                .antMatchers("/app/**").permitAll()
//                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/auth/sign") //.usernameParameter("username").passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and().logout()
                .logoutSuccessHandler(logoutSuccessHandler)
        ;

//        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityAuthenticationFilter authenticationFilter() {
        return new SecurityAuthenticationFilter();
    }

}
