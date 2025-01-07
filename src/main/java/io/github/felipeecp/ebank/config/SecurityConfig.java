package io.github.felipeecp.ebank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.felipeecp.ebank.exception.ApiError;
import io.github.felipeecp.ebank.repository.UserRepository;
import io.github.felipeecp.ebank.security.CustomUserDetailService;
import io.github.felipeecp.ebank.security.JwtAuthenticationFilter;
import io.github.felipeecp.ebank.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/swagger-resources",
            "/webjars/**"
    };

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailService(userRepository);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService){
        return new JwtAuthenticationFilter(jwtTokenProvider,userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, ex) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            ApiError error = new ApiError(
                                    HttpStatus.FORBIDDEN.value(),
                                    "Acesso negado. Autenticação é necessária para acessar este recurso."
                            );
                            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
                        })
                        .accessDeniedHandler((request, response, ex) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            ApiError error = new ApiError(
                                    HttpStatus.FORBIDDEN.value(),
                                    "Acesso negado. Você não tem permissão para acessar este recurso."
                            );
                            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
