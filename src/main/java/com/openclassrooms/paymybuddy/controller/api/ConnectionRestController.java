package com.openclassrooms.paymybuddy.controller.api;

import com.openclassrooms.paymybuddy.dto.AddConnectionRequest;
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
public class ConnectionRestController {

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
    public ResponseEntity<Void> add(@Valid @RequestBody AddConnectionRequest request) {
        userService.addConnection(request.ownerId(), request.friendId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
