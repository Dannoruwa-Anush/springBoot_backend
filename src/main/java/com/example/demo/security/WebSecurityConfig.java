package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.common.projectEnum.RoleName;
import com.example.demo.security.jwt.AuthEntryPointJwt;

import static org.springframework.security.config.Customizer.withDefaults;
import com.example.demo.security.jwt.AuthTokenFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    /*
     * Spring Boot’s auto-configuration mechanism automatically picks up your
     * WebSecurityConfig class and applies it. When you annotate your configuration
     * class with @Configuration, Spring Boot recognizes it as a part of the
     * configuration and applies the security settings to the application.
     */

    /*
     * JWT flow diagram
     * WebSecurityConfig
     * REQUEST
     * ->SecurityFilterChain
     * ->endpoint : /auth/** - llowed to pass without authentication.
     * -> AuthTokenFilter : check for the presence and validity of a JWT token in
     * the reques
     * 
     * -> DaoAuthenticationProvider : verify the user's credentials
     * -> AuthEntryPointJwt : handles the exception and returns an appropriate
     * response
     * 
     */

    /*
     * @Bean is used to declare objects that Spring needs to manage. These can be
     * instances of your custom classes, utility classes, service objects, or any
     * other type of bean that should be instantiated and managed by Spring
     * 
     * @Bean is commonly used within @Configuration classes.
     * 
     * 
     * @Autowired is a DI (Dependency Injection) annotation, and it's used to tell
     * Spring where to inject an already existing bean. It can be applied to
     * constructors, fields, or setter methods.
     */

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /*
     * In this case, @Autowired is telling Spring to inject an instance of the
     * AuthEntryPointJwt class into the unauthorizedHandler field.
     * 
     * 
     * For this to work, AuthEntryPointJwt must be registered as a Spring bean,
     * either through annotations like @Component or through an explicit bean
     * definition in a configuration class using @Bean.
     */
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // ---
    /*
     * @Bean returns an object as a bean definition
     * Ex: In here :@Bean and returns a userDetailsService object
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
    // ---

    // ---
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    // ---

    // ---
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    // ---

    // ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ---

    // ---
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    // ---

    // ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults()).csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Access is restricted to ADMIN
                        .requestMatchers("/userpermissions/**").hasRole(RoleName.ADMIN.getRoleName())
                        .requestMatchers("/role/**").hasRole(RoleName.ADMIN.getRoleName())
                        .requestMatchers("/user/**").hasRole(RoleName.ADMIN.getRoleName())
                        // *****

                        /*For /category, /subCategory, /author, /book
                         * Aceess is restricted according to following rule
                         * ADMIN        : VIEW, ADD, UPDATE, DELETE
                         * MANAGER      : VIEW, ADD, UPDATE
                         * Cashier      : VIEW
                         * Customer     : VIEW
                         * without login: VIEW
                         */
                        //category
                        .requestMatchers(HttpMethod.DELETE, "/category/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName())

                        .requestMatchers(HttpMethod.PUT, "/category/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        .requestMatchers(HttpMethod.POST, "/category/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        //subCategory
                        .requestMatchers(HttpMethod.DELETE, "/subCategory/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName())

                        .requestMatchers(HttpMethod.PUT, "/subCategory/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        .requestMatchers(HttpMethod.POST, "/subCategory/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        //author
                        .requestMatchers(HttpMethod.DELETE, "/author/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName())

                        .requestMatchers(HttpMethod.PUT, "/author/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        .requestMatchers(HttpMethod.POST, "/author/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        //book
                        .requestMatchers(HttpMethod.DELETE, "/book/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName())

                        .requestMatchers(HttpMethod.PUT, "/book/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        .requestMatchers(HttpMethod.POST, "/book/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName(), RoleName.MANAGER.getRoleName())

                        //order
                        /*For /order
                         * Aceess is restricted according to following rule
                         * ADMIN        : VIEW, UPDATE, DELETE
                         * MANAGER      : VIEW
                         * Cashier      : VIEW, ADD
                         * Customer     : VIEW, ADD
                         */
                        .requestMatchers(HttpMethod.DELETE, "/order/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName())

                        .requestMatchers(HttpMethod.PUT, "/order/**")
                        .hasAnyRole(RoleName.ADMIN.getRoleName())

                        .requestMatchers(HttpMethod.POST, "/book/**")
                        .hasAnyRole(RoleName.CUSTOMER.getRoleName(), RoleName.CASHIER.getRoleName())

                        // Access is unauthenticated (for all)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/subCategory/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/author/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/book/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/order/getShoppingCartTotal").permitAll()

                        .anyRequest().authenticated()); // Authenticate all other requests

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // ---
}
