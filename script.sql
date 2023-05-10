DROP SCHEMA IF EXISTS euprava2023;
CREATE SCHEMA euprava2023 DEFAULT CHARACTER SET utf8;
USE euprava2023;

CREATE TABLE manufacturer (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(30) NOT NULL,
                              country VARCHAR(30) NOT NULL,
                              UNIQUE (id)
);

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(30) NOT NULL,
                       lastname VARCHAR(30) NOT NULL,
                       email VARCHAR(50) NOT NULL,
                       password VARCHAR(16) NOT NULL,
                       address VARCHAR(70) NOT NULL,
                       phone VARCHAR(20) NOT NULL,
                       jmbg VARCHAR(15) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       registrationdate TIMESTAMP NOT NULL,
                       birth DATE NOT NULL,
                       UNIQUE (email),
                       UNIQUE (phone)
);

CREATE TABLE vaccine (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         available INT NOT NULL,
                         manufacturer INT NOT NULL,
                         UNIQUE (id),
                         FOREIGN KEY (manufacturer) REFERENCES manufacturer(id) ON DELETE CASCADE
);

CREATE TABLE news (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(120) NOT NULL,
                      content VARCHAR(400) NOT NULL,
                      postedon DATETIME NOT NULL,
                      UNIQUE (id)
);

CREATE TABLE infectednews (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              infected INT NOT NULL,
                              tested INT NOT NULL,
                              hospitalized INT NOT NULL,
                              respirator INT NOT NULL,
                              date DATETIME NOT NULL,
                              UNIQUE (id)
);

CREATE TABLE patientinfo (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             vaccinated BIT NOT NULL,
                             received TINYINT NOT NULL,
                             lastdose DATETIME NOT NULL,
                             userid INT NOT NULL,
                             FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE,
                             UNIQUE (id)
);

CREATE TABLE applications (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              date DATETIME NOT NULL,
                              patient INT NOT NULL,
                              vaccine INT NOT NULL,
                              FOREIGN KEY (patient) REFERENCES users(id) ON DELETE CASCADE,
                              FOREIGN KEY (vaccine) REFERENCES vaccine(id) ON DELETE CASCADE,
                              UNIQUE (id)
);

CREATE TABLE buyrequest (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            amount INT NOT NULL,
                            reason VARCHAR(200),
                            date DATE NOT NULL,
                            status VARCHAR(70),
                            employee INT NOT NULL,
                            vaccine INT NOT NULL,
                            comment VARCHAR(200),
                            FOREIGN KEY (employee) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (vaccine) REFERENCES vaccine(id) ON DELETE CASCADE,
                            UNIQUE (id)
);

CREATE FUNCTION totalinfected(newsid INT) RETURNS INT
    READS SQL DATA
    DETERMINISTIC
BEGIN
    RETURN (SELECT SUM(infected) FROM infectednews WHERE date <= (SELECT date FROM infectednews WHERE id = newsid));
END;