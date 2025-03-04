CREATE TABLE users (
    id INT AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reviews (
    id INT AUTO_INCREMENT,
    reviewer_id INT NOT NULL,
    show_id INT NOT NULL,
    rating DOUBLE NOT NULL,
    commentary TEXT DEFAULT NULL,
    contains_spoilers BOOLEAN DEFAULT false,
    num_likes INT DEFAULT 0,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
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