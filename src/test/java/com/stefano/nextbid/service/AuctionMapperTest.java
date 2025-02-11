package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AuctionMapperTest {
    private final AuctionMapper auctionMapper = new AuctionMapper();

    @Test
    void mapToAuctionWithAtLeastOneNullArgShouldReturnNull() {
        assertNull(auctionMapper.mapToAuction(null, new User(1)));
    }

    @Test
    void mapToAuctionWithValidArgShouldReturnAuction() {
        Auction auction = auctionMapper.mapToAuction(new CreateAuctionBody("title", "description", 10, Instant.parse("3024-02-20T07:00:00.00Z")), new User(1));
        assertEquals("title", auction.getTitle());
        assertEquals("description", auction.getDescription());
        assertEquals(10, auction.getInitialBid());
        assertEquals(Instant.parse("3024-02-20T07:00:00.00Z"), auction.getDueDate());
        assertEquals(1, auction.getOwner().getId());
    }

    @Test
    void mapToAuctionDTOWithNullArgShouldReturnNull() {
        assertNull(auctionMapper.mapToAuctionDTO(null));
    }

    @Test
    void mapToAuctionDTOWithValidArgShouldReturnAuctionDTO() {
        AuctionDTO auctionDTO = auctionMapper.mapToAuctionDTO(new Auction("title", "description", Instant.parse("3024-02-20T07:00:00.00Z"), 10, new User(1), null));
        assertEquals("title", auctionDTO.title());
        assertEquals("description", auctionDTO.description());
        assertEquals(10, auctionDTO.initialBid());
        assertEquals(Instant.parse("3024-02-20T07:00:00.00Z"), auctionDTO.dueDate());
        assertEquals(1, auctionDTO.owner().getId());
    }
}