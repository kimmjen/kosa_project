package com.taskagile.domain.common.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * pdf. 459, page. 437 확인하기
 *
 * Custom annotation to get the authentication principal out of Spring's
 * {@link org.springframework.security.core.context.SecurityContext}.
 *
 * <p>
 * Reference:
 * https://docs.spring.io/spring-security/site/docs/current/reference/html/mvc.html#mvc-authentication-principal
 * </p>
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
