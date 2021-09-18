package com.postsquad.scoup.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postsquad.scoup.web.signin.controller.SignInInterceptor;
import com.postsquad.scoup.web.user.UserArgumentResolver;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@PropertySource("classpath:sign-in.properties")
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${sign-in.interceptor.path-to-exclude}")
    private final String[] SIGNIN_PATH_TO_EXCLUDE;

    private final SignInInterceptor signInInterceptor;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    public WebClient webClient(HttpClient httpClient, ObjectMapper baseConfig) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                                                                  .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(baseConfig)))
                                                                  .build();
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).exchangeStrategies(exchangeStrategies).build();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(SIGNIN_PATH_TO_EXCLUDE);
    }
}
