package com.postsquad.scoup.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postsquad.scoup.web.common.ObjectMapperUtils;
import com.postsquad.scoup.web.signin.controller.SignInInterceptor;
import com.postsquad.scoup.web.user.UserArgumentResolver;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] SIGNIN_PATH_TO_EXCLUDE = {"/sign-in/**", "/users/**", "/oauth/**", "/h2-console/**", "/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**"};

    private final SignInInterceptor signInInterceptor;

    private final ObjectMapperUtils objectMapperUtils;

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

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
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://sc0up.herokuapp.com")
                .allowedMethods("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
        resolvers.add(new RequestParameterArgumentResolver(objectMapperUtils, List.of(mappingJackson2HttpMessageConverter)));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(SIGNIN_PATH_TO_EXCLUDE);
    }

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }
}
