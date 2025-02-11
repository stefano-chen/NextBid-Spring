package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.repo.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionService {

    private final AuctionMapper auctionMapper;

    private final AuctionRepository auctionRepository;

    private final SessionManager sessionManager;

    @Autowired
    public AuctionService(AuctionMapper auctionMapper, AuctionRepository auctionRepository, SessionManager sessionManager) {
        this.auctionMapper = auctionMapper;
        this.auctionRepository = auctionRepository;
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
}
