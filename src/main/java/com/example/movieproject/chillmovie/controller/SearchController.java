package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.MovieSearchResponseDto;
import com.example.movieproject.chillmovie.DTO.SearchSuggestionsDto;
import com.example.movieproject.chillmovie.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    private final SearchService movieSearchService;

    public SearchController(SearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    // 1. API GỢI Ý TÌM KIẾM TỰ ĐỘNG
    // Cách gọi: GET /search/suggestions?q=batman
    @GetMapping("/search/suggestions")
    public SearchSuggestionsDto getSuggestions(@RequestParam(defaultValue = "") String q) {
        return movieSearchService.getSuggestions(q);
    }

    // 2. API TÌM KIẾM PHIM VÀ PHÂN TRANG//Search theo tên + thể loại
    // Cách gọi: GET /api/search/movies?q=a&genreIds=1,3,5&page=0&size=20
    @GetMapping("/search/movies")
    public MovieSearchResponseDto searchMovies(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(required = false) String genreIds, // Nhận vào chuỗi "1,2,3"
            @RequestParam(defaultValue = "0") int page,      // Mặc định là trang 0
            @RequestParam(defaultValue = "20") int size      // Mặc định lấy 20 phim 1 trang
    ) {
        // Chuyển đổi chuỗi ID thể loại thành danh sách (List)
        List<Integer> parsedGenreIds = parseGenreIds(genreIds);

        // Gọi xuống Service để xử lý và trả về kết quả
        return movieSearchService.searchMovies(q, parsedGenreIds, page, size);
    }

    // ==========================================
    // CÁC HÀM PHỤ TRỢ (HELPER METHODS)
    // ==========================================

    /**
     * Tách chuỗi ID thể loại từ Frontend gửi lên thành List<Integer>.
     * Ví dụ: Nhận vào "1, 2, 3" -> Trả về danh sách [1, 2, 3]
     */
    private List<Integer> parseGenreIds(String genreIds) {
        List<Integer> result = new ArrayList<>();

        if (genreIds == null || genreIds.isBlank()) {
            return result;
        }

        String[] parts = genreIds.split(",");
        for (String p : parts) {
            try {
                result.add(Integer.parseInt(p.trim()));
            } catch (NumberFormatException ignored) {
                // Bỏ qua nếu có ai đó cố tình truyền chữ vào param genreIds
            }
        }
        return result;
    }
}
