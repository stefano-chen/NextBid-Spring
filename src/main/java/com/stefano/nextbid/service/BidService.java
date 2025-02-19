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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final BidMapper bidMapper;
    private final SessionManager sessionManager;

    public BidService(BidRepository bidRepository, AuctionRepository auctionRepository, BidMapper bidMapper, SessionManager sessionManager) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.bidMapper = bidMapper;
        this.sessionManager = sessionManager;
    }

    public List<BidDTO> findByAuctionId(Integer auctionId) throws IllegalArgumentException {
        if (auctionId == null)
            throw new IllegalArgumentException();
        return bidRepository.findAllByAuctionOrderByAmountDesc(new Auction(auctionId)).stream()
                .map(bidMapper::mapToBidDTO).toList();
    }

    @Transactional
    public void createBid(Integer auctionId, CreateBidBody body) throws IllegalArgumentException, NotAuthenticatedException, AuctionClosedException, AmountTooLowException, InvalidIdException {
        if (auctionId == null || body == null)
            throw new IllegalArgumentException();
        if (!sessionManager.isAuthenticated())
            throw new NotAuthenticatedException();
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(InvalidIdException::new);
        if (Instant.now().isAfter(auction.getDueDate()))
            throw new AuctionClosedException();
        List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(new Auction(auctionId));
        double lastBid = allBids.isEmpty() ? auction.getInitialBid() : allBids.getFirst().getAmount();
        if (lastBid >= body.amount())
            throw new AmountTooLowException();
        User authUser = new User(sessionManager.getUserId());
        auction.setWinner(authUser);
        auctionRepository.save(auction);
        bidRepository.save(new Bid(authUser, new Auction(auctionId), body.amount()));
    }

    public BidDTO getBidDetail(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        Bid bid = bidRepository.findById(id).orElseThrow(InvalidIdException::new);
        return bidMapper.mapToBidDTO(bid);
    }
}
