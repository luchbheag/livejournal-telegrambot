DROP TABLE IF EXISTS unparsed_blog_confirm;

CREATE TABLE unparsed_blog_confirm (
                        user_id VARCHAR(100),
                        blog_name VARCHAR(100),
                        PRIMARY KEY (user_id)
);