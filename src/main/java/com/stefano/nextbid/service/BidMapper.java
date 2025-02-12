package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Bid;
import org.springframework.stereotype.Service;

@Service
public class BidMapper {

    public BidDTO mapToBidDTO(Bid bid) {
        if (bid == null)
            return null;
        return new BidDTO(bid.getId(), bid.getUser(), bid.getAuction(), bid.getAmount(), bid.getCreatedAt());
    }
}
