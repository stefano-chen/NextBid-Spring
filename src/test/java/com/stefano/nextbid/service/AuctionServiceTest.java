package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.dto.UpdateAuctionBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.exceptions.NotAuthorizedException;
import com.stefano.nextbid.repo.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    void getAllAuctionWhenThereAreNoAuctionsShouldReturnEmptyList() {
        List<AuctionDTO> auctions = auctionService.getAllAuctions("");
        assertEquals(0, auctions.size());
    }

    @Test
    void getAllAuctionWhenThereAreAuctionsShouldReturnList() {
        String query = "";

        List<Auction> databaseAuctions = new ArrayList<>();

        databaseAuctions.add(new Auction("title1", "description1", Instant.now().plus(1, ChronoUnit.DAYS), 10.0, new User(1), null));
        databaseAuctions.add(new Auction("title2", "title1", Instant.now().plus(1, ChronoUnit.DAYS), 10.0, new User(1), null));

        when(auctionRepository.findAllByOrderByCreatedAtDesc()).thenReturn(databaseAuctions);

        List<AuctionDTO> auctions = auctionService.getAllAuctions(query);

        assertEquals(databaseAuctions.size(), auctions.size());
    }

    @Test
    void getAllAuctionWhenThereAreMatchingAuctionsShouldReturnList() {
        String query = "title1";

        List<Auction> databaseAuctions = new ArrayList<>();

        databaseAuctions.add(new Auction("title1", "description1", Instant.now().plus(1, ChronoUnit.DAYS), 10.0, new User(1), null));
        databaseAuctions.add(new Auction("title2", "title1", Instant.now().plus(1, ChronoUnit.DAYS), 10.0, new User(1), null));

        when(auctionRepository.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query,query)).thenReturn(databaseAuctions);

        List<AuctionDTO> auctions = auctionService.getAllAuctions(query);

        assertEquals(databaseAuctions.size(), auctions.size());
    }

    @Test
    void getAllAuctionWhenThereAreNoMatchingAuctionsShouldReturnList() {
        String query = "title1";

        List<Auction> databaseAuctions = new ArrayList<>();

        databaseAuctions.add(new Auction("title1", "description1", Instant.now().plus(1, ChronoUnit.DAYS), 10.0, new User(1), null));
        databaseAuctions.add(new Auction("title2", "title1", Instant.now().plus(1, ChronoUnit.DAYS), 10.0, new User(1), null));

        when(auctionRepository.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query,query)).thenReturn(List.of());

        List<AuctionDTO> auctions = auctionService.getAllAuctions(query);

        assertEquals(0, auctions.size());
    }

    @Test
    void getAuctionByIdWithValidIdShouldReturnAuctionDetails() {
        Integer id = 1;

        Auction auction = new Auction(id);

        when(auctionRepository.findById(id)).thenReturn(Optional.of(auction));
        when(auctionMapper.mapToAuctionDTO(auction)).thenReturn(new AuctionDTO(1, "title", "description", 10.0,Instant.now().plus(1, ChronoUnit.DAYS),Instant.now(), new User(1), null));

        AuctionDTO response = auctionService.getAuctionById(id);

        assertEquals(1, response._id());
    }

    @Test
    void getAuctionByIdWithInvalidIdShouldThrow() {
        Integer id = 1;

        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> auctionService.getAuctionById(id));
    }

    @Test
    void updateAuctionByIdWithValidIdShouldSuccess() {
        Integer id = 1;

        UpdateAuctionBody body = new UpdateAuctionBody("new title", "new desc");

        Auction auction = new Auction("title", "desc", Instant.now().plus(1, ChronoUnit.DAYS),10.0, new User(100),null);

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(100);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(auction));

        assertDoesNotThrow(() -> auctionService.updateAuctionById(id, body));
    }

    @Test
    void updateAuctionByIdWithInvalidIdShouldThrow() {
        Integer id = 190909;

        UpdateAuctionBody body = new UpdateAuctionBody("new title", "new desc");

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(100);
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> auctionService.updateAuctionById(id, body));
    }

    @Test
    void updateAuctionByIdWithAtLeastOneNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> auctionService.updateAuctionById(null, null));
    }

    @Test
    void updateAuctionByIdWhileNotAuthenticatedShouldThrow() {
        when(sessionManager.isAuthenticated()).thenReturn(false);
        assertThrows(NotAuthenticatedException.class, () -> auctionService.updateAuctionById(1, new UpdateAuctionBody("new title", "new desc")));
    }

    @Test
    void updateAuctionByIdWhileNotAuthorizedShouldThrow() {
        Integer id = 1;

        Auction auction = new Auction("title", "desc", Instant.now().plus(1, ChronoUnit.DAYS),10.0, new User(100),null);

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(1);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(auction));
        assertThrows(NotAuthorizedException.class, () -> auctionService.updateAuctionById(1, new UpdateAuctionBody("new title", "new desc")));
    }

    @Test
    void updateAuctionByIdWithPartialUpdateBodyShouldSuccess() {
        Integer id = 1;

        Auction auction = new Auction("title", "desc", Instant.now().plus(1, ChronoUnit.DAYS),10.0, new User(100),null);

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(100);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(auction));
        assertDoesNotThrow( () -> auctionService.updateAuctionById(1, new UpdateAuctionBody("new title", null)));
    }

    @Test
    void deleteAuctionByIdWithValidIdShouldSuccess() {
        Integer id = 1;

        Auction auction = new Auction("title", "desc", Instant.now().plus(1, ChronoUnit.DAYS),10.0, new User(100),null);

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(100);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(auction));

        assertDoesNotThrow(() -> auctionService.deleteAuctionById(id));
    }


    @Test
    void deleteAuctionByIdWithInvalidIdShouldThrow() {
        Integer id = 190909;

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(100);
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> auctionService.deleteAuctionById(id));
    }

    @Test
    void deleteAuctionByIdWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> auctionService.deleteAuctionById(null));
    }

    @Test
    void deleteAuctionByIdWhileNotAuthenticatedShouldThrow() {
        when(sessionManager.isAuthenticated()).thenReturn(false);
        assertThrows(NotAuthenticatedException.class, () -> auctionService.deleteAuctionById(1));
    }

    @Test
    void deleteAuctionByIdWhileNotAuthorizedShouldThrow() {
        Integer id = 1;

        Auction auction = new Auction("title", "desc", Instant.now().plus(1, ChronoUnit.DAYS),10.0, new User(100),null);

        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(1);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(auction));
        assertThrows(NotAuthorizedException.class, () -> auctionService.deleteAuctionById(1));
    }
}