package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.repo.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionServiceTest {

    private AuctionMapper auctionMapper = mock(AuctionMapper.class);

    private AuctionRepository auctionRepository = mock(AuctionRepository.class);

    private SessionManager sessionManager = mock(SessionManager.class);

    private AuctionService auctionService = new AuctionService(auctionMapper, auctionRepository, sessionManager);

    @Test
    void createAuctionWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> auctionService.createAuction(null));
    }

    @Test
    void createAuctionWhileAuthenticatedShouldSuccess() {
        CreateAuctionBody body = new CreateAuctionBody("title", "description", 10.0, Instant.parse("3025-01-01T12:00:50.00Z"));
        Auction mappedAuction = new Auction(body.title(), body.description(), body.dueDate(), body.initialBid(), new User(1), null);
        AuctionDTO auctionDTO = new AuctionDTO(1, mappedAuction.getTitle(), mappedAuction.getDescription(), mappedAuction.getInitialBid(), mappedAuction.getDueDate(), Instant.now(), mappedAuction.getOwner(), mappedAuction.getWinner());
        when(auctionMapper.mapToAuction(refEq(body), any())).thenReturn(mappedAuction);
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(1);
        when(auctionRepository.save(mappedAuction)).thenReturn(mappedAuction);
        when(auctionMapper.mapToAuctionDTO(mappedAuction)).thenReturn(auctionDTO);

        AuctionDTO response = auctionService.createAuction(body);

        assertEquals(body.title(), response.title());
        assertEquals(body.description(), response.description());
        assertEquals(body.dueDate(), response.dueDate());
        assertEquals(body.initialBid(), response.initialBid());
    }

    @Test
    void createAuctionWhileNotAuthenticatedShouldThrow() {
        CreateAuctionBody body = new CreateAuctionBody("title", "description", 10.0, Instant.parse("3025-01-01T12:00:50.00Z"));
        when(sessionManager.isAuthenticated()).thenReturn(false);
        assertThrows(NotAuthenticatedException.class, () -> auctionService.createAuction(body));
    }
}