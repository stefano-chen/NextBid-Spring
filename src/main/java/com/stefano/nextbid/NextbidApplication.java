package com.stefano.nextbid;

import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.repo.AuctionRepository;
import com.stefano.nextbid.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@SpringBootApplication
@EnableJpaAuditing
public class NextbidApplication {


    public static void main(String[] args) {
        SpringApplication.run(NextbidApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner (UserRepository repository, AuctionRepository auctionRepository) {
//
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
//                User user = new User();
//                user.setName("flavio");
//                user.setUsername("flavio");
//                user.setSurname("flavio");
//                user.setPassword("flavio");
//
//                user=repository.save(user);
//
//                Auction auction = new Auction();
//                auction.setTitle("auction");
//                auction.setDescription("auction");
//                auction.setDueDate(Instant.now().plus(1, ChronoUnit.DAYS));
//                auction.setOwner(user);
//                auction.setInitialBid(10.0);
//                auctionRepository.save(auction);
//            }
//        };
//    }

}
