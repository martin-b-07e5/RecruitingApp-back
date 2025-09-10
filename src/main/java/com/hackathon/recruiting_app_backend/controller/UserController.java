package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.UserRequestUpdateDTO;
import com.hackathon.recruiting_app_backend.dto.UserResponseDTO;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import com.hackathon.recruiting_app_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users") // revisar
@RequiredArgsConstructor
public class UserController {
    // dependency injection
    private final UserService userService;
    private final UserRepository userRepository;

    // PUT {{baseURL1}}/api/users/update/{userId} Endpoint for users to update their own details and admins to update any user.
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'CANDIDATE')")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId, @RequestBody UserRequestUpdateDTO userRequestUpdateDTO, Authentication authentication) {
        try {
            User authenticatedUser = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
            userService.updateUser(userId, authenticatedUser.getId(), authenticatedUser.getRole(), userRequestUpdateDTO);
            return ResponseEntity.ok("✅ User updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // DELETE /api/users/delete-self  -  Endpoint for users to delete their own account and admins to delete any account.
    @DeleteMapping("/delete-self")
    @PreAuthorize("hasAnyRole('RECRUITER', 'CANDIDATE')")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        try {
            User authenticatedUser = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
            userService.deleteUser(authenticatedUser.getId(), authenticatedUser.getRole());
            return ResponseEntity.ok("✅ User: " + authenticatedUser.getId() + " deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // DELETE /api/users/delete/{userId}
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, Authentication authentication) {
        try {
            User authenticatedUser = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
            userService.deleteUser(userId, authenticatedUser.getRole());
            return ResponseEntity.ok("✅ User: " + userId + " Deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // GET /api/users/getAllUsers (admin-only)
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();

            // return ResponseEntity.ok(users);  // to avoid circular reference use dto
            List<UserResponseDTO> userResponseDTOs = users.stream().map(UserResponseDTO::fromEntityUser).toList();
            return ResponseEntity.ok(userResponseDTOs);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
        }
    }

    // GET /api/users/getUserById/{userId}
    @GetMapping("/getUserById/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'CANDIDATE')")
    public ResponseEntity<?> getUserById(@PathVariable Long userId, Authentication authentication) {
        try {
            User authenticatedUser = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
            if (authenticatedUser.getRole() != User.Role.ADMIN && !authenticatedUser.getId().equals(userId)) {
                throw new RuntimeException("You are not authorized to view this user");
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            // return ResponseEntity.ok(user);  // to avoid circular reference use dto
            return ResponseEntity.ok(UserResponseDTO.fromEntityUser(user));


        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
        }
    }

}
