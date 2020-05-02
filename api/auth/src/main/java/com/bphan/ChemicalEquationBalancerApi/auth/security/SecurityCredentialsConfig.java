package com.bphan.ChemicalEquationBalancerApi.auth.security;

import com.bphan.ChemicalEquationBalancerApi.common.auth.JwtConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .cors()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(usernamePasswordAuthenticationFailureHandler())
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
        .and()
        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
        .anyRequest().authenticated()
        .and()
        .logout().permitAll();

        // http.csrf().disable()
        //     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        //     .exceptionHandling()
        //     .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
        //     .addFilter(new UserSignupFilter(authenticationManager(), jwtConfig))
        //     .authorizeRequests()
        //     .antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
        //     .and()
        //     .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
        //     .authorizeRequests()
        //     .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
        //     .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    AuthenticationFailureEntryPoint usernamePasswordAuthenticationFailureHandler() {
        return new AuthenticationFailureEntryPoint();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addExposedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}