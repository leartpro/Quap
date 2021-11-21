--Test-Environment
--5Users, 2Chatrooms with each 3-4Users, 4Friendships
INSERT INTO users(name, password) VALUES('Otto', 'Qwertz!1');
INSERT INTO users(name, password) VALUES('Horst', 'Qwertz!1');
INSERT INTO users(name, password) VALUES('Tomas', 'Qwertz!1');
INSERT INTO users(name, password) VALUES('Karl', 'Qwertz!1');
INSERT INTO users(name, password) VALUES('Gott', 'Besser!7');

INSERT INTO chatrooms(name, is_Private) VALUES('group1', false);
INSERT INTO chatrooms(name, is_Private) VALUES('group2', false);

INSERT INTO chatrooms(name, is_Private) VALUES('dirChat2-3', true);
INSERT INTO chatrooms(name, is_Private) VALUES('dirChat2-1', true);
INSERT INTO chatrooms(name, is_Private) VALUES('dirChat3-4', true);
INSERT INTO chatrooms(name, is_Private) VALUES('dirChat4-5', true);

INSERT INTO participants(user_id, chatroom_id) VALUES(2, 1);
INSERT INTO participants(user_id, chatroom_id) VALUES(3, 1);
INSERT INTO participants(user_id, chatroom_id) VALUES(4, 1);

INSERT INTO participants(user_id, chatroom_id) VALUES(1, 2);
INSERT INTO participants(user_id, chatroom_id) VALUES(2, 2);
INSERT INTO participants(user_id, chatroom_id) VALUES(3, 2);
INSERT INTO participants(user_id, chatroom_id) VALUES(5, 2);

INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(2, 3, 3);
INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(3, 2, 3);

INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(2, 1, 4);
INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(1, 2, 4);

INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(3, 4, 5);
INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(4, 3, 5);

INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(4, 5, 6);
INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(5, 4, 6);

