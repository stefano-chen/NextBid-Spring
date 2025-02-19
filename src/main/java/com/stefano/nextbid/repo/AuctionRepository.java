package com.stefano.nextbid.repo;

import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    List<Auction> findAllByOwner(User owner);

    List<Auction> findAllByWinner(User winner);

    List<Auction> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    List<Auction> findAllByOrderByCreatedAtDesc();
}
