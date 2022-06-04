package tn.esprit.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.security.jwt.JwtAccessDenied;
import tn.esprit.spring.security.jwt.JwtAuthenticationHttp403;
import tn.esprit.spring.security.jwt.JwtAuthorizationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JwtAccessDenied jwtAccessDenied;
    
    @Autowired
    private JwtAuthenticationHttp403 jwtAuthenticationHttp403;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors();//cross-origin-resource-sharing
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/authentication/**", "/swagger-ui/", "/swagger-ui/**",
                		"/v2/api-docs", "/configuration/ui",
                		"/swagger-resources/**", "/configuration/security",
                		"/swagger-ui.html", "/webjars/**","/course/**", "/offer/**", "/forum/**","/file/**", "/chat-websocket/**").permitAll()//login and register pre-path
                .antMatchers("/api/admin/**").hasRole(Role.ADMIN.name()) 
                .anyRequest().authenticated()
		        //.and()
		        //.oauth2Login();
		         .and()
		         .exceptionHandling().accessDeniedHandler(jwtAccessDenied)
		         .authenticationEntryPoint(jwtAuthenticationHttp403);

        

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);  
    }

    //Why don't we describe it as a component, because of scope.
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter()
    {
        return new JwtAuthorizationFilter();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }
}