package com.stefano.nextbid.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionManager {
    private final HttpSession session;

    @Autowired
    public SessionManager(HttpSession session) {
        this.session = session;
    }

    public void setUserId(Integer id) {
        session.setAttribute("uid", id);
    }

    public boolean isAuthenticated() {
        return session.getAttribute("uid") != null;
    }

    public Integer getUserId() {
        return (Integer) session.getAttribute("uid");
    }

    public void destroy() {
        session.invalidate();
    }
}
