package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.Bid;
import com.stefano.nextbid.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BidMapperTest {
    private final BidMapper bidMapper = new BidMapper();

    @Test
    void mapToBidDTOWithNullArgShouldReturnNull() {
        assertNull(bidMapper.mapToBidDTO(null));
    }

    @Test
    void mapToBidDTOWithValidArgShouldReturnBidDTO() {
        Bid bid = new Bid(new User(1), new Auction(1), 100.0);
        bid.setId(1);
        bid.setCreatedAt(Instant.now());

        BidDTO bidDTO = bidMapper.mapToBidDTO(bid);

        assertEquals(bid.getId(), bidDTO._id());
        assertEquals(bid.getUser().get_id(), bidDTO.user().get_id());
        assertEquals(bid.getAuction().getId(), bidDTO.auction().getId());
        assertEquals(bid.getAmount(), bidDTO.amount());
    }
}