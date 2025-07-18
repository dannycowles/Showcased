CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_picture VARCHAR(255),
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    bio VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS show_info (
    show_id INT,
    title TEXT,
    poster_path TEXT,
    PRIMARY KEY(show_id)
);

CREATE TABLE IF NOT EXISTS show_reviews (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_id INT NOT NULL,
    rating DOUBLE NOT NULL,
    commentary TEXT DEFAULT NULL,
    contains_spoilers BOOLEAN DEFAULT false,
    num_likes INT DEFAULT 0,
    num_comments INT DEFAULT 0,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (show_id) REFERENCES show_info(show_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS liked_show_reviews (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    review_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, review_id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(review_id) REFERENCES show_reviews(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS episode_info (
    id INT,
    show_id INT NOT NULL,
    show_title TEXT NOT NULL,
    episode_title TEXT NOT NULL,
    season INT NOT NULL,
    episode INT NOT NULL,
    poster_path TEXT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS episode_reviews (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    episode_id INT NOT NULL,
    rating DOUBLE NOT NULL,
    commentary TEXT DEFAULT NULL,
    contains_spoilers BOOLEAN DEFAULT false,
    num_likes INT DEFAULT 0,
    num_comments INT DEFAULT 0,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, episode_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (episode_id) REFERENCES episode_info(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS liked_episode_reviews (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    review_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, review_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES episode_reviews(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS currently_watching (
    user_id INT,
    show_id INT NOT NULL,
    PRIMARY KEY (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_watchlist (
    user_id INT,
    show_id INT NOT NULL,
    PRIMARY KEY (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_show_rankings (
    user_id INT,
    show_id INT NOT NULL,
    rank_num INT NOT NULL,
    PRIMARY KEY (user_id, show_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_episode_rankings (
    user_id INT,
    episode_id INT,
    rank_num INT NOT NULL,
    PRIMARY KEY(user_id, episode_id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(episode_id) REFERENCES episode_info(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS otp_requests (
    user_id INT,
    otp VARCHAR(6),
    expires_at DATETIME,
    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS followers (
    id INT AUTO_INCREMENT,
    follower_id INT NOT NULL,
    following_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (follower_id, following_id),
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS season_info (
    id INT,
    show_id INT NOT NULL,
    season INT NOT NULL,
    poster_path TEXT NOT NULL,
    show_title TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_season_rankings (
    user_id INT,
    season_id INT,
    rank_num INT NOT NULL,
    PRIMARY KEY(user_id, season_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (season_id) REFERENCES season_info(id)
);

CREATE TABLE IF NOT EXISTS character_info (
    id VARCHAR(255),
    show_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_character_rankings (
    user_id INT,
    character_id VARCHAR(255),
    character_type ENUM('Protagonist', 'Deuteragonist', 'Antagonist', 'Tritagonist', 'Side') NOT NULL,
    rank_num INT NOT NULL,
    PRIMARY KEY(user_id, character_id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(character_id) REFERENCES character_info(id) ON DELETE CASCADE,
    CHECK (character_type IN ('Protagonist', 'Deuteragonist', 'Antagonist', 'Tritagonist', 'Side'))
);

CREATE TABLE IF NOT EXISTS user_dynamics_rankings (
  id INT AUTO_INCREMENT,
  user_id INT NOT NULL,
  character1_id VARCHAR(255) NOT NULL,
  character2_id VARCHAR(255) NOT NULL,
  rank_num INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (user_id, character1_id, character2_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (character1_id) REFERENCES character_info(id) ON DELETE CASCADE,
  FOREIGN KEY (character2_id) REFERENCES character_info(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_collections (
    user_id INT NOT NULL,
    collection_id INT AUTO_INCREMENT,
    collection_name VARCHAR(255) NOT NULL,
    description TEXT DEFAULT NULL,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    is_ranked BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (collection_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS shows_in_collections (
    collection_id INT,
    show_id INT,
    rank_num INT DEFAULT NULL,
    PRIMARY KEY (collection_id, show_id),
    FOREIGN KEY (collection_id) REFERENCES user_collections(collection_id) ON DELETE CASCADE,
    UNIQUE (collection_id, rank_num)
);

CREATE TABLE IF NOT EXISTS liked_collections (
    user_id INT,
    collection_id INT,
    PRIMARY KEY (user_id, collection_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (collection_id) REFERENCES user_collections(collection_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS social_platforms (
    id INT AUTO_INCREMENT,
    name TEXT NOT NULL,
    url TEXT NOT NULL,
    icon TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_socials (
    user_id INT,
    social_id INT,
    handle TEXT NOT NULL,
    PRIMARY KEY (user_id, social_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (social_id) REFERENCES social_platforms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS show_review_comments (
    id INT AUTO_INCREMENT,
    review_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    num_likes INT DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES show_reviews(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS liked_show_review_comments (
    user_id INT,
    comment_id INT,
    PRIMARY KEY (user_id, comment_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES show_review_comments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS episode_review_comments (
    id INT AUTO_INCREMENT,
    review_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    num_likes INT DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES episode_reviews(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS liked_episode_review_comments (
    user_id INT,
    comment_id INT,
    PRIMARY KEY (user_id, comment_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES episode_review_comments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS activity_descriptions (
     activity_type INT,
     description TEXT NOT NULL,
     PRIMARY KEY (activity_type)
);

CREATE TABLE IF NOT EXISTS activities (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity_type INT NOT NULL,
    external_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_type) REFERENCES activity_descriptions(activity_type) ON DELETE CASCADE
);