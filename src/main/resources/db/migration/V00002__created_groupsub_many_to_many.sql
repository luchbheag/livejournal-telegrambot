-- add PRIMARY KEY FOR tg_user
ALTER TABLE tg_user ADD PRIMARY KEY (chat_id);

-- ensure that the tables with these names are removed before creating a new one.
DROP TABLE IF EXISTS blog_sub;
DROP TABLE IF EXISTS blog_x_user;
DROP TABLE IF EXISTS article_previews;

CREATE TABLE article_previews (
                                  id INT,
                                  main_header VARCHAR(100),
                                  sub_header VARCHAR(100),
                                  text TEXT,
                                  link VARCHAR(200),
                                  PRIMARY KEY (id)
);

CREATE TABLE blog_sub (
                           id VARCHAR(100),
                           last_article_id INT,
                           PRIMARY KEY (id),
                           FOREIGN KEY (last_article_id) REFERENCES article_previews(id)
);

CREATE TABLE blog_x_user (
                              blog_sub_id VARCHAR(100) NOT NULL,
                              user_id VARCHAR(100) NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES tg_user(chat_id),
                              FOREIGN KEY (blog_sub_id) REFERENCES blog_sub(id),
                              UNIQUE(user_id, blog_sub_id)
);


