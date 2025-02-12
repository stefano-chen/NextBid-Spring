package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.repo.BidRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;

    private final BidMapper bidMapper;

    public BidService(BidRepository bidRepository, BidMapper bidMapper) {
        this.bidRepository = bidRepository;
        this.bidMapper = bidMapper;
    }

    public List<BidDTO> findByAuctionId(Integer auctionId) throws IllegalArgumentException{
        if (auctionId == null)
            throw new IllegalArgumentException();

        return bidRepository.findAllByAuctionOrderByAmountDesc(new Auction(auctionId)).stream()
                .map(bidMapper::mapToBidDTO).toList();
    }
}
