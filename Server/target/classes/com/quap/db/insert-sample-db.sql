--Test-Environment
INSERT INTO users(name, password) VALUES('Otto', 'Qwertz!1');
INSERT INTO users(name, password) VALUES('Horst', 'Qwertz!1');
INSERT INTO users(name, password) VALUES('Olaf', 'Qwertz!1');

INSERT INTO chatrooms(name, is_Private) VALUES('dirChat2-1', true);
INSERT INTO chatrooms(name, is_Private) VALUES('Klettern', false);

INSERT INTO participants(user_id, chatroom_id) VALUES(2, 1);
INSERT INTO participants(user_id, chatroom_id) VALUES(1, 1);

INSERT INTO participants(user_id, chatroom_id) VALUES(1, 2);
INSERT INTO participants(user_id, chatroom_id) VALUES(2, 2);
INSERT INTO participants(user_id, chatroom_id) VALUES(3, 2);

INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(2, 1, 1);
INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(1, 2, 1);


