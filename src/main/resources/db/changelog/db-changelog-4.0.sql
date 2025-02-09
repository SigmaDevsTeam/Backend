-- --liquibase formatted sql

--changeset vladislav:1

INSERT INTO users (username, email, password, image, role, rating, users_rated) VALUES
('john_doe', 'john.doe@example.com', 'securePass123', 'john.jpg', 'USER', 4.7, 15),
('emma_smith', 'emma.smith@example.com', 'passEmma2023', 'emma.jpg', 'USER', 4.3, 12),
('alex_jones', 'alex.jones@example.com', 'alexStrong99', 'alex.jpg', 'ADMIN', 4.9, 30),
('sophia_wilson', 'sophia.wilson@example.com', 'wilsonSophie', 'sophia.jpg', 'USER', 3.8, 7),
('michael_brown', 'michael.brown@example.com', 'brownMike88', 'michael.jpg', 'USER', 4.2, 10),
('olivia_miller', 'olivia.miller@example.com', 'oliviaMpass', 'olivia.jpg', 'USER', 4.5, 18),
('william_taylor', 'william.taylor@example.com', 'taylorWill456', 'william.jpg', 'USER', 3.9, 6),
('ava_anderson', 'ava.anderson@example.com', 'avaStrongPass', 'ava.jpg', 'USER', 4.6, 14),
('james_thomas', 'james.thomas@example.com', 'thomasJames12', 'james.jpg', 'USER', 4.8, 22),
('charlotte_white', 'charlotte.white@example.com', 'whiteChar2024', 'charlotte.jpg', 'USER', 4.1, 9);

INSERT INTO quest (title, description, image, task_count, time_limit, user_id, rating, users_rated) VALUES
('Python Basics', 'Learn the fundamentals of Python programming', 'python.jpg', 5, '00:30:00', 1, 4.5, 100),
('Java Fundamentals', 'Introduction to Java programming', 'java.jpg', 7, '00:45:00', 2, 4.7, 85),
('History of Art', 'Explore the history of art through the ages', 'art.jpg', 10, '01:00:00', 3, 4.8, 120),
('Space Exploration', 'Discover the wonders of space', 'space.jpg', 6, '00:40:00', 4, 4.6, 95),
('Music Theory', 'Understand the basics of music composition', 'music.jpg', 8, '00:50:00', 5, 4.4, 75);

INSERT INTO task (title, audio, video, image, open_answer, quest_id) VALUES
('What is a variable?', 'var.mp3', 'var.mp4', 'var.jpg', 'A variable is a storage location in programming.', 1),
('Java Data Types', 'datatypes.mp3', 'datatypes.mp4', 'datatypes.jpg', 'Java has primitive and reference data types.', 2),
('Renaissance Art', 'renaissance.mp3', 'renaissance.mp4', 'renaissance.jpg', 'Renaissance art flourished in the 14th-17th centuries.', 3),
('First Moon Landing', 'apollo11.mp3', 'apollo11.mp4', 'apollo11.jpg', '1969', 4),
('Major Chords', 'chords.mp3', 'chords.mp4', 'chords.jpg', 'A major chord consists of a root, major third, and perfect fifth.', 5);

INSERT INTO option (title, is_true, task_id) VALUES
('A named storage for data', true, 1),
('An error in code', false, 1),
('A mathematical function', false, 1),
('int, float, char, boolean', true, 2),
('Only string and int', false, 2),
('Leonardo da Vinci was a famous Renaissance artist.', true, 3),
('The Renaissance started in the 18th century.', false, 3),
('The first moon landing was in 1969.', true, 4),
('The first moon landing was in 1980.', false, 4),
('A major chord consists of three notes.', true, 5);

INSERT INTO comment (title, user_id, quest_id) VALUES
('Very helpful quest! I learned a lot about Python.', 1, 1),
('Great material, but it would be nice to add more practical exercises.', 2, 1),
('Java is a great language! Thanks for the quest!', 3, 2),
('Interesting topic! Would love to see more information about Baroque.', 4, 3),
('Amazing space exploration quest! Very informative.', 5, 4),
('Great explanation of music theory. Everything is clear now.', 6, 5),
('Really enjoyed this quest. The content was engaging and well-organized.', 7, 1),
('This task was a bit too challenging for me, but still very rewarding.', 8, 2),
('I appreciate the detailed approach to the first moon landing. Very cool.', 9, 4),
('The chord theory part was very useful. Learned something new!', 10, 5);
