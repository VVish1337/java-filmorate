CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INT PRIMARY KEY AUTO_INCREMENT,
    EMAIL     VARCHAR(255) NOT NULL,
    LOGIN     VARCHAR(255) NOT NULL,
    USER_NAME VARCHAR(255) NOT NULL,
    BIRTHDAY  DATE
);

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID   INT PRIMARY KEY,
    MPA_NAME VARCHAR(10)

);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INT PRIMARY KEY AUTO_INCREMENT,
    FILM_NAME    varchar(255),
    DESCRIPTION  varchar(200),
    DURATION     INT,
    RELEASE_DATE DATE,
    MPA_ID       INT REFERENCES MPA (MPA_ID)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID   INT PRIMARY KEY,
    GENRE_NAME VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    PRIMARY KEY (FILM_ID, GENRE_ID),
    FILM_ID  INT REFERENCES FILMS(FILM_ID),
    GENRE_ID INT REFERENCES GENRES(GENRE_ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    FILM_ID INT,
    USER_ID INT,
    PRIMARY KEY (FILM_ID, USER_ID),
    CONSTRAINT FK_FILM_LIKES_FILM_ID FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    CONSTRAINT FK_FILM_LIKES_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FRIEND_LIST
(
    USER_ID   INT NOT NULL,
    FRIEND_ID INT NOT NULL,
    PRIMARY KEY (USER_ID, FRIEND_ID),
    CONSTRAINT FK_USERS_FRIENDS_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    CONSTRAINT FK_USERS_FRIENDS_FRIEND_ID FOREIGN KEY (FRIEND_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);