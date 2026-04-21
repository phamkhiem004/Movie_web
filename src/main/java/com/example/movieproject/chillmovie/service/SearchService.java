package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.MovieSearchItemDto;
import com.example.movieproject.chillmovie.DTO.MovieSearchResponseDto;
import com.example.movieproject.chillmovie.DTO.SearchSuggestionsDto;
import com.example.movieproject.chillmovie.projection.MovieSearchRow;
import com.example.movieproject.chillmovie.respository.MovieSearchRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final MovieSearchRepository movieSearchRepository;

    public SearchService(MovieSearchRepository movieSearchRepository) {
        this.movieSearchRepository = movieSearchRepository;
    }
    // 1. HÀM GỢI Ý TÌM KIẾM NHANH (AUTOCOMPLETE)
    // Trả về DTO chứa danh sách tên phim và tên diễn viên gợi ý
    public SearchSuggestionsDto getSuggestions(String q) {
        String keyword = q == null ? "" : q.trim();

        // Nếu user chưa gõ gì thì trả về list rỗng
        if (keyword.isEmpty()) {
            return new SearchSuggestionsDto(Collections.emptyList(), Collections.emptyList());
        }

        List<String> movieTitles = movieSearchRepository.suggestMovieTitles(keyword);
        List<String> actors = movieSearchRepository.suggestActors(keyword);

        return new SearchSuggestionsDto(movieTitles, actors);
    }

    // 2. HÀM TÌM KIẾM PHIM CHÍNH THỨC
    public MovieSearchResponseDto searchMovies(String q, List<Integer> genreIds, int page, int size) {
        String keyword = q == null ? "" : q.trim();

        // Xử lý danh sách ID thể loại an toàn
        List<Integer> safeGenreIds = (genreIds == null) ? Collections.emptyList() : genreIds;
        boolean genreFilterOff = safeGenreIds.isEmpty();

        // Mẹo tránh lỗi SQL IN () khi danh sách rỗng: truyền một list có phần tử giả (-1)
        List<Integer> queryGenreIds = genreFilterOff ? List.of(-1) : safeGenreIds;

        // Thiết lập phân trang (Giới hạn tối đa 50 item mỗi trang)
        PageRequest pageable = PageRequest.of(page, Math.min(size, 50));

        // Gọi DB để lấy dữ liệu thô và tổng số lượng
        List<MovieSearchRow> rows = movieSearchRepository.searchMovies(keyword, genreFilterOff, queryGenreIds, pageable);
        long total = movieSearchRepository.countMovies(keyword, genreFilterOff, queryGenreIds);

        // Biến đổi dữ liệu thô (Row) thành DTO trả về cho Frontend
        List<MovieSearchItemDto> items = rows.stream().map(r -> {
            MovieSearchItemDto dto = new MovieSearchItemDto();
            dto.setMovieId(r.getMovieId());
            dto.setTitle(r.getTitle());
            dto.setDuration(r.getDuration());
            dto.setPosterUrl(r.getPosterUrl());

            // Dùng hàm phụ trợ tách chuỗi "Tom Cruise||Henry Cavill" thành List
            dto.setActors(splitConcat(r.getActors()));
            dto.setGenres(splitConcat(r.getGenres()));
            return dto;
        }).collect(Collectors.toList());

        return new MovieSearchResponseDto(items, page, size, total);
    }

    // 3. HÀM PHỤ TRỢ: TÁCH CHUỖI GROUP_CONCAT
    private List<String> splitConcat(String value) {
        if (value == null || value.isBlank()) return Collections.emptyList();

        return Arrays.stream(value.split("\\|\\|"))
                .filter(s -> !s.isBlank())
                .distinct() // Loại bỏ phần tử trùng lặp (nếu có)
                .collect(Collectors.toList());
    }


}
