package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AuctionMapper {
    public Auction mapToAuction(CreateAuctionBody body, User owner){
        if (body == null || owner == null)
            return null;
        return new Auction(body.title(), body.description(), body.dueDate(), body.initialBid(), owner, null);
    }

    public AuctionDTO mapToAuctionDTO(Auction auction){
        if (auction == null)
            return null;
        return new AuctionDTO(auction.getId(), auction.getTitle(), auction.getDescription(),
                auction.getInitialBid(), auction.getDueDate(),auction.getCreatedAt(),
                auction.getOwner(), auction.getWinner());
    }
}
