package com.example.movieproject.chillmovie.service;

import java.util.List;
import java.util.Optional;

import com.example.movieproject.chillmovie.DTO.*;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.entity.*;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import com.example.movieproject.chillmovie.respository.*;
import org.intellij.lang.annotations.Language;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieActorRepository movieActorRepository;
    private final UserRepository userRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final EpisodeRepository episodeRepository;

    public MovieService(MovieRepository movieRepository, MovieGenreRepository movieGenreRepository, MovieActorRepository movieActorRepository, UserRepository userRepository, WatchHistoryRepository watchHistoryRepository, EpisodeRepository episodeRepository) {
        this.movieRepository = movieRepository;
        this.movieGenreRepository = movieGenreRepository;
        this.movieActorRepository = movieActorRepository;
        this.userRepository = userRepository;
        this.watchHistoryRepository = watchHistoryRepository;
        this.episodeRepository = episodeRepository;
    }

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

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


    public MovieDTO getMovieByID(Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
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
        return dto;
    }

    //Danh sách full phim kèm lịch sử bản thân
    public List<MovieProjection> getALlMovieWithHistory(Long userId) {
        return movieRepository.findAllMoviesWithHistory(userId);
    }


    //Lịch sử xem phim
    public List<WatchHistoryProjection> getAllHistoryMovies(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);
        return movieRepository.findHistory(userId, pageable);
    }

    @Transactional
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
        movie.setType(request.type);

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
        if (request.type == MovieType.SERIES && request.episodes != null) {

            for (EpisodeRequest e : request.episodes) {

                Episode episode = new Episode();
                episode.setMovie(movie);
                episode.setEpisodeNumber(e.episodeNumber);
                episode.setTitle(e.title);
                episode.setVideoUrl(e.videoUrl);
                episode.setDuration(e.duration);

                episodeRepository.save(episode);
            }
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
        movie.setType(request.type);

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
        if (movie.getType() == MovieType.SERIES) {

            // 1. Xóa hết episode cũ
            episodeRepository.deleteByMovieId(movieId);

            // 2. Insert lại episode mới
            if (request.episodes != null) {
                for (EpisodeRequest e : request.episodes) {

                    Episode episode = new Episode();
                    episode.setMovie(movie);
                    episode.setEpisodeNumber(e.episodeNumber);
                    episode.setTitle(e.title);
                    episode.setVideoUrl(e.videoUrl);
                    episode.setDuration(e.duration);

                    episodeRepository.save(episode);
                }
            }
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

    public List<MovieDTO> findMovieByGenreId(Long genreId) {
        List<Movie> movies = movieRepository.findMoviesByGenreId(genreId);

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

    public List<MovieDTO> findMovieByType(MovieType type) {
        List<Movie> movies = movieRepository.findByType(type);

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


}
