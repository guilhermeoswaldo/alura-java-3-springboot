package br.alura.medvollapi.infra.security;


import java.util.List;

import br.alura.medvollapi.infra.security.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // Ativar a possibilidade de usar a anotacao @Secured
public class SecurityConfiguration {

    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    // Configuração da corrente de segurança ativando ou inativando determinados componentes (Passos da "Alfândega")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN");
                    authorizeRequests.anyRequest().authenticated();
                }).addFilterBefore(this.tokenFilter, UsernamePasswordAuthenticationFilter.class) // Ordem de configuracão dos filtros afeta o funcionamento pois a verificacao do token deve vir antes da autenticacao do Spring
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Indicar qual a origem do front
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Serve para exportar uma classe para o Spring, fazendo com que ele consiga carregá-la e realize a sua injeção de dependência em outra classe.
    @Bean
    // Realiza de fato a autenticação e compara as senhas de criptografia (Inspetor da Alfândega)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    // Indica o tipo de criptografia de senha para o AuthenticationManager
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
