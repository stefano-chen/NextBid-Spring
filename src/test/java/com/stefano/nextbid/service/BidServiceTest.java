package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.dto.CreateBidBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.Bid;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.AmountTooLowException;
import com.stefano.nextbid.exceptions.AuctionClosedException;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.repo.AuctionRepository;
import com.stefano.nextbid.repo.BidRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidServiceTest {
    private final BidRepository bidRepository = mock(BidRepository.class);
    private final AuctionRepository auctionRepository = mock(AuctionRepository.class);
    private final SessionManager sessionManager = mock(SessionManager.class);
    private final BidMapper bidMapper = mock(BidMapper.class);
    private final BidService bidService = new BidService(bidRepository, auctionRepository, bidMapper, sessionManager);

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

    @Test
    void createBidWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> bidService.createBid(null, null));
    }

    @Test
    void createBidWhileNotAuthenticatedShouldThrow() {
        when(sessionManager.isAuthenticated()).thenReturn(false);
        assertThrows(
                NotAuthenticatedException.class,
                () -> bidService.createBid(1, new CreateBidBody(10))
        );
    }

    @Test
    void createBidWithInvalidAuctionIdShouldThrow() {
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(auctionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(
                InvalidIdException.class,
                () -> bidService.createBid(-10, new CreateBidBody(100))
        );
    }

    @Test
    void createBidForExpiredAuctionShouldThrow() {
        Auction auction = new Auction(
                "auction", "desc", Instant.now().minus(1, ChronoUnit.DAYS),
                10, new User(1), null
        );
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        assertThrows(
                AuctionClosedException.class,
                () -> bidService.createBid(1, new CreateBidBody(10))
        );
    }

    @Test
    void createBidWithLowBidThanLastBidShouldThrow() {
        Auction auction = new Auction(
                "auction", "desc", Instant.now().plus(1, ChronoUnit.DAYS),
                10, new User(1), null
        );
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        List<Bid> allBids = new ArrayList<>();
        Bid lastBid = new Bid();
        lastBid.setAmount(1000);
        allBids.add(lastBid);
        when(bidRepository.findAllByAuctionOrderByAmountDesc(any(Auction.class))).thenReturn(allBids);
        assertThrows(
                AmountTooLowException.class,
                () -> bidService.createBid(1, new CreateBidBody(10))
        );
    }

    @Test
    void createBidWithValidDataShouldSuccess() {
        Auction auction = new Auction(
                "auction", "desc", Instant.now().plus(1, ChronoUnit.DAYS),
                10, new User(1), null
        );
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        List<Bid> allBids = new ArrayList<>();
        Bid lastBid = new Bid();
        lastBid.setAmount(100);
        allBids.add(lastBid);
        when(bidRepository.findAllByAuctionOrderByAmountDesc(any(Auction.class))).thenReturn(allBids);
        assertDoesNotThrow(() -> bidService.createBid(1, new CreateBidBody(1000)));
    }

    @Test
    void getBidDetailWithValidIdShouldSuccess() {
        Bid foundBid = new Bid(new User(1), new Auction(1), 100);
        foundBid.setId(1);
        when(bidRepository.findById(1)).thenReturn(Optional.of(foundBid));
        when(bidMapper.mapToBidDTO(foundBid)).thenCallRealMethod();
        BidDTO result = bidService.getBidDetail(1);
        assertEquals(1, result._id());
    }

    @Test
    void getBidDetailWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> bidService.getBidDetail(null));
    }

    @Test
    void getBidDetailWithInvalidIdShouldThrow() {
        when(bidRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> bidService.getBidDetail(10930));
    }
}