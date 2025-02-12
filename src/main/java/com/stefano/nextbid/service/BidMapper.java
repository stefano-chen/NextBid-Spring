package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Bid;

public class BidMapper {

    public BidDTO mapToBidDTO(Bid bid) {
        return new BidDTO(bid.getId(), bid.getUser(), bid.getAuction(), bid.getAmount(), bid.getCreatedAt());
    }
}
