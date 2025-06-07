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
        // Explicitly tell Spring MVC to NOT consider /api/** as a static resource path.
        // This ensures that API requests are routed to controllers.
        registry.addResourceHandler("/api/**").addResourceLocations("classpath:/nonexistent/");
        // You can also consider adding other static resource handlers here if needed,
        // for example: registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

        // Default Spring Boot static resource handling (if not explicitly overridden)
        // super.addResourceHandlers(registry); // Uncomment if you want to explicitly call super
    }
}
