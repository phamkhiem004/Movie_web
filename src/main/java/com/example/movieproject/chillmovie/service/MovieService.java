package com.example.movieproject.chillmovie.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.DTO.WatchHistoryDTO;
import com.example.movieproject.chillmovie.entity.*;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import com.example.movieproject.chillmovie.respository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieActorRepository movieActorRepository;
    private final UserRepository userRepository;
    private final WatchHistoryRepository watchHistoryRepository;

    public MovieService(MovieRepository movieRepository, MovieGenreRepository movieGenreRepository, MovieActorRepository movieActorRepository, UserRepository userRepository, WatchHistoryRepository watchHistoryRepository) {
        this.movieRepository = movieRepository;
        this.movieGenreRepository = movieGenreRepository;
        this.movieActorRepository = movieActorRepository;
        this.userRepository = userRepository;
        this.watchHistoryRepository = watchHistoryRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    public Movie getMovieByID(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);// do findById trả về Optional để tránh lỗi
        // NullPointerException khi không tìm thấy movie
        // với id đó
        // nếu movie tồn tại, trả về movie đó
        return movieOptional.orElse(null);
    }

    //Danh sách full phim kèm lịch sử bản thân
    public List<MovieProjection> getALlMovieWithHistory(Long userId) {
        return movieRepository.findAllMoviesWithHistory(userId);
    }

    //Danh sách phim yêu thích
    public List<Movie> getAllFavouriteMovies(Long userId) {
        return movieRepository.findFavoriteByUserId(userId);
    }

    //Lịch sử xem phim
    public List<WatchHistoryProjection> getAllHistoryMovies(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);
        return movieRepository.findHistory(userId, pageable);
    }

    public Movie createMovie(CreateMovieRequest request) {

        // 1. Lưu movie trước
        Movie movie = new Movie();
        movie.setTitle(request.title);
        movie.setDuration(request.duration);
        movie.setLanguage(request.language);
        movie.setCountry(request.country);
        movie.setAgeLimit(request.ageLimit);
        movie.setDescription(request.description);
        movie.setTrailerUrl(request.trailerUrl);
        movie.setPosterUrl(request.posterUrl);

        movie = movieRepository.save(movie);

        // 2. Lưu genres (movie_genres)
        for (Integer genreId : request.genreIds) {
            MovieGenre mg = new MovieGenre();

            MovieGenreId id = new MovieGenreId();
            id.setMovieId(movie.getId());
            id.setGenreId(genreId);

            mg.setId(id);
            mg.setMovie(movie);

            movieGenreRepository.save(mg);
        }

        // 3. Lưu actors (movie_actors)
        for (CreateMovieRequest.ActorRequest a : request.actors) {
            MovieActor ma = new MovieActor();

            MovieActorId id = new MovieActorId();
            id.setMovieId(movie.getId());
            id.setActorId(a.actorId);

            ma.setId(id);
            ma.setMovie(movie);
            ma.setRoleName(a.roleName);

            movieActorRepository.save(ma);
        }

        return movie;
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Transactional
    public Movie updateMovie(Long movieId, UpdateMovieRequest request) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));


        movie.setTitle(request.title);
        movie.setDescription(request.description);
        movie.setTrailerUrl(request.trailerUrl);
        movie.setPosterUrl(request.posterUrl);
        movie.setDuration(request.duration);
        movie.setLanguage(request.language);
        movie.setCountry(request.country);
        movie.setAgeLimit(request.ageLimit);

        movieRepository.save(movie);


        movieGenreRepository.deleteByMovieId(movieId);

        for (Integer genreId : request.genreIds) {
            MovieGenre mg = new MovieGenre();

            MovieGenreId id = new MovieGenreId();
            id.setMovieId(movieId);
            id.setGenreId(genreId);

            mg.setId(id);
            mg.setMovie(movie);

            movieGenreRepository.save(mg);
        }

        movieActorRepository.deleteByMovieId(movieId);

        for (UpdateMovieRequest.ActorRequest a : request.actors) {
            MovieActor ma = new MovieActor();

            MovieActorId id = new MovieActorId();
            id.setMovieId(movieId);
            id.setActorId(a.actorId);

            ma.setId(id);
            ma.setMovie(movie);
            ma.setRoleName(a.roleName);

            movieActorRepository.save(ma);
        }

        return movie;
    }

    // Lấy thông tin phim trả về với cả lịch sử người xem
    public MovieDTO getMovieDetail(Long movieId, Long userId) {
        Movie movie = movieRepository.findMovieDetail(movieId);
        List<WatchHistory> histories = watchHistoryRepository.findWatchHistory(movieId, userId);

        if (movie == null) {
            return null;
        }

        MovieDTO dto = new MovieDTO();

        // ===== map basic info =====
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDuration(movie.getDuration());
        dto.setCountry(movie.getCountry());
        dto.setLanguage(movie.getLanguage());
        dto.setAgeLimit(movie.getAgeLimit());
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setPosterUrl(movie.getPosterUrl());

        // ===== map actors =====
        List<String> actors = movie.getMovieActors()
                .stream()
                .map(ma -> ma.getActor().getName()) // nhớ đúng field name
                .toList();

        dto.setActors(actors);

        // ===== map genres =====
        List<String> genres = movie.getMovieGenres()
                .stream()
                .map(mg -> mg.getGenre().getName())
                .toList();

        dto.setGenres(genres);

        // ===== map continueWatching =====
        WatchHistory latest = histories.stream()
                .filter(h -> h.getLastWatchedAt() != null) // tránh null
                .max((a, b) -> a.getLastWatchedAt().compareTo(b.getLastWatchedAt()))
                .orElse(null);

        if (latest != null) {
            WatchHistoryDTO historyDTO = new WatchHistoryDTO();

            // check episode null (phim lẻ)
            if (latest.getEpisode() != null) {
                historyDTO.setEpisodeId(latest.getEpisode().getId());
            }

            historyDTO.setWatchedSeconds(latest.getWatchedSeconds());
            historyDTO.setCompleted(latest.getCompleted());
            historyDTO.setLastWatchedAt(latest.getLastWatchedAt());

            dto.setContinueWatching(historyDTO);
        }

        return dto;
    }

    public List<MovieDTO> findMovieByActorId(Long actorId) {
        List<Movie> movies = movieRepository.findMoviesByActorId(actorId);

        return movies.stream().map(m -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(m.getId());
            dto.setTitle(m.getTitle());
            dto.setDescription(m.getDescription());
            dto.setDuration(m.getDuration());
            dto.setCountry(m.getCountry());
            dto.setLanguage(m.getLanguage());
            dto.setAgeLimit(m.getAgeLimit());
            dto.setTrailerUrl(m.getTrailerUrl());
            dto.setPosterUrl(m.getPosterUrl());
            return dto;
        }).toList();
    }

    public List<Movie> findMovieByGenreId(Long genreId) {
        return movieRepository.findMoviesByGenreId(genreId);
    }

}
