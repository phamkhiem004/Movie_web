-- ============================================================
--  movie_project  –  Schema v3  (production-ready)
--  Updated: 2026-05-11
-- ============================================================
DROP DATABASE IF EXISTS movie_project;
CREATE DATABASE IF NOT EXISTS movie_project
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE movie_project;

SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- 1. roles
-- ------------------------------------------------------------
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
                       id         INT         PRIMARY KEY AUTO_INCREMENT,
                       role_name  VARCHAR(50) NOT NULL UNIQUE,
                       created_at DATETIME    DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO roles (id, role_name, created_at) VALUES
                                                  (1, 'ADMIN', '2026-03-03 22:57:35'),
                                                  (2, 'USER',  '2026-04-16 22:17:50');

-- ------------------------------------------------------------
-- 2. users
--    FIX: status NOT NULL DEFAULT 'INACTIVE'
--    FIX: thêm FK cho created_by / updated_by (self-referencing)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id          BIGINT       PRIMARY KEY AUTO_INCREMENT,
                       username    VARCHAR(100) NOT NULL UNIQUE,
                       email       VARCHAR(150) NOT NULL UNIQUE,
                       password    VARCHAR(255) NOT NULL,
                       full_name   VARCHAR(150),
                       avatar_url  VARCHAR(255),
                       role_id     INT          NOT NULL,
                       status      ENUM('INACTIVE','ACTIVE','BLOCKED') NOT NULL DEFAULT 'INACTIVE',
                       is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
                       secret_code VARCHAR(255),
                       created_by  BIGINT,
                       updated_by  BIGINT,
                       created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
                       updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                       CONSTRAINT fk_user_role
                           FOREIGN KEY (role_id)    REFERENCES roles(id),
                       CONSTRAINT fk_user_created_by
                           FOREIGN KEY (created_by) REFERENCES users(id),
                       CONSTRAINT fk_user_updated_by
                           FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- UNIQUE đã tạo index trên email — chỉ cần thêm idx riêng cho username
CREATE INDEX idx_user_username ON users(username);

INSERT INTO users (id, username, email, password, full_name, avatar_url,
                   role_id, status, is_deleted, secret_code,
                   created_by, updated_by, created_at, updated_at)
VALUES
    (1,  'admin',    'admin@gmail.com',
     '$2a$10$DowJonesIndexxYxM8M6L2UQe9OQH3iB2g2j9IhW8y4V2Y3W6hW',
     'Administrator', NULL, 1, 'ACTIVE', FALSE, NULL, NULL, NULL,
     '2026-03-03 22:59:26', '2026-04-16 22:33:25'),
    (3,  'abc',      'abc@gmail.com',
     '$2a$10$KmWMNnIVSzIqoOE8A9T.iuZ5Rbow0u2RYDrkSMP89RTYZ1umnO7ha',
     'Abc', NULL, 1, 'ACTIVE', FALSE, NULL, NULL, NULL,
     NULL, '2026-05-07 16:57:08'),
    (19, 'tula5904', 'tula5904@gmail.com',
     '$2a$10$x43ExbRAS26ZKw/N6lThf.bisvYalWlpTLDNa9BLqWaadSDn4Tql6',
     'Khiem', NULL, 2, 'ACTIVE', FALSE, NULL, NULL, NULL,
     '1959-02-28 22:33:41', NULL),
    (21, 'khai',     'khaiquang7508@gmail.com',
     '$2a$10$FAu0eo6noKvx/D.5vshGke/7802/nU0F4q1i6tdtn247Fjfi9.P1q',
     'Khai', NULL, 2, 'ACTIVE', FALSE, NULL, NULL, NULL,
     '2026-05-10 11:29:18', NULL);

