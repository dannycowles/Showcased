CREATE TABLE users (
    id INT AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reviews (
    id INT AUTO_INCREMENT,
    reviewerId INT NOT NULL,
    showId INT NOT NULL,
    commentary TEXT DEFAULT NULL,
    containsSpoilers BOOLEAN DEFAULT false,
    numLikes INT DEFAULT 0,
    reviewDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (reviewerId) REFERENCES users(id)
);

CREATE TABLE currently_watching (
    userId INT,
    showId INT NOT NULL,
    PRIMARY KEY (userId, showId),
    FOREIGN KEY (userId) REFERENCES users(id)
);

CREATE TABLE user_watchlist (
    userId INT,
    showId INT NOT NULL,
    PRIMARY KEY (userId, showId),
    FOREIGN KEY (userId) REFERENCES users(id)
);

CREATE TABLE user_show_rankings (
    userId INT,
    showId INT NOT NULL,
    rankNum INT NOT NULL,
    PRIMARY KEY (userId, showId),
    FOREIGN KEY (userId) REFERENCES users(id)
);