package com.stefano.nextbid.repo;

import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findAllByAuctionOrderByAmountDesc(Auction auction);
}
