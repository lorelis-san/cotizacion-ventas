package com.lorelis.cotizacion.config;

import com.lorelis.cotizacion.jwt.JwtAuthenticationFilter;
import com.lorelis.cotizacion.jwt.JwtEntryPoint;
import com.lorelis.cotizacion.service.usuario.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

public class SecurityConfig {
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/error/**", "/auth/login", "/iniciarSesion", "/css/**", "/js/**", "/images/**", "/vistaCotizacion").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories",  "/suppliers", "/categories").hasRole("ADMIN")
                        .requestMatchers("/auth/register","/registrarse", "/nuevaCategoria", "/categories/guardar", "/categoria/**", "/actualizarCategoria", "/eliminarCategoria/**", "/api/suppliers/**", "suppliers/**")
                        .hasRole("ADMIN")
                        .requestMatchers(
                                "/categories",
                                "/nuevaCategoria",
                                "/categories/guardar",
                                "/categoria/**",
                                "/actualizarCategoria",
                                "/eliminarCategoria/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/products/newProducto",
                                "/products/save",
                                "/edit/**",
                                "/products/update",
                                "/delete/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/suppliers",
                                "/supplier/newSupplier",
                                "/supplier/save",
                                "/supplier/edit/**",
                                "/supplier/update/**",
                                "/supplier/delete/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/categories").hasRole("ADMIN")

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
