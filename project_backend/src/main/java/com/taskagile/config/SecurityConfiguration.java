package com.taskagile.config;

import com.taskagile.domain.common.security.AccessDeniedHandlerImpl;
import com.taskagile.web.apis.authenticate.AuthenticationFilter;
import com.taskagile.web.apis.authenticate.SimpleAuthenticationFailureHandler;
import com.taskagile.web.apis.authenticate.SimpleAuthenticationSuccessHandler;
import com.taskagile.web.apis.authenticate.SimpleLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC = new String[] {
            "/error",
            "/login",
            "/logout",
            "/register",
            "/api/registrations"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .authorizeRequests()
                // 메소드를 호출해서 스프링 시큐리티에 HTTP 요청에 기반한 접근제한을 하고 있음을 알려줌
                // -> 반환결과는 ExpressionInterceptUrlRegistry 인스턴스
                    .antMatchers(PUBLIC).permitAll()
                    // 스프링 시큐리티가 처리하기를 원하는 요청의 경로를 명시.
                    .anyRequest().authenticated()
                    // PUBLIC에 명시된 경로 외 다른 요청은 인증된 사용자만 접근할 수 있음을 명시
                .and()
                    .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    // 메소드 호출 체인을 HTTP 오브젝트로 복원한다.
                    .formLogin()
                // 스프링 시큐리티에 애플리케이션이 폼을 기반으로 한 인증을 활용하고 있음으 알려줌
                    .loginPage("/login")
                    // 로그인 페이지의 경로를 명시
                .and()
                    .logout()
                // 로그아웃의 동작을 의미
                    .logoutUrl("/logout")
                    //
//                    .logoutSuccessUrl("/login?logged-out")
                    // 로그아웃 후 리다이렉트되는 경로
                    .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                    .csrf().disable();
                // Cross-Site Request Forgery, CSRF 기능으 끄도록 요청
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/static/**",
                "/js/**",
                "/css/**",
                "/images/**",
                "/favicon.ico"
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleLogoutSuccessHandler();
    }

    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }
}
