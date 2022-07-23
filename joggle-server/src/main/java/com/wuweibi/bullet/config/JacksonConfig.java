package com.wuweibi.bullet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wuweibi.bullet.jackson.BigDecimalSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;

@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    public ObjectMapper bigDecimalObjectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder){
        ObjectMapper objectMapper= jackson2ObjectMapperBuilder.build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(BigDecimal.class,new NumberDeserializers.BigDecimalDeserializer());
        simpleModule.addSerializer(BigDecimal.class,new BigDecimalSerializer());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}