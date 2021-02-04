package com.esliceu.config;

import com.esliceu.interceptors.CheckCSRFTokenInterceptor;
import com.esliceu.interceptors.GenerateCSRFTokenInterceptor;
import com.esliceu.interceptors.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.esliceu")
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    SessionInterceptor sessionInterceptor;

    @Autowired
    CheckCSRFTokenInterceptor checkCSRFTokenInterceptor;

    @Autowired
    GenerateCSRFTokenInterceptor generateCSRFTokenInterceptor;

    @Bean
    public UrlBasedViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/feed", "/detailNote", "/createNote",
                        "/deleteNote", "/deleteUserFromSharedNote",
                        "/editNote", "/logout", "/multipleDeleteNote",
                        "/md", "/shareNote", "/profile");

        registry.addInterceptor(generateCSRFTokenInterceptor)
                .addPathPatterns("/*");

        registry.addInterceptor(checkCSRFTokenInterceptor)
                .addPathPatterns("/*");
    }
/*
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        return jsonConverter;
    }
    */

}
