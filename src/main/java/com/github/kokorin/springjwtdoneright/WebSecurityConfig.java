package com.github.kokorin.springjwtdoneright;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(preAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .addFilter(usernamePasswordFilter())
                .addFilter(authenticationFilter())

                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/**").authenticated();

    }

    /**
     * Bean annotation is required for this Filter to be initialized by Spring Context.
     * Internally this filter will extract username and password and create UsernamePasswordAuthenticationToken.
     * This token will be authenticated by corresponding AuthenticationProvider.
     */
    @Bean
    public Filter usernamePasswordFilter() throws Exception {
        // By default filter listens to /login requests
        // It awaits URL encoded username and password parameters
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();

        // Required, not injected automatically
        filter.setAuthenticationManager(authenticationManager());
        // Filter works as usual, JWT is generated in success handler
        filter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());

        return filter;
    }


    /**
     * Bean annotation is required for this Filter to be initialized by Spring Context.
     * Internally this filter will check AUTHORIZATION header and create PreAuthenticatedAuthenticationToken.
     * This token will be authenticated by corresponding AuthenticationProvider.
     */
    @Bean
    public Filter authenticationFilter() throws Exception {
        JwtPreAuthenticatedProcessingFilter filter = new JwtPreAuthenticatedProcessingFilter();

        // Required, not injected automatically
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(simpleUserDetailsService());
        provider.setPasswordEncoder(plaintextPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        // Default UserDetailsCheck will ensure, that authenticated UserDetails is enabled and,
        // what is most important, that credentials haven't expired
        provider.setPreAuthenticatedUserDetailsService(preAuthenticatedUserDetailsService());

        return provider;
    }

    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthenticatedUserDetailsService() {
        return new JwtAuthenticationUserDetailsService();
    }

    @Bean
    public UserDetailsService simpleUserDetailsService() {
        return new SimpleUserDetailsService();
    }

    @Bean
    public PasswordEncoder plaintextPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
}
