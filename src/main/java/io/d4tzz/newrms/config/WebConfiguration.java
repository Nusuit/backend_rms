package io.d4tzz.newrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver("userLocale");
        cookieLocaleResolver.setDefaultLocale(new Locale("vi"));
        cookieLocaleResolver.setCookieMaxAge(3600 * 24 * 30);
        cookieLocaleResolver.setCookiePath("/");
        return cookieLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // By default, Spring Boot serves static content from /static, /public, /resources, /META-INF/resources.
        // We don't need to explicitly add handlers for these if we're following conventions,
        // unless we have specific custom paths or need to disable caching, etc.
        // The crucial part is to ensure API paths are NOT considered static resources.

        // Standard static resource handling for frontend assets (if any beyond default locations)
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/favicon.ico");

        // If your frontend build output is in a specific folder like 'build' or 'dist' in the classpath
        // registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}
