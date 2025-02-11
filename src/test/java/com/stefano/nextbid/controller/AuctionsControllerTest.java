package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidAuctionDataException;
import com.stefano.nextbid.service.AuctionMapper;
import com.stefano.nextbid.service.AuctionService;
import com.stefano.nextbid.service.AuthService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuctionsController.class)
class AuctionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuctionService auctionService;
    @Autowired
    private AuthService authService;


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

    @Test
    void getAllAuctionsWhenThereAreNoAuctionsShouldReturnEmptyList() throws Exception {
        assertTrue(this.mockMvc.perform(get("/api/auctions")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("[]"));
    }

    @Test
    void getAllAuctionsWhenThereAreAuctionsShouldReturnList() throws Exception {

        List<AuctionDTO> auctions = List.of(new AuctionDTO(1,"title1", "description1", 10.0, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), new User(1), null),
                new AuctionDTO(2,"title2", "description2", 10.0, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), new User(2), null));

        when(auctionService.getAllAuctions("")).thenReturn(auctions);

        this.mockMvc.perform(get("/api/auctions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"));
    }

    @Test
    void getAllAuctionsWhenThereAreNoMatchShouldReturnEmptyList() throws Exception {

        List<AuctionDTO> auctions = List.of(new AuctionDTO(1,"title1", "description1", 10.0, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), new User(1), null),
                new AuctionDTO(2,"title2", "description2", 10.0, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), new User(2), null));

        when(auctionService.getAllAuctions("title3")).thenReturn(List.of());

        assertTrue(this.mockMvc.perform(get("/api/auctions")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("[]"));
    }

    @Test
    void getAllAuctionsWhenThereAreMatchShouldReturnList() throws Exception {

        List<AuctionDTO> auctions = List.of(new AuctionDTO(1,"title1", "description1", 10.0, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), new User(1), null),
                new AuctionDTO(2,"title2", "description2", 10.0, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), new User(2), null));

        when(auctionService.getAllAuctions("title1")).thenReturn(auctions);

        this.mockMvc.perform(get("/api/auctions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }
}