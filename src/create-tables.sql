CREATE TABLE users (
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_picture VARCHAR(255),
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT,
    reviewer_id INT,
    show_id INT,
    show_title TEXT,
    rating DOUBLE NOT NULL,
    commentary TEXT DEFAULT NULL,
    contains_spoilers BOOLEAN DEFAULT false,
    num_likes INT DEFAULT 0,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (reviewer_id, show_id),
    UNIQUE (review_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(id)
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

CREATE TABLE user_episode_rankings (
    user_id INT,
    show_id INT NOT NULL,
    season INT,
    episode INT,
    rank_num INT,
    PRIMARY KEY(user_id, show_id, season, episode),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE show_info (
    show_id INT,
    title TEXT,
    poster_path TEXT,
    PRIMARY KEY(show_id)
);

CREATE TABLE episode_info (
    show_id INT,
    show_title TEXT,
    episode_title TEXT,
    season INT,
    episode INT,
    poster_path TEXT,
    PRIMARY KEY(show_id, season, episode)
);

CREATE TABLE liked_reviews (
    user_id INT,
    review_id INT,
    PRIMARY KEY(user_id, review_id),
    FOREIGN KEY(user_id) REFERENCES users(id)
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
)