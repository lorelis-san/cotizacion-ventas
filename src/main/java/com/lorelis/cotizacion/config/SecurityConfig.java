package com.lorelis.cotizacion.config;

import com.lorelis.cotizacion.jwt.JwtAuthenticationFilter;
import com.lorelis.cotizacion.jwt.JwtEntryPoint;
import com.lorelis.cotizacion.service.usuario.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
////                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/register", "/auth/login")
////                        .permitAll()
////                        .anyRequest().authenticated())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/register", "/auth/login", "/loginView").permitAll()
////                        .requestMatchers("/admin/**").hasRole("ADMIN")
////                        .requestMatchers("/home/**", "/user/**").hasRole("USER")
//                        .anyRequest().authenticated())
//
//                .httpBasic(Customizer.withDefaults())
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint()))
//                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login", "/loginView", "/registerView", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint()))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public JwtAuthenticationFilter jwtTokenFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public JwtEntryPoint jwtEntryPoint(){
        return new JwtEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserService();
    }




    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration= new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
