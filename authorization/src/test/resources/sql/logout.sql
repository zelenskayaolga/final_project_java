INSERT INTO auth_user(id, login, password, e_mail, first_name, status_id, role_id)
VALUES (10, 'zelenskayaolga', '$2a$10$calWZWI8UQshR01YuRIuT.jCdVJ00G3NmJq.d2jYqmEnZ6B9us2oK', 'zelenskayao@mail.ru',
        'Зеленская',
        1, 1);

INSERT INTO auth_session(id, creation_date, destruction_date, timeout_id, status_id, user_id)
VALUES ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlhb2xnYSIsInVzZXJJZCI6MTAsImlhdCI6MTY0ODU3Njg5OCwiZXhwIjoxNjQ4ODM2MDk4fQ.Z4S7mTpbIw7uhX7aX0PD5wNRbIilJ59G-4clO7BTM_zpOdNcL4vl9cW8CbAFFNEVXHjC7MSocLD0-NakO-Tt_Q',
        '2022-03-29 21:01:38.242285', null, 1, 1, 10);

INSERT INTO auth_user_details(user_id, creation_date, authorization_date, exit_date)
VALUES (10, '2022-03-29 21:01:22.363562', null, null)