
package RentEasy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            PasswordEncoder passwordEncoder) {

        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("RentEasy@123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/index.html", "/admin-login.html",
                         "/css/**", "/js/**", "/images/**")
        .permitAll()

                .requestMatchers("/api/products/**")
                .permitAll()

                .requestMatchers(HttpMethod.POST, "/api/bookings")
                .permitAll()

                .requestMatchers("/api/bookings/**")
                .hasRole("ADMIN")

                .requestMatchers("/admin.html")
                .hasRole("ADMIN")

                .anyRequest().permitAll()
            )
            .formLogin(form -> form
    .loginPage("/admin-login.html")
    .loginProcessingUrl("/perform_login")
    .defaultSuccessUrl("/admin.html", true)
    .failureUrl("/admin-login.html?error=true")
    .permitAll()
)
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}
