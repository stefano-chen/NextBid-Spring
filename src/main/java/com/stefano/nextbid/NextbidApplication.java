package com.stefano.nextbid;

import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NextbidApplication {


    public static void main(String[] args) {
        SpringApplication.run(NextbidApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner (UserRepository repository) {
//
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
//                User user = new User();
//                user.setName("stefano");
//                user.setUsername("stefano");
//                user.setSurname("stefano");
//                user.setPassword("stefano");
//
//                repository.save(user);
//            }
//        };
//    }

}
