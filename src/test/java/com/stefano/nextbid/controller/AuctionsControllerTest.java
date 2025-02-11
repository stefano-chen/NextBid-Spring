package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidAuctionDataException;
import com.stefano.nextbid.service.AuctionMapper;
import com.stefano.nextbid.service.AuctionService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuctionsController.class)
class AuctionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuctionService auctionService;


    @Test
    void createAuctionWithEmptyDataShouldFail() throws Exception {
        String body = "{}";
        this.mockMvc.perform(post("/api/auctions").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    void createAuctionWithMissingDataShouldFail() throws Exception {
        String body = "{\"title\":\"auction\"}";
        this.mockMvc.perform(post("/api/auctions").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    void createAuctionWithValidDataShouldSuccess() throws Exception {
        String body = "{" +
                "\"title\":\"auction\"," +
                "\"description\":\"description\"," +
                "\"initialBid\": 10.0," +
                "\"dueDate\":\"3026-07-20T13:05:30.00Z\"" +
                "}";
        CreateAuctionBody auctionBody = new CreateAuctionBody("auction", "description", 10.0, Instant.parse("3026-07-20T13:05:30.00Z"));
        AuctionDTO createdAuction = new AuctionDTO(1,"auction", "description", 10.0,  Instant.parse("3026-07-20T13:05:30.00Z"), Instant.now(), new User(1), null);
        when(auctionService.createAuction(auctionBody)).thenReturn(createdAuction);
        this.mockMvc.perform(post("/api/auctions").content(body).contentType("application/json")).andDo(print()).andExpect(status().isOk());
    }
}