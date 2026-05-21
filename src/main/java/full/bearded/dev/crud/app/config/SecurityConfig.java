package full.bearded.dev.crud.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

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
                        .requestMatchers(HttpMethod.GET, API_USERS_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
                        .requestMatchers(API_USERS_PATH).hasRole(ADMIN_ROLE)
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(final PasswordEncoder passwordEncoder) {

        final var user = User.withUsername("user")
                             .password(passwordEncoder.encode("password"))
                             .roles(USER_ROLE)
                             .build();

        final var admin = User.withUsername("admin")
                              .password(passwordEncoder.encode("password"))
                              .roles(ADMIN_ROLE)
                              .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}