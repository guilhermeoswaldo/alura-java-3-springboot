package br.alura.medvollapi.infra.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    // Configuração da corrente de segurança ativando ou inativando determinados componentes (Passos da "Alfândega")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
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
