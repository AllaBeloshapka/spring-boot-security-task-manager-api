package com.example.taskmanagerapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ======================================================
 * ГЛАВНАЯ КОНФИГУРАЦИЯ SPRING SECURITY
 * ======================================================
 *
 * Этот класс определяет:
 * 1. Какие endpoints защищены, а какие публичные
 * 2. Как проверяются пароли (PasswordEncoder)
 * 3. Как загружаются пользователи (UserDetailsService)
 * 4. Как работает аутентификация (Basic Auth)
 */
@Configuration
@EnableWebSecurity  // Включает Spring Security
@EnableMethodSecurity  // Включает аннотации @PreAuthorize, @Secured
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * ======================================================
     * ШАГ 1: SecurityFilterChain - ПРАВИЛА ДОСТУПА
     * ======================================================
     *
     * SecurityFilterChain определяет:
     * - Какие URL требуют аутентификации
     * - Какие доступны всем
     * - Какие требуют определённых ролей
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ============ CSRF PROTECTION ============
                // CSRF = Cross-Site Request Forgery
                // Для REST API обычно отключаем, т.к. нет браузерных форм
                .csrf(csrf -> csrf.disable())

                // ============ AUTHORIZATION RULES ============
                .authorizeHttpRequests(auth -> auth
                        // Публичные endpoints (доступны ВСЕМ, даже без логина)
                        .requestMatchers("/api/auth/**").permitAll()

                        // ВСЕ остальные endpoints требуют аутентификации
                        // (пользователь должен войти в систему)
                        .anyRequest().authenticated()
                )

                // ============ AUTHENTICATION METHOD ============
                // Используем Basic Authentication
                // Формат: Authorization: Basic base64(username:password)
                .httpBasic(Customizer.withDefaults())

                // ============ SESSION MANAGEMENT ============
                // STATELESS = сервер не хранит сессии
                // Каждый запрос должен содержать credentials
                // Идеально для REST API!
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    /**
     * ======================================================
     * ШАГ 2: PasswordEncoder - ХЭШИРОВАНИЕ ПАРОЛЕЙ
     * ======================================================
     *
     * BCryptPasswordEncoder:
     * - Хэширует пароли с солью (salt)
     * - Невозможно обратить хэш в пароль
     * - Даже одинаковые пароли дают разные хэши!
     *
     * Пример:
     * password "123456" → $2a$10$N9qo8uLOickgx2ZMRZoMye...
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ======================================================
     * ШАГ 3: AuthenticationProvider - СВЯЗКА КОМПОНЕНТОВ
     * ======================================================
     *
     * DaoAuthenticationProvider связывает:
     * - UserDetailsService (загружает пользователя из БД)
     * - PasswordEncoder (проверяет пароль)
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * ======================================================
     * ШАГ 4: AuthenticationManager - МЕНЕДЖЕР АУТЕНТИФИКАЦИИ
     * ======================================================
     *
     * Нужен для программной аутентификации
     * (если захотим создать свой endpoint для логина)
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
