package com.stefano.nextbid.repo;

import com.stefano.nextbid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Integer> {
}
