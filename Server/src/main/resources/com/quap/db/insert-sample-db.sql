--TODO: generate sample users, friendships and chatrooms as test-environment

INSERT INTO users(name, password) VALUES('otto', 'qwertz!10');
INSERT INTO users(name, password) VALUES('horst', 'qwertz!10');
INSERT INTO users(name, password) VALUES('burak', 'qwertz!10');
INSERT INTO users(name, password) VALUES('tomas', 'qwertz!10');
INSERT INTO users(name, password) VALUES('karl', 'qwertz!10');

INSERT INTO chatrooms(name, is_Private) VALUES('group1', false);
INSERT INTO chatrooms(name, is_Private) VALUES('directChat', true);

INSERT INTO participants(user_id, chatroom_id) VALUES(2, 1);
INSERT INTO participants(user_id, chatroom_id) VALUES(3, 1);
INSERT INTO participants(user_id, chatroom_id) VALUES(4, 1);

INSERT INTO participants(user_id, chatroom_id) VALUES(1, 2);
INSERT INTO participants(user_id, chatroom_id) VALUES(3, 2);

INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(1, 3, 1);
INSERT INTO friends(friend1_id, friend2_id, chat_id) VALUES(3, 1, 1);
