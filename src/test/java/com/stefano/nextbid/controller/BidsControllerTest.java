package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.service.BidService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BidsController.class)
class BidsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BidService bidService;

    @Test
    void getAuctionBidsWhenThereAreBidsShouldSuccess() throws Exception {

        List<BidDTO> bids = List.of(
                new BidDTO(1, new User(1), new Auction(1), 10.0, Instant.now()),
                new BidDTO(2, new User(10), new Auction(1), 11.0, Instant.now())
        );

        when(bidService.findByAuctionId(1)).thenReturn(bids);

        assertTrue(this.mockMvc.perform(get("/api/auctions/1/bids")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().length() > 2);
    }

    @Test
    void getAuctionBidsWhenThereAreNoBidsShouldReturnEmptyList() throws Exception {

        when(bidService.findByAuctionId(100)).thenReturn(List.of());

        assertEquals(2, this.mockMvc.perform(get("/api/auctions/1/bids")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().length());
    }

    @Test
    void createBidWithValidDataShouldSuccess() throws Exception {
        String body = "{\"amount\": 10}";
        this.mockMvc.perform(post("/api/auctions/1/bids").content(body).contentType("application/json")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void createBidWithInvalidDataShouldFail() throws Exception {
        String body = "{\"amount\": -10}";
        this.mockMvc.perform(post("/api/auctions/1/bids").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    void getBidDetailWithValidIdShouldSuccess() throws Exception {
        BidDTO response = new BidDTO(1, new User(1), new Auction(1), 10.0, Instant.now());
        when(bidService.getBidDetail(1)).thenReturn(response);

        this.mockMvc.perform(get("/api/bids/1")).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$._id").value(1));
    }

    @Test
    void getBidDetailWithInvalidIdShouldFail() throws Exception {

        doThrow(new InvalidIdException()).when(bidService).getBidDetail(1291);

        this.mockMvc.perform(get("/api/bids/1291")).andDo(print()).andExpect(status().isBadRequest());
    }
}