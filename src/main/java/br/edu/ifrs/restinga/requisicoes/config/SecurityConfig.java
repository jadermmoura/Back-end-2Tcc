package br.edu.ifrs.restinga.requisicoes.config;

import br.edu.ifrs.restinga.requisicoes.modelo.servico.JwtServico;
import br.edu.ifrs.restinga.requisicoes.modelo.servico.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
 
    @Autowired
    UsuarioServico userDetails;
    
    @Autowired
    JwtServico jwtService;
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtFilterConfig(userDetails,jwtService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/usuarios/alunos/","/api/login/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/anexos/cursos").permitAll()
                .antMatchers(HttpMethod.POST,"/api/anexos/disciplinas").permitAll()
                .antMatchers(HttpMethod.POST,"/api/anexos/usuarios").permitAll()
                .antMatchers(HttpMethod.GET,"/api/usuarios/pesquisa/{userName}").permitAll()
                .antMatchers(HttpMethod.GET,"/api/email/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/usuarios/matricula/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/usuarios/**").hasRole("SERVIDOR")
                .antMatchers(HttpMethod.GET,"/api/usuarios/auth/","/api/requisicoes/alunos/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/usuarios/**").hasAnyRole("SERVIDOR","PROFESSOR","ALUNO")
                .antMatchers(HttpMethod.PUT,"/api/cursos/**").hasRole("SERVIDOR")
                .antMatchers(HttpMethod.PUT,"/api/requisicoes/**").hasAnyRole("SERVIDOR","PROFESSOR")
                .antMatchers(HttpMethod.GET,"/api/cursos/").hasAnyRole("ALUNO","SERVIDOR","PROFESSOR")
                .antMatchers(HttpMethod.POST,"/api/requisicoes/** ").hasRole("ALUNO")
                .antMatchers().permitAll()
                .anyRequest().authenticated()
                .and()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    } 

     @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs","/swagger-resources/**", "/configuration/**", "/swagger-ui.html**", "/webjars/**","/static/**","/images/**");
    }
    
    
}
