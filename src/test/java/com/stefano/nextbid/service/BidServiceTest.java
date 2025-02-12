package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.Bid;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.repo.BidRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidServiceTest {

    private final BidRepository bidRepository = mock(BidRepository.class);

    private final BidMapper bidMapper = mock(BidMapper.class);

    private final BidService bidService = new BidService(bidRepository, bidMapper);

    @Test
    void findByAuctionIdWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> bidService.findByAuctionId(null));
    }


    @Test
    void findByAuctionIdWhenThereAreMatchesShouldReturnNotEmptyList() {
        List<Bid> matchedBids = List.of(
                new Bid(new User(1), new Auction(100), 13),
                new Bid(new User(2), new Auction(100), 11)
        );

        when(bidRepository.findAllByAuctionOrderByAmountDesc(any())).thenReturn(matchedBids);
        when(bidMapper.mapToBidDTO(any())).thenCallRealMethod();

        List<BidDTO> bids = this.bidService.findByAuctionId(100);

        assertEquals(2, bids.size());
    }

    @Test
    void findByAuctionIdWhenThereAreNoMatchesShouldReturnEmptyList() {

        when(bidRepository.findAllByAuctionOrderByAmountDesc(any())).thenReturn(List.of());
        when(bidMapper.mapToBidDTO(any())).thenCallRealMethod();

        List<BidDTO> bids = this.bidService.findByAuctionId(100);

        assertEquals(0, bids.size());
    }
}