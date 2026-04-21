package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.projection.MovieSearchRow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieSearchRepository extends JpaRepository<Movie, Long> {
    // 1. TÌM KIẾM PHIM CHÍNH
    @Query(value = """
            SELECT
                m.id AS movieId,
                m.title AS title,
                m.duration AS duration,
                m.poster_url AS posterUrl,
                GROUP_CONCAT(DISTINCT a.name SEPARATOR '||') AS actors,
                GROUP_CONCAT(DISTINCT g.name SEPARATOR '||') AS genres
            FROM movies m
            LEFT JOIN movie_actors ma ON m.id = ma.movie_id
            LEFT JOIN actors a ON ma.actor_id = a.id
            LEFT JOIN movie_genres mg ON m.id = mg.movie_id
            LEFT JOIN genres g ON mg.genre_id = g.id
            WHERE
                (
                    COALESCE(:q, '') = ''
                    OR LOWER(m.title) LIKE CONCAT('%', LOWER(:q), '%')
                    OR LOWER(a.name) LIKE CONCAT('%', LOWER(:q), '%')
                )
                AND
                (:genreFilterOff = true OR mg.genre_id IN (:genreIds))
            GROUP BY m.id, m.title, m.duration, m.poster_url
            ORDER BY m.created_at DESC
            """, nativeQuery = true)
    List<MovieSearchRow> searchMovies(
            @Param("q") String q,
            @Param("genreFilterOff") boolean genreFilterOff,
            @Param("genreIds") List<Integer> genreIds,
            Pageable pageable
    );

    // 2. ĐẾM SỐ LƯỢNG PHIM ĐỂ PHÂN TRANG
    @Query(value = """
            SELECT COUNT(DISTINCT m.id)
            FROM movies m
            LEFT JOIN movie_actors ma ON m.id = ma.movie_id
            LEFT JOIN actors a ON ma.actor_id = a.id
            LEFT JOIN movie_genres mg ON m.id = mg.movie_id
            WHERE
                (
                    COALESCE(:q, '') = ''
                    OR LOWER(m.title) LIKE CONCAT('%', LOWER(:q), '%')
                    OR LOWER(a.name) LIKE CONCAT('%', LOWER(:q), '%')
                )
                AND
                (:genreFilterOff = true OR mg.genre_id IN (:genreIds))
            """, nativeQuery = true)
    long countMovies(
            @Param("q") String q,
            @Param("genreFilterOff") boolean genreFilterOff,
            @Param("genreIds") List<Integer> genreIds
    );

    // 3. GỢI Ý TÊN PHIM
    @Query(value = """
            SELECT m.title
            FROM movies m
            WHERE LOWER(m.title) LIKE CONCAT('%', LOWER(:q), '%')
            GROUP BY m.title
            LIMIT 8
            """, nativeQuery = true)
    List<String> suggestMovieTitles(@Param("q") String q);

    // 4. GỢI Ý DIỄN VIÊN
    @Query(value = """
            SELECT DISTINCT a.name
            FROM actors a
            WHERE LOWER(a.name) LIKE CONCAT('%', LOWER(:q), '%')
            ORDER BY a.name ASC
            LIMIT 8
            """, nativeQuery = true)
    List<String> suggestActors(@Param("q") String q);
}
