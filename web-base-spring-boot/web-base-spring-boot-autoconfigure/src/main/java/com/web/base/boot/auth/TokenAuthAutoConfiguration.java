package com.web.base.boot.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableConfigurationProperties({TokenProperties.class})
@ConditionalOnProperty(prefix = "boot.auth", name = "enable", havingValue = "true")
public class TokenAuthAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private TokenProperties tokenProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(uidHandlerMethodArgumentResolver());
    }

    public UidHandlerMethodArgumentResolver uidHandlerMethodArgumentResolver() {
        return new UidHandlerMethodArgumentResolver(tokenProperties);
    }
}
