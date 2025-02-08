package com.stefano.nextbid.config;

import com.password4j.BcryptFunction;
import com.password4j.types.Bcrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

    @Bean
    public BcryptFunction bcryptFunction() {
        return BcryptFunction.getInstance(Bcrypt.B, 12);

    }
}
