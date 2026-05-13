package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.CustomUserDetails;
import com.example.movieproject.chillmovie.DTO.WatchHistoryDTO;
import com.example.movieproject.chillmovie.DTO.WatchHistoryRequest;
import com.example.movieproject.chillmovie.service.MovieViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/views")
@Tag(name = "Movie-View")
public class MovieViewController {

    private final MovieViewService movieViewService;

    // Lấy userId từ SecurityContext — dùng chung
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }

    @Operation(summary = "Write data view of user", description = "API write data view of user")
    @PostMapping("/movie/{id}/view")
    public ResponseEntity<?> recordView(@PathVariable Long id,
                                        HttpServletRequest request) {
        movieViewService.recordView(id, getCurrentUserId(),
                request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Update history of user", description = "API update user history")
    @PostMapping("/watch-history/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateWatch(@RequestBody WatchHistoryRequest req) {
        movieViewService.updateWatchHistory(getCurrentUserId(), req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
