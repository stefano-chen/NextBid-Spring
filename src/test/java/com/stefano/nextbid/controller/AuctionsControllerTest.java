package com.stefano.nextbid.controller;

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
        CreateAuctionBody auctionBody = new CreateAuctionBody("", "", 0.0, null);
        when(auctionService.createAuction(auctionBody)).thenThrow(InvalidAuctionException.class);
        this.mockMvc.perform(post("/api/auctions").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void createAuctionWithMissingDataShouldFail() {
        String body = "{\"title\":\"auction\"}";
        CreateAuctionBody auctionBody = new CreateAuctionBody("auction", "", 0.0, null);
        when(auctionService.createAuction(any())).thenThrow(InvalidAuctionException.class);
        this.mockMvc.perform(post("/api/auctions").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void createAuctionWithValidDataShouldSuccess() {
        String body = "{" +
                "\"title\":\"auction\"," +
                "\"description\":\"description\"," +
                "\"initialBid\": 10.0," +
                "\"dueDate\":\"2026-07-20T13:05:30\"," +
                "}";
        CreateAuctionBody auctionBody = new CreateAuctionBody("auction", "description", 10.0, Instant.parse("2026-07-20T13:05:30"));
        when(auctionService.createAuction(auctionBody)).thenReturn(auctionBody);
        this.mockMvc.perform(post("/api/auctions").content(body).contentType("application/json")).andDo(print()).andExpect(status().isOk());
    }
}