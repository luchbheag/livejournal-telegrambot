# livejournal-telegrambot
Teleram bot that sends notifications about new posts in selected LiveJournal blogs. If blog cannot be parsed, the bot allows user to be added to the waiting list and recieve notification when developers update parsing mechanism.

Used **techologies**: Java, Spring Boot, MySql, Flyway, JUnit, Mockito, Docker, Maven. The CI/CD pipeline implemented.
## How to Install and Run the Project

You need **Docker** to be installed. Clone project repository to your local machine. In project repository, run following command:
```
docker-compose -f docker-compose.yml up
```
To stop application, run
```
docker-compose -f docker-compose.yml up
```
## How to Use Project
List of available commands:

**/start** - start/restart work with bot <br />
**/stop** - stop work with bot <br />
**/addblogsub** - subscribe to blog updates <br />
**/deleteblogsub** - unsubscribe from blog updates <br />
**/listblogsub** - list of the blogs user subscribed on <br />
**/help** - get help working with bot <br />

**/ahelp** - show list of command available for admins only <br />
**/stat** - show stat info (how many users use bot etc., admin-only command)

If blog cannot be parsed, the bot offers user to be added in the waiting list and waits for confirmation by **yes** word.
