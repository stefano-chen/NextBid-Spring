package com.stefano.nextbid.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Instant dueDate;
    @Column(nullable = false)
    private double initialBid;
    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "winnerId", nullable = true, unique = false)
    private User winner;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Version
    private int version;

    public Auction() {
    }

    public Auction(Integer id) {
        this.id = id;
    }

    public Auction(String title, String description, Instant dueDate, double initialBid, User owner, User winner) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.initialBid = initialBid;
        this.owner = owner;
        this.winner = winner;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public double getInitialBid() {
        return initialBid;
    }

    public void setInitialBid(double initialBid) {
        this.initialBid = initialBid;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }
}
