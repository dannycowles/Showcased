CREATE TABLE users (
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_picture VARCHAR(255),
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    bio VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE show_reviews (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_id INT NOT NULL,
    rating DOUBLE NOT NULL,
    commentary TEXT DEFAULT NULL,
    contains_spoilers BOOLEAN DEFAULT false,
    num_likes INT DEFAULT 0,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (show_id) REFERENCES show_info(show_id) ON DELETE CASCADE
);

CREATE TABLE liked_show_reviews (
    user_id INT,
    review_id INT,
    PRIMARY KEY(user_id, review_id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(review_id) REFERENCES show_reviews(id) ON DELETE CASCADE
);

CREATE TABLE episode_reviews (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    episode_id INT NOT NULL,
    rating DOUBLE NOT NULL,
    commentary TEXT DEFAULT NULL,
    contains_spoilers BOOLEAN DEFAULT false,
    num_likes INT DEFAULT 0,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, episode_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (episode_id) REFERENCES episode_info(id) ON DELETE CASCADE
);

CREATE TABLE liked_episode_reviews (
    user_id INT,
    review_id INT,
    PRIMARY KEY (user_id, review_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES episode_reviews(id) ON DELETE CASCADE
);

CREATE TABLE currently_watching (
    user_id INT,
    show_id INT NOT NULL,
    PRIMARY KEY (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_watchlist (
    user_id INT,
    show_id INT NOT NULL,
    PRIMARY KEY (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_show_rankings (
    user_id INT,
    show_id INT NOT NULL,
    rank_num INT NOT NULL,
    PRIMARY KEY (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE show_info (
    show_id INT,
    title TEXT,
    poster_path TEXT,
    PRIMARY KEY(show_id)
);

CREATE TABLE user_episode_rankings (
    user_id INT,
    episode_id INT,
    rank_num INT NOT NULL,
    PRIMARY KEY(user_id, episode_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(episode_id) REFERENCES episode_info(id)
);

CREATE TABLE episode_info (
    id INT,
    show_id INT NOT NULL,
    show_title TEXT NOT NULL,
    episode_title TEXT NOT NULL,
    season INT NOT NULL,
    episode INT NOT NULL,
    poster_path TEXT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE otp_requests (
    user_id INT,
    otp VARCHAR(6),
    expires_at DATETIME,
    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE followers (
    follower_id INT,
    following_id INT,
    PRIMARY KEY(follower_id, following_id),
    FOREIGN KEY(follower_id) REFERENCES users(id),
    FOREIGN KEY (following_id) REFERENCES users(id)
);

CREATE TABLE user_season_rankings (
    user_id INT,
    season_id INT,
    rank_num INT NOT NULL,
    PRIMARY KEY(user_id, season_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (season_id) REFERENCES season_info(id)
);

CREATE TABLE season_info (
    id INT,
    show_id INT NOT NULL,
    season INT NOT NULL,
    poster_path TEXT NOT NULL,
    show_title TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_character_rankings (
    user_id INT,
    character_id VARCHAR(255),
    character_type ENUM('Protagonist', 'Deuteragonist', 'Antagonist', 'Tritagonist', 'Side') NOT NULL,
    rank_num INT NOT NULL,
    PRIMARY KEY(user_id, character_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(character_id) REFERENCES character_info(id),
    CHECK (character_type IN ('Protagonist', 'Deuteragonist', 'Antagonist', 'Tritagonist', 'Side'))
);

CREATE TABLE character_info (
    id VARCHAR(255),
    show_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_collections (
    user_id INT NOT NULL,
    collection_id INT AUTO_INCREMENT,
    collection_name VARCHAR(255) NOT NULL,
    description TEXT DEFAULT NULL,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    is_ranked BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (collection_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE shows_in_collections (
    collection_id INT,
    show_id INT,
    rank_num INT DEFAULT NULL,
    PRIMARY KEY (collection_id, show_id),
    FOREIGN KEY (collection_id) REFERENCES user_collections(collection_id),
    UNIQUE (collection_id, rank_num)
);

CREATE TABLE liked_collections (
    user_id INT,
    collection_id INT,
    PRIMARY KEY (user_id, collection_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (collection_id) REFERENCES user_collections(collection_id)
);

CREATE TABLE social_platforms (
    id INT AUTO_INCREMENT,
    name TEXT NOT NULL,
    url TEXT NOT NULL,
    icon TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_socials (
    user_id INT,
    social_id INT,
    handle TEXT NOT NULL,
    PRIMARY KEY (user_id, social_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (social_id) REFERENCES social_platforms(id)
);