-- ------------------------------------------------------------
-- 3. tokens
--    FIX: thêm updated_at
--    FIX: access_token / refresh_token → LONGTEXT (JWT có thể dài)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS tokens;
CREATE TABLE tokens (
                        id            BIGINT    PRIMARY KEY AUTO_INCREMENT,
                        user_id       BIGINT    NOT NULL,
                        access_token  LONGTEXT,
                        refresh_token LONGTEXT,
                        expired       BOOLEAN   NOT NULL DEFAULT FALSE,
                        revoked       BOOLEAN   NOT NULL DEFAULT FALSE,
                        created_at    DATETIME  DEFAULT CURRENT_TIMESTAMP,
                        updated_at    DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT fk_token_user
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_token_user ON tokens(user_id);

-- ------------------------------------------------------------
-- 4. movies
--    FIX: type thêm DEFAULT 'SINGLE' tránh insert fail
--    FIX: thêm idx_movie_status
-- ------------------------------------------------------------
DROP TABLE IF EXISTS movies;
CREATE TABLE movies (
                        id           BIGINT       PRIMARY KEY AUTO_INCREMENT,
                        title        VARCHAR(255) NOT NULL,
                        description  TEXT,
                        trailer_url  VARCHAR(255),
                        poster_url   VARCHAR(255),
                        duration     INT,
                        release_date DATE,
                        country      VARCHAR(100),
                        language     VARCHAR(100),
                        age_limit    INT,
                        status       ENUM('COMING_SOON','NOW_SHOWING','STOPPED') NOT NULL DEFAULT 'NOW_SHOWING',
                        type         ENUM('SINGLE','SERIES')                     NOT NULL DEFAULT 'SINGLE',
                        is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
                        created_by   BIGINT,
                        updated_by   BIGINT,
                        created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
                        updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT fk_movie_created_by
                            FOREIGN KEY (created_by) REFERENCES users(id),
                        CONSTRAINT fk_movie_updated_by
                            FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_movie_title  ON movies(title);
CREATE INDEX idx_movie_status ON movies(status);

INSERT INTO movies (id, title, description, trailer_url, poster_url,
                    duration, release_date, country, language, age_limit,
                    status, type, is_deleted, created_by, updated_by,
                    created_at, updated_at)
VALUES
    (6,  'Breaking Bad',
     'Một giáo viên hóa học bị ung thư quyết định sản xuất ma túy để đảm bảo tương lai tài chính cho gia đình.',
     'https://www.youtube.com/watch?v=HhesaQXLuRY',
     'https://image.tmdb.org/t/p/original/ggFHVNu6YYI5L9pCfOacjizRGt.jpg',
     50,  '2023-05-12', 'USA',     'English',  18, 'NOW_SHOWING', 'SERIES', FALSE, 1,    NULL, '2026-03-03 22:59:33', '2026-05-08 22:57:31'),
    (7,  'The Silent Hill',
     'A hacker uncovers a hidden digital conspiracy.',
     'https://example.com/trailer1', 'https://example.com/poster1',
     115, '2023-05-12', 'England', 'English',  13, 'NOW_SHOWING', 'SERIES', FALSE, 1,    NULL, '2026-03-03 22:59:33', '2026-04-25 08:10:45'),
    (8,  'Midnight in Tokyo',
     'A detective hunts a mysterious serial killer.',
     'https://example.com/trailer3', 'https://example.com/poster3',
     128, '2024-01-20', 'Japan',   'Japanese', 18, 'NOW_SHOWING', 'SERIES', FALSE, 1,    NULL, '2026-03-03 22:59:33', '2026-04-25 08:10:45'),
    (9,  'Ocean of Dreams',
     'Two strangers meet on a life-changing journey.',
     'https://example.com/trailer4', 'https://example.com/poster4',
     98,  '2021-07-18', 'France',  'French',   13, 'NOW_SHOWING', 'SERIES', FALSE, 1,    NULL, '2026-03-03 22:59:33', '2026-04-25 08:10:45'),
    (10, 'Galaxy Reborn',
     'Humanity fights for survival in deep space.',
     'https://example.com/trailer5', 'https://example.com/poster5',
     140, '2024-08-09', 'USA',     'English',  13, 'NOW_SHOWING', 'SERIES', FALSE, 1,    NULL, '2026-03-03 22:59:33', '2026-04-25 08:10:45'),
    (12, 'Example Movie', 'This is an example movie description.',
     'https://www.youtube.com/watch?v=example', 'https://www.example.com/poster.jpg',
     120, '2023-01-01', 'USA',     'English',  13, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-03-05 21:36:01', '2026-04-25 08:10:45'),
    (14, 'Example Movie', 'This is an example movie description.',
     'https://www.youtube.com/watch?v=example', 'https://www.example.com/poster.jpg',
     120, '2023-01-01', 'USA',     'English',  13, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-03-05 21:50:41', '2026-04-25 08:10:45'),
    (16, 'Resident Evil', 'A hacker uncovers a hidden digital conspiracy.',
     'https://example.com/trailer12', 'https://example.com/poster12',
     115, '2023-05-12', 'USA',     'English',  18, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-03-05 22:10:46', '2026-04-25 08:10:45'),
    (17, 'Resident Evil II', 'A hacker uncovers a hidden digital conspiracy.',
     'https://example.com/trailer13', 'https://example.com/poster13',
     139, '2023-05-12', 'USA',     'English',  18, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-03-06 22:18:18', '2026-04-25 08:10:45'),
    (18, 'Resident Evil III', '?',
     'https://example.com/trailer13', 'https://example.com/poster13',
     139, '2023-05-12', 'USA',     'English',  18, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-03-07 21:35:50', '2026-04-25 08:10:45'),
    (20, 'Resident Evil III', '?',
     'https://example.com/trailer13', 'https://example.com/poster13',
     139, '2023-05-12', 'USA',     'English',  18, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-03-10 15:13:54', '2026-04-25 08:10:45'),
    (25, 'Avengers', 'Superhero movie',
     'https://youtube.com/...', 'https://image.com/...',
     139, NULL,         'England', 'English',  18, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-04-18 23:22:59', '2026-04-25 08:10:45'),
    (31, 'Inception',
     'Một tên trộm chuyên xâm nhập giấc mơ được giao nhiệm vụ cấy ý tưởng vào tâm trí mục tiêu.',
     'https://www.youtube.com/watch?v=YoHD9XEInc0',
     'https://image.tmdb.org/t/p/original/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg',
     148, NULL,         'USA',     'English',  13, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-05-09 00:13:35', '2026-05-09 00:13:35'),
    (33, 'Stranger Things',
     'Một nhóm bạn nhỏ khám phá những hiện tượng siêu nhiên tại thị trấn Hawkins.',
     'https://www.youtube.com/watch?v=b9EkMc79ZSU',
     'https://image.tmdb.org/t/p/original/49WJfeN0moxb9IPfGn8AIqMGskD.jpg',
     50,  NULL,         'USA',     'English',  16, 'NOW_SHOWING', 'SERIES', FALSE, NULL, NULL, '2026-05-09 00:19:09', '2026-05-09 00:19:09'),
    (34, 'Shadow of Kyoto',
     'In the near future, a former detective returns to Kyoto after a series of mysterious disappearances.',
     'https://example.com/trailers/shadow-of-kyoto',
     'https://example.com/posters/shadow-of-kyoto.jpg',
     118, NULL,         'Japan',   'Japanese', 18, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-05-09 09:23:03', '2026-05-09 09:23:03'),
    (35, 'Last Train to Berlin',
     'During a political crisis in Europe, a former intelligence officer boards the last night train to Berlin.',
     'https://example.com/trailers/last-train-to-berlin',
     'https://example.com/posters/last-train-to-berlin.jpg',
     132, NULL,         'Germany', 'German',   16, 'NOW_SHOWING', 'SINGLE', FALSE, NULL, NULL, '2026-05-09 09:43:54', '2026-05-09 09:43:54');

-- ------------------------------------------------------------
-- 5. genres
--    FIX: thêm is_deleted để soft-delete
-- ------------------------------------------------------------
DROP TABLE IF EXISTS genres;
CREATE TABLE genres (
                        id         INT          PRIMARY KEY AUTO_INCREMENT,
                        name       VARCHAR(100) NOT NULL UNIQUE,
                        is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;

INSERT INTO genres (id, name) VALUES
                                  (1,'Action'),(2,'Adventure'),(3,'Animation'),(4,'Comedy'),(5,'Crime'),
                                  (6,'Documentary'),(7,'Drama'),(8,'Fantasy'),(9,'Horror'),(10,'Mystery'),
                                  (11,'Romance'),(12,'Sci-Fi'),(13,'Thriller'),(14,'War'),(15,'Western'),
                                  (16,'Musical'),(17,'Family'),(18,'Biography'),(19,'History'),(20,'Sport');

-- ------------------------------------------------------------
-- 6. movie_genres
-- ------------------------------------------------------------
DROP TABLE IF EXISTS movie_genres;
CREATE TABLE movie_genres (
                              movie_id BIGINT NOT NULL,
                              genre_id INT    NOT NULL,
                              PRIMARY KEY (movie_id, genre_id),

                              CONSTRAINT fk_movie_genre_movie
                                  FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                              CONSTRAINT fk_movie_genre_genre
                                  FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO movie_genres (movie_id, genre_id) VALUES
                                                  (10,1),(16,1),(17,1),(18,1),(25,1),(31,1),(33,1),
                                                  (25,2),(31,2),(34,2),(35,2),
                                                  (6,3),(33,3),
                                                  (6,4),(34,4),
                                                  (8,5),(35,5),
                                                  (34,6),
                                                  (9,7),(12,7),(35,7),
                                                  (7,9),(16,9),(17,9),(18,9),
                                                  (8,10),
                                                  (9,11),(14,11),
                                                  (10,12),
                                                  (7,13);

-- ------------------------------------------------------------
-- 7. actors
--    FIX: thêm is_deleted để soft-delete
--    FIX: Tom Holland avatar_url
-- ------------------------------------------------------------
DROP TABLE IF EXISTS actors;
CREATE TABLE actors (
                        id          BIGINT       PRIMARY KEY AUTO_INCREMENT,
                        name        VARCHAR(150) NOT NULL,
                        birth_date  DATE,
                        nationality VARCHAR(100),
                        biography   TEXT,
                        avatar_url  VARCHAR(255),
                        is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

INSERT INTO actors (id, name, birth_date, nationality, biography, avatar_url) VALUES
                                                                                  (1,  'Leonardo DiCaprio', '1974-11-11', 'American',   'Famous actor known for Titanic and Inception.',  'https://example.com/leonardo.jpg'),
                                                                                  (2,  'Emma Watson',       '1990-04-15', 'British',    'Actress known for Harry Potter series.',         'https://example.com/emma.jpg'),
                                                                                  (3,  'Tom Cruise',        '1962-07-03', 'American',   'Action movie star, Mission Impossible series.',  'https://example.com/tom.jpg'),
                                                                                  (4,  'Scarlett Johansson','1984-11-22', 'American',   'Marvel Black Widow actress.',                    'https://example.com/scarlett.jpg'),
                                                                                  (5,  'Chris Hemsworth',   '1983-08-11', 'Australian', 'Actor known for Thor in Marvel.',                'https://example.com/chris.jpg'),
                                                                                  (6,  'Jennifer Lawrence', '1990-08-15', 'American',   'Hunger Games lead actress.',                     'https://example.com/jennifer.jpg'),
                                                                                  (7,  'Robert Downey Jr.', '1965-04-04', 'American',   'Iron Man in Marvel Cinematic Universe.',         'https://example.com/rdj.jpg'),
                                                                                  (8,  'Keanu Reeves',      '1964-09-02', 'Canadian',   'Known for The Matrix and John Wick.',            'https://example.com/keanu.jpg'),
                                                                                  (9,  'Angelina Jolie',    '1975-06-04', 'American',   'Actress and humanitarian.',                      'https://example.com/angelina.jpg'),
                                                                                  (10, 'Dwayne Johnson',    '1972-05-02', 'American',   'Also known as The Rock, action star.',           'https://example.com/rock.jpg'),
                                                                                  (11, 'Tom Holland',       '1996-06-01', 'British',    'Also known as Spider-Man, action star.',         'https://example.com/tom_holland.jpg'),
                                                                                  (12, 'Tobey Maguire',     '1975-06-27', 'American',   'Also known as Spider-Man, action star.',         'https://example.com/tobey_maguire.jpg');

-- ------------------------------------------------------------
-- 8. movie_actors
-- ------------------------------------------------------------
DROP TABLE IF EXISTS movie_actors;
CREATE TABLE movie_actors (
                              movie_id  BIGINT       NOT NULL,
                              actor_id  BIGINT       NOT NULL,
                              role_name VARCHAR(150),
                              PRIMARY KEY (movie_id, actor_id),

                              CONSTRAINT fk_movie_actor_movie
                                  FOREIGN KEY (movie_id)  REFERENCES movies(id) ON DELETE CASCADE,
                              CONSTRAINT fk_movie_actor_actor
                                  FOREIGN KEY (actor_id)  REFERENCES actors(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO movie_actors (movie_id, actor_id, role_name) VALUES
-- ⚠️ Breaking Bad & Stranger Things bị gán sai diễn viên từ data gốc — cần UPDATE lại actor_id
(6,  4,  'Walter White'),
(6,  5,  'Jesse Pinkman'),
(25, 4,  'Black Widow'),
(25, 7,  'Iron Man'),
(31, 1,  'Cobb'),
(31, 2,  'Arthur'),
(33, 3,  'Eleven'),
(33, 4,  'Mike Wheeler'),
(34, 3,  'Takashi Mori'),
(34, 5,  'Yuna Akiyama'),
(34, 8,  'Kenji Sato'),
(35, 6,  'Lukas Weber'),
(35, 9,  'Eva Hoffman'),
(35, 11, 'Victor Stein');

-- ------------------------------------------------------------
-- 9. episodes
--    FIX: bỏ duplicate unique key
--    FIX: thêm is_deleted để soft-delete
-- ------------------------------------------------------------
DROP TABLE IF EXISTS episodes;
CREATE TABLE episodes (
                          id             BIGINT       PRIMARY KEY AUTO_INCREMENT,
                          movie_id       BIGINT       NOT NULL,
                          episode_number INT          NOT NULL,
                          title          VARCHAR(255),
                          video_url      VARCHAR(255) NOT NULL,
                          duration       INT,
                          is_deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                          created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP,

                          UNIQUE KEY uq_movie_episode (movie_id, episode_number),

                          CONSTRAINT fk_episode_movie
                              FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;

INSERT INTO episodes (id, movie_id, episode_number, title, video_url, duration, created_at) VALUES
                                                                                                (4,  7,  1, 'Episode 1: Arrival',             'https://example.com/video7_1',  42, '2026-04-25 08:29:28'),
                                                                                                (5,  7,  2, 'Episode 2: The Fog',             'https://example.com/video7_2',  44, '2026-04-25 08:29:28'),
                                                                                                (6,  7,  3, 'Episode 3: Revelation',          'https://example.com/video7_3',  48, '2026-04-25 08:29:28'),
                                                                                                (7,  8,  1, 'Episode 1: First Kill',          'https://example.com/video8_1',  50, '2026-04-25 08:29:28'),
                                                                                                (8,  8,  2, 'Episode 2: The Chase',           'https://example.com/video8_2',  52, '2026-04-25 08:29:28'),
                                                                                                (9,  8,  3, 'Episode 3: Final Clue',          'https://example.com/video8_3',  55, '2026-04-25 08:29:28'),
                                                                                                (10, 9,  1, 'Episode 1: The Meeting',         'https://example.com/video9_1',  40, '2026-04-25 08:29:28'),
                                                                                                (11, 9,  2, 'Episode 2: The Journey',         'https://example.com/video9_2',  42, '2026-04-25 08:29:28'),
                                                                                                (12, 9,  3, 'Episode 3: Goodbye',             'https://example.com/video9_3',  45, '2026-04-25 08:29:28'),
                                                                                                (13, 10, 1, 'Episode 1: Awakening',           'https://example.com/video10_1', 60, '2026-04-25 08:29:28'),
                                                                                                (14, 10, 2, 'Episode 2: Battlefront',         'https://example.com/video10_2', 62, '2026-04-25 08:29:28'),
                                                                                                (15, 10, 3, 'Episode 3: Last Hope',           'https://example.com/video10_3', 65, '2026-04-25 08:29:28'),
                                                                                                (24, 6,  1, 'Pilot',                          'https://example.com/videos/breakingbad-ep1.mp4', 58, NULL),
                                                                                                (25, 6,  2, 'Cat''s in the Bag...',           'https://example.com/videos/breakingbad-ep2.mp4', 48, NULL),
                                                                                                (28, 33, 1, 'The Vanishing of Will Byers',    'https://example.com/episode1.mp4', 47, NULL),
                                                                                                (29, 33, 2, 'The Weirdo on Maple Street',     'https://example.com/episode2.mp4', 50, NULL);

-- ------------------------------------------------------------
-- 10. favorite_movies
--     FIX: thêm updated_at để audit
-- ------------------------------------------------------------
DROP TABLE IF EXISTS favorite_movies;
CREATE TABLE favorite_movies (
                                 user_id    BIGINT   NOT NULL,
                                 movie_id   BIGINT   NOT NULL,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (user_id, movie_id),

                                 CONSTRAINT fk_favorite_user
                                     FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE,
                                 CONSTRAINT fk_favorite_movie
                                     FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- 11. watch_history
--
--  VẤN ĐỀ NULL DUPLICATE:
--    UNIQUE(user_id, movie_id, episode_id) bị bypass khi episode_id = NULL
--    vì trong MySQL: NULL != NULL nên nhiều row (user=1, movie=2, episode=NULL)
--    vẫn không conflict.
--
--  GIẢI PHÁP ĐƯỢC CHỌN:
--    Dùng episode_id = 0 cho phim SINGLE thay vì NULL.
--    Application layer phải set episode_id = 0 khi ghi phim lẻ.
--    Unique constraint lúc này hoạt động đúng.
--
--  THAY THẾ (nếu muốn giữ NULL):
--    Tách thành watch_history_movies + watch_history_episodes
-- ------------------------------------------------------------
DROP TABLE IF EXISTS watch_history;
CREATE TABLE watch_history (
                               id              BIGINT   PRIMARY KEY AUTO_INCREMENT,
                               user_id         BIGINT   NOT NULL,
                               movie_id        BIGINT   NOT NULL,
    -- 0 = phim SINGLE (không có episode), >0 = episode_id thực tế
                               episode_id      BIGINT   NOT NULL DEFAULT 0,
                               watched_seconds INT      NOT NULL DEFAULT 0,
                               completed       BOOLEAN  NOT NULL DEFAULT FALSE,
                               last_watched_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               UNIQUE KEY uq_watch (user_id, movie_id, episode_id),

                               CONSTRAINT fk_watch_user
                                   FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE,
                               CONSTRAINT fk_watch_movie
                                   FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
    -- Không FK episode_id vì 0 không phải episode thật
    -- Application tự resolve episode_id > 0 khi cần
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_watch_user  ON watch_history(user_id);
CREATE INDEX idx_watch_movie ON watch_history(movie_id);

-- episode_id = 0 vì Inception là phim SINGLE
INSERT INTO watch_history (id, user_id, movie_id, episode_id, watched_seconds, completed, last_watched_at)
VALUES (2, 3, 31, 0, 70, FALSE, '2026-05-09 17:47:57');

-- ------------------------------------------------------------
-- 12. movie_views
--     FIX: ON DELETE CASCADE cho movie, ON DELETE SET NULL cho user
--     FIX: thêm idx_view_user
-- ------------------------------------------------------------
DROP TABLE IF EXISTS movie_views;
CREATE TABLE movie_views (
                             id         BIGINT      PRIMARY KEY AUTO_INCREMENT,
                             movie_id   BIGINT      NOT NULL,
                             user_id    BIGINT      NULL,
                             viewed_at  DATETIME    DEFAULT CURRENT_TIMESTAMP,
                             ip_address VARCHAR(50),

                             CONSTRAINT fk_view_movie
                                 FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                             CONSTRAINT fk_view_user
                                 FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_view_movie ON movie_views(movie_id);
CREATE INDEX idx_view_user  ON movie_views(user_id);

-- ------------------------------------------------------------
-- 13. movie_reviews
--     FIX: CHECK constraint rating (DB-level, dù cần validate app-layer thêm)
--     FIX: UNIQUE(user_id, movie_id) — mỗi user review 1 lần/phim
--     FIX: thêm updated_at (user có thể sửa review)
--     FIX: thêm updated_at để audit
-- ------------------------------------------------------------
DROP TABLE IF EXISTS movie_reviews;
CREATE TABLE movie_reviews (
                               id         BIGINT   PRIMARY KEY AUTO_INCREMENT,
                               user_id    BIGINT   NOT NULL,
                               movie_id   BIGINT   NOT NULL,
                               rating     INT      NOT NULL CHECK (rating BETWEEN 1 AND 5),
                               comment    TEXT,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               UNIQUE KEY uq_user_movie_review (user_id, movie_id),

                               CONSTRAINT fk_review_user
                                   FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE,
                               CONSTRAINT fk_review_movie
                                   FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- 14. subscription_plans
--     FIX: thêm updated_at để audit
-- ------------------------------------------------------------
DROP TABLE IF EXISTS subscription_plans;
CREATE TABLE subscription_plans (
                                    id            INT           PRIMARY KEY AUTO_INCREMENT,
                                    name          VARCHAR(100)  NOT NULL UNIQUE,
                                    price         DECIMAL(10,2) NOT NULL,
                                    duration_days INT           NOT NULL,
                                    max_devices   INT           NOT NULL DEFAULT 1,
                                    quality       ENUM('SD','HD','FULLHD','4K') NOT NULL DEFAULT 'HD',
                                    created_at    DATETIME      DEFAULT CURRENT_TIMESTAMP,
                                    updated_at    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- 15. subscriptions
--     FIX: thêm updated_at để audit
-- ------------------------------------------------------------
DROP TABLE IF EXISTS subscriptions;
CREATE TABLE subscriptions (
                               id         BIGINT   PRIMARY KEY AUTO_INCREMENT,
                               user_id    BIGINT   NOT NULL,
                               plan_id    INT      NOT NULL,
                               start_date DATETIME NOT NULL,
                               end_date   DATETIME NOT NULL,
                               status     ENUM('ACTIVE','EXPIRED','CANCELLED') NOT NULL DEFAULT 'ACTIVE',
                               auto_renew BOOLEAN  NOT NULL DEFAULT TRUE,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               CONSTRAINT fk_subscription_user
                                   FOREIGN KEY (user_id)  REFERENCES users(id),
                               CONSTRAINT fk_subscription_plan
                                   FOREIGN KEY (plan_id)  REFERENCES subscription_plans(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_subscription_user ON subscriptions(user_id);

-- ------------------------------------------------------------
-- 16. payments
--     FIX: thêm updated_at để audit
-- ------------------------------------------------------------
DROP TABLE IF EXISTS payments;
CREATE TABLE payments (
                          id               BIGINT        PRIMARY KEY AUTO_INCREMENT,
                          subscription_id  BIGINT        NOT NULL,
                          amount           DECIMAL(10,2) NOT NULL,
                          payment_method   VARCHAR(50),
                          payment_status   ENUM('PENDING','SUCCESS','FAILED') NOT NULL DEFAULT 'PENDING',
                          transaction_code VARCHAR(150)  UNIQUE,
                          paid_at          DATETIME,
                          created_at       DATETIME      DEFAULT CURRENT_TIMESTAMP,
                          updated_at       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT fk_payment_subscription
                              FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_payment_subscription ON payments(subscription_id);
CREATE INDEX idx_payment_status       ON payments(payment_status);

SET FOREIGN_KEY_CHECKS = 1;
-- ============================================================
-- END OF SCRIPT
-- ============================================================