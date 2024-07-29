package com.hendy.spring_chat_app.controller;

import com.hendy.spring_chat_app.entity.Friend;
import com.hendy.spring_chat_app.model.FriendRequest;
import com.hendy.spring_chat_app.model.PendingFriendRequest;
import com.hendy.spring_chat_app.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FriendController {

    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private FriendService friendService;

    @GetMapping("/friendRequests")
    @ResponseBody
    public List<PendingFriendRequest> getPendingFriendRequests(@RequestParam("username") String username) {
        return friendService.getPendingRequests(username);
    }

    @MessageMapping("/friendRequest")
    public void sendToSpecificUser(@Payload FriendRequest request) {
        try {
            Friend friend = friendService.sendFriendRequest(request);
            String message = String.format("New friend request from %s to %s. Request ID: %d",
                    request.getFrom(), request.getTo(), friend.getId());
            simpMessagingTemplate.convertAndSendToUser(request.getTo(), "/specific", message);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error processing friend request", e);
            simpMessagingTemplate.convertAndSendToUser(request.getFrom(), "/specific",
                    "Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error processing friend request", e);
            simpMessagingTemplate.convertAndSendToUser(request.getFrom(), "/specific",
                    "Error: Unexpected error occurred.");
        }
    }
}