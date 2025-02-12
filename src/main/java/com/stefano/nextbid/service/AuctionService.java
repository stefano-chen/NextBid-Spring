package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.dto.UpdateAuctionBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.exceptions.NotAuthorizedException;
import com.stefano.nextbid.repo.AuctionRepository;
import com.stefano.nextbid.repo.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuctionService {

    private final AuctionMapper auctionMapper;

    private final AuctionRepository auctionRepository;

    private final BidService bidService;

    private final SessionManager sessionManager;

    @Autowired
    public AuctionService(AuctionMapper auctionMapper, AuctionRepository auctionRepository, BidService bidService, SessionManager sessionManager) {
        this.auctionMapper = auctionMapper;
        this.auctionRepository = auctionRepository;
        this.bidService = bidService;
        this.sessionManager = sessionManager;
    }

    public AuctionDTO createAuction(CreateAuctionBody body) throws IllegalArgumentException, NotAuthenticatedException{
        if (body == null)
            throw new IllegalArgumentException();
        if (!sessionManager.isAuthenticated())
            throw new NotAuthenticatedException();
        User owner = new User(sessionManager.getUserId());
        Auction auction = auctionMapper.mapToAuction(body, owner);
        Auction savedAuction = auctionRepository.save(auction);
        return auctionMapper.mapToAuctionDTO(savedAuction);
    }

    public List<AuctionDTO> getAllAuctions(String q) throws IllegalArgumentException{
        if (q == null)
            throw new IllegalArgumentException();

        if (q.isEmpty()) {
            return auctionRepository.findAll().stream().map(auctionMapper::mapToAuctionDTO).toList();
        }

        return auctionRepository.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q,q)
                .stream().map(auctionMapper::mapToAuctionDTO).toList();
    }

    public AuctionDTO getAuctionById(Integer id) throws InvalidIdException{
        if (id == null)
            throw new InvalidIdException();

        Auction auction = auctionRepository.findById(id).orElseThrow(InvalidIdException::new);

        return auctionMapper.mapToAuctionDTO(auction);
    }

    public void updateAuctionById(Integer id, UpdateAuctionBody body) throws IllegalArgumentException, NotAuthenticatedException, InvalidIdException, NotAuthorizedException{

        if (body == null || id == null)
            throw new IllegalArgumentException();

        if (!sessionManager.isAuthenticated())
            throw new NotAuthenticatedException();

        Auction auction = auctionRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (!sessionManager.getUserId().equals(auction.getOwner().getId()))
            throw new NotAuthorizedException();

        boolean isUpdated = false;

        if (body.title() != null && !body.title().isEmpty()) {
            auction.setTitle(body.title());
            isUpdated = true;
        }

        if (body.description() != null && !body.description().isEmpty()) {
            auction.setDescription(body.description());
            isUpdated = true;
        }

        if (isUpdated)
            auctionRepository.save(auction);
    }

    public void deleteAuctionById(Integer id) throws IllegalArgumentException, NotAuthenticatedException, InvalidIdException, NotAuthorizedException {
        if (id == null)
            throw new IllegalArgumentException();

        if (!sessionManager.isAuthenticated())
            throw new NotAuthenticatedException();

        Auction auction = auctionRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (!sessionManager.getUserId().equals((auction.getOwner().getId())))
            throw new NotAuthorizedException();

        auctionRepository.delete(auction);
    }

    public List<BidDTO> getAuctionBidsById(Integer auctionId) throws IllegalArgumentException, InvalidIdException{
        if (auctionId == null)
            throw new IllegalArgumentException();

        auctionRepository.findById(auctionId).orElseThrow(InvalidIdException::new);

        return this.bidService.findByAuctionId(auctionId);
    }
}
