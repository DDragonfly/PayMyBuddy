package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.ConnectionRequest;
import com.openclassrooms.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public List<FriendResponse> list(@PathVariable Integer userId) {
        return userService.listFriends(userId)
                .stream()
                .map(u -> new FriendResponse(u.getUserId(), u.getUsername(), u.getEmail()))
                .toList();
    }

    public record FriendResponse(Integer userId, String username, String email) {}

    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody ConnectionRequest request) {
        userService.addConnection(request.ownerId(), request.friendId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
