package com.github.kokorin.springjwtdoneright;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .addFilter(usernamePasswordFilter())
                .addFilter(authenticationFilter())

                .authenticationProvider(preAuthenticationProvider())

                .authorizeRequests()
                .antMatchers("login").permitAll()
                .antMatchers("/**").authenticated();

    }

    /**
     * Bean annotation is required for this Filter to be initialized by Spring Context.
     * Internally this filter will extract username and password and create UsernamePasswordAuthenticationToken.
     * This token will be authenticated by corresponding AuthenticationProvider.
     */
    @Bean
    public Filter usernamePasswordFilter() throws Exception {
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
    public AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        // Default UserDetailsCheck will ensure, that authenticated UserDetails is enabled and,
        // what is most important, that credentials haven't expired
        provider.setPreAuthenticatedUserDetailsService(preAuthenticatedUserDetailsService());

        return provider;
    }

    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthenticatedUserDetailsService() {
        return new SimpleAuthenticationUserDetailsService();
    }
}
