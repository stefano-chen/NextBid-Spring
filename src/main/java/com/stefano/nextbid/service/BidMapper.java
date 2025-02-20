package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Bid;
import org.springframework.stereotype.Service;

// A mapper is introduced to create independence between the internal representation of an entity and the response representation
@Service
public class BidMapper {

    public BidDTO mapToBidDTO(Bid bid) {
        if (bid == null)
            return null;
        return new BidDTO(bid.getId(), bid.getUser().getId(), bid.getAuction().getId(), bid.getAmount(), bid.getCreatedAt());
    }
}
