package cc.flyee.branchart.config;

import cc.flyee.branchart.filters.AuthorizeInterceptor;
import cc.flyee.branchart.filters.SignitureInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Bean
    SignitureInterceptor signInterceptor() {
        return new SignitureInterceptor();
    }

    @Bean
    AuthorizeInterceptor authInterceptor() {
        return new AuthorizeInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/auth/logout")
                .addPathPatterns("/user/edit")
                .addPathPatterns("/user/changepwd")
                .addPathPatterns("/article/create")
                .addPathPatterns("/article/update/*")
                .addPathPatterns("/article/delete/*")
                .addPathPatterns("/article/linkupper/*/*/*")
                .addPathPatterns("/article/optag/*/*/*")
                .addPathPatterns("/book/create")
                .addPathPatterns("/book/update/*")
                .addPathPatterns("/book/addarticle/*")
                .addPathPatterns("/book/delarticle/*")
                .addPathPatterns("/comment/send")
                .addPathPatterns("/comment/del/*")
                .addPathPatterns("/social/news")
                .addPathPatterns("/social/check/*")
                .addPathPatterns("/social/follow/*/*/*")
                .addPathPatterns("/fav/list")
                .addPathPatterns("/fav/del/*")
                .addPathPatterns("/fav/add/*/*/*");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                .maxAge(3600);
    }

}
