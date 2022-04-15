INSERT INTO auth_user(id, login, password, e_mail, first_name, status_id, role_id)
VALUES (3, 'employee', '$2a$12$E1ud1r1Hcz81JyMD/D9ZXu/X3/KTGjfOmZwnj6CNaMy.ddrm0yypG', 'employee@gmai.com',
        'Александра', 1, 1);

INSERT INTO auth_user_attempts(user_id, quantity)
VALUES (3, 5);