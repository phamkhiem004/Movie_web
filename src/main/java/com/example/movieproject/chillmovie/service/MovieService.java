package com.example.movieproject.chillmovie.service;

import java.util.Comparator;
import java.util.List;

import com.example.movieproject.chillmovie.DTO.*;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.entity.*;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import com.example.movieproject.chillmovie.respository.*;
import com.example.movieproject.chillmovie.DTO.CustomUserDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieActorRepository movieActorRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final EpisodeRepository episodeRepository;

    public MovieService(MovieRepository movieRepository, MovieGenreRepository movieGenreRepository, MovieActorRepository movieActorRepository, UserRepository userRepository, GenreRepository genreRepository, ActorRepository actorRepository, WatchHistoryRepository watchHistoryRepository, EpisodeRepository episodeRepository) {
        this.movieRepository = movieRepository;
        this.movieGenreRepository = movieGenreRepository;
        this.movieActorRepository = movieActorRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.watchHistoryRepository = watchHistoryRepository;
        this.episodeRepository = episodeRepository;
    }

    //Chỉ hiển thị cho user xem
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
            dto.setReleaseDate(m.getReleaseDate());
            return dto;
        }).toList();
    }

    //Hiển thị ở dashboard admin
    public List<MovieDTO> getAdminMovies() {
        List<Movie> movies = movieRepository.findByIsDeletedFalse();

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
            dto.setIsDeleted(m.getIsDeleted());
            dto.setPosterUrl(m.getPosterUrl());
            dto.setReleaseDate(m.getReleaseDate());
            return dto;
        }).toList();
    }


    //Lịch sử xem phim
    public List<WatchHistoryProjection> getAllHistoryMovies(CustomUserDetails user) {
        Pageable pageable = PageRequest.of(0, 5);
        if (user == null || user.getId() == null) {
            throw new AccessDeniedException("Access denied");
        }
        return movieRepository.findHistory(user.getId(), pageable);
    }

    @Transactional
    public MovieDTO createMovie(CreateMovieRequest request) {

        // 1. Lưu movie trước
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setCountry(request.getCountry());
        movie.setAgeLimit(request.getAgeLimit());
        movie.setDescription(request.getDescription());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setType(request.getType());
        movie.setStatus(request.getMovieStatus());
        movie.setIsDeleted(false);

        movie = movieRepository.save(movie);

        // 2. Lưu genres (movie_genres)
        for (Integer genreId : request.getGenreIds()) {
            Genre genre = genreRepository.findByGenreIdAndIsDeletedFalse(genreId)
                    .orElseThrow(() -> new RuntimeException("Genre not found or not valid with ID " + genreId));

            MovieGenre mg = new MovieGenre();

            MovieGenreId id = new MovieGenreId();
            id.setMovieId(movie.getId());
            id.setGenreId(genreId);

            mg.setId(id);
            mg.setMovie(movie);
            mg.setGenre(genre);

            MovieGenre savedMg = movieGenreRepository.save(mg);

            movie.getMovieGenres().add(savedMg);
        }

        // 3. Lưu actors (movie_actors)
        for (CreateMovieRequest.ActorRequest a : request.getActors()) {
            Actor actor = actorRepository.findByIdAndIsDeletedFalse(a.actorId)
                    .orElseThrow(() -> new RuntimeException("Actor not found or not valid ID " + a.actorId));
            MovieActor ma = new MovieActor();

            MovieActorId id = new MovieActorId();
            id.setMovieId(movie.getId());
            id.setActorId(a.actorId);

            ma.setId(id);
            ma.setMovie(movie);
            ma.setActor(actor);
            ma.setRoleName(a.roleName);


            MovieActor savedMa = movieActorRepository.save(ma);

            movie.getMovieActors().add(savedMa);
        }
        if (request.getType() == MovieType.SERIES && request.getEpisodes() != null) {

            for (EpisodeRequest e : request.getEpisodes()) {

                Episode episode = new Episode();
                episode.setMovie(movie);
                episode.setEpisodeNumber(e.getEpisodeNumber());
                episode.setTitle(e.getTitle());
                episode.setVideoUrl(e.getVideoUrl());
                episode.setDuration(e.getDuration());

                Episode savedMe = episodeRepository.save(episode);
                movie.getEpisodes().add(savedMe);
            }
        }

        Movie savedMovie = movieRepository.findMovieDetail(movie.getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return convertToMovieDTO(savedMovie);
    }



    @Transactional
    public MovieDTO updateMovie(Long movieId, UpdateMovieRequest request) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));


        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setCountry(request.getCountry());
        movie.setAgeLimit(request.getAgeLimit());
        movie.setType(request.getType());
        movie.setStatus(request.getMovieStatus());
        movie.setIsDeleted(request.getIsDeleted());

        movieRepository.save(movie);


        movieGenreRepository.deleteByMovieId(movieId);
        movieGenreRepository.flush();

        for (Integer genreId : request.getGenreIds()) {
            Genre genre = genreRepository.findByGenreIdAndIsDeletedFalse(genreId)
                    .orElseThrow(() -> new RuntimeException("Genre not found or not valid with ID " + genreId));
            MovieGenre mg = new MovieGenre();

            MovieGenreId id = new MovieGenreId();
            id.setMovieId(movieId);
            id.setGenreId(genreId);

            mg.setId(id);

            mg.setMovie(movie);
            mg.setGenre(genre);

            movieGenreRepository.save(mg);
        }

        movieActorRepository.deleteByMovieId(movieId);
        movieActorRepository.flush();


        for (UpdateMovieRequest.ActorRequest a : request.getActors()) {
            Actor actor = actorRepository.findByIdAndIsDeletedFalse(a.actorId)
                    .orElseThrow(() -> new RuntimeException("Actor not found or not valid ID " + a.actorId));

            MovieActor ma = new MovieActor();

            MovieActorId id = new MovieActorId();
            id.setMovieId(movieId);
            id.setActorId(a.actorId);

            ma.setId(id);

            ma.setMovie(movie);
            ma.setActor(actor);

            ma.setRoleName(a.roleName);

            movieActorRepository.save(ma);
        }
        if (movie.getType() == MovieType.SERIES) {

            // 1. Xóa hết episode cũ
            episodeRepository.deleteByMovieId(movieId);
            episodeRepository.flush();

            // 2. Insert lại episode mới
            if (request.getEpisodes() != null) {
                for (EpisodeRequest e : request.getEpisodes()) {

                    Episode episode = new Episode();
                    episode.setMovie(movie);
                    episode.setEpisodeNumber(e.getEpisodeNumber());
                    episode.setTitle(e.getTitle());
                    episode.setVideoUrl(e.getVideoUrl());
                    episode.setDuration(e.getDuration());

                    episodeRepository.save(episode);
                }
            }
        }


        return convertToMovieDTO(movie);
    }

    // Lấy thông tin phim trả về với cả lịch sử người xem
    public MovieDTO getMovieDetail(Long movieId, CustomUserDetails user) {


        Movie movie = movieRepository.findMovieDetail(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        MovieDTO dto = new MovieDTO();

        // ===== map basic info =====
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDuration(movie.getDuration());
        dto.setCountry(movie.getCountry());
        dto.setLanguage(movie.getLanguage());
        dto.setAgeLimit(movie.getAgeLimit());
        dto.setType(movie.getType());
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setPosterUrl(movie.getPosterUrl());

        // ===== actors =====
        dto.setActors(
                movie.getMovieActors()
                        .stream()
                        .filter(ma -> !ma.getActor().getIsDeleted())
                        .map(ma -> ma.getActor().getName())
                        .toList()
        );

        // ===== genres =====
        dto.setGenres(
                movie.getMovieGenres()
                        .stream()
                        .filter(ma -> !ma.getGenre().getIsDeleted())
                        .map(mg -> mg.getGenre().getName())
                        .toList()
        );

        // ===== episodes =====
        if (movie.getType() == MovieType.SERIES) {
            dto.setEpisodes(
                    movie.getEpisodes()
                            .stream()
                            .filter(e -> !e.getIsDeleted())
                            .map(e -> {
                                EpisodeDTO ep = new EpisodeDTO();
                                ep.setEpisodeNumber(e.getEpisodeNumber());
                                ep.setTitle(e.getTitle());
                                ep.setVideoUrl(e.getVideoUrl());
                                ep.setDuration(e.getDuration());
                                return ep;
                            })
                            .toList()
            );
        }

        // ===== watch history (CHỈ KHI LOGIN) =====
        if (user != null) {

            List<WatchHistory> histories =
                    watchHistoryRepository.findWatchHistory(movieId, user.getId());

            WatchHistory latest = histories.stream()
                    .filter(h -> h.getLastWatchedAt() != null) // tránh null
                    .max(Comparator.comparing(WatchHistory::getLastWatchedAt))
                    .orElse(null);

            if (latest != null) {
                WatchHistoryDTO historyDTO = new WatchHistoryDTO();

                if (latest.getEpisode() != null) {
                    historyDTO.setEpisodeId(latest.getEpisode().getId());
                }

                historyDTO.setWatchedSeconds(latest.getWatchedSeconds());
                historyDTO.setCompleted(latest.getCompleted());
                historyDTO.setLastWatchedAt(latest.getLastWatchedAt());

                dto.setContinueWatching(historyDTO);
            }
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

    public List<MovieDTO> find5RecentMovies(Long id, int page, int size) {
        PageRequest pageable = PageRequest.of(page, Math.min(size, 50));
        List<Movie> movies = movieRepository.findRecentMovies(id, pageable);
        return movies.stream().map(map -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(map.getId());
            dto.setTitle(map.getTitle());
            dto.setDescription(map.getDescription());
            dto.setDuration(map.getDuration());
            return dto;

        }).toList();
    }

    public MovieDTO convertToMovieDTO(Movie movie) {

        MovieDTO res = new MovieDTO();

        res.setId(movie.getId());
        res.setTitle(movie.getTitle());
        res.setDescription(movie.getDescription());
        res.setTrailerUrl(movie.getTrailerUrl());
        res.setPosterUrl(movie.getPosterUrl());
        res.setDuration(movie.getDuration());
        res.setReleaseDate(movie.getReleaseDate());
        res.setCountry(movie.getCountry());
        res.setLanguage(movie.getLanguage());
        res.setAgeLimit(movie.getAgeLimit());
        res.setType(movie.getType());

        // genres
        res.setGenres(movie.getMovieGenres()
                .stream()
                .map(mg -> mg.getGenre().getName())
                .toList());

        // actors
        res.setActors(movie.getMovieActors()
                .stream()
                .map(ma -> ma.getActor().getName()) // nhớ đúng field name
                .toList());

        // episodes
        res.setEpisodes(movie.getEpisodes()
                .stream()
                .map(e -> {

                    EpisodeDTO er = new EpisodeDTO();

                    er.setEpisodeNumber(e.getEpisodeNumber());
                    er.setTitle(e.getTitle());
                    er.setVideoUrl(e.getVideoUrl());
                    er.setDuration(e.getDuration());

                    return er;
                })
                .toList());

        return res;
    }


}
