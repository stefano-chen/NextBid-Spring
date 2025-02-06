package com.stefano.nextbid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NextbidApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void signupEndpointShouldExist() throws Exception {
		this.mockMvc.perform(post("/api/auth/signup")).andExpect(status().is(not(404)));
	}

	@Test
	void signinEndpointShouldExist() throws Exception {
		this.mockMvc.perform(post("/api/auth/signin")).andExpect(status().is(not(404)));
	}

	@Test
	void logoutEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/auth/logout")).andExpect(status().is(not(404)));
	}

	@Test
	void allUsersEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/users?q=query")).andExpect(status().is(not(404)));
	}

	@Test
	void userDetailByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/users/{id}", 1)).andExpect(status().is(not(404)));
	}

	@Test
	void allAuctionsEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/auctions?q=query")).andExpect(status().is(not(404)));
	}

	@Test
	void createAuctionEndpointShouldExist() throws Exception {
		this.mockMvc.perform(post("/api/auctions")).andExpect(status().is(not(404)));
	}

	@Test
	void auctionDetailByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/auctions/{id}", 1)).andExpect(status().is(not(404)));
	}

	@Test
	void updateAuctionDetailByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(put("/api/auctions/{id}",1)).andExpect(status().is(not(404)));
	}

	@Test
	void deleteAuctionByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(delete("/api/auctions/{id}",1)).andExpect(status().is(not(404)));
	}

	@Test
	void allBidsForAuctionByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/auctions/{id}/bids",1)).andExpect(status().is(not(404)));
	}

	@Test
	void createBidForAuctionByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(post("/api/auctions/{id}/bids",1)).andExpect(status().is(not(404)));
	}

	@Test
	void bidDetailByIdEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/bids/{id}",1)).andExpect(status().is(not(404)));
	}

	@Test
	void whoamiEndpointShouldExist() throws Exception {
		this.mockMvc.perform(get("/api/whoami")).andExpect(status().is(not(404)));
	}
}
