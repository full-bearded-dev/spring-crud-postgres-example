package full.bearded.dev.crud.app.config;

import full.bearded.dev.crud.app.config.properties.AdminProperties;
import full.bearded.dev.crud.app.user.model.UserRole;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(AdminProperties.class)
public class SecurityConfig {

    private static final String SWAGGER_UI_PATH = "/swagger-ui/**";
    private static final String V_3_API_DOCS_PATH = "/v3/api-docs/**";
    private static final String API_DOCS_PATH = "/docs/**";
    private static final String API_DOCS_JSON_PATH = "/docs-json/**";
    private static final String API_USERS_PATH = "/api/users/**";

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_UI_PATH,
                                         V_3_API_DOCS_PATH,
                                         API_DOCS_PATH,
                                         API_DOCS_JSON_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, API_USERS_PATH).hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
                        .requestMatchers(API_USERS_PATH).hasRole(UserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}