DROP TABLE IF EXISTS unparsed_blog;
DROP TABLE IF EXISTS unparsed_x_user;

CREATE TABLE unparsed_blog (
                          id VARCHAR(100),
                          PRIMARY KEY (id)
);

CREATE TABLE unparsed_x_user (
                             unparsed_blog_id VARCHAR(100) NOT NULL,
                             user_id VARCHAR(100) NOT NULL,
                             FOREIGN KEY (user_id) REFERENCES tg_user(chat_id),
                             FOREIGN KEY (unparsed_blog_id) REFERENCES unparsed_blog(id),
                             UNIQUE(user_id, unparsed_blog_id)
);


