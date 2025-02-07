package com.stefano.nextbid.repo;

import com.stefano.nextbid.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
