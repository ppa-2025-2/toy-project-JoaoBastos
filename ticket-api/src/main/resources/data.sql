DELETE
FROM users;
DELETE
FROM roles;
DELETE
from types;

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_GUEST'),
       ('ROLE_VIEWER')
;

INSERT INTO types (name)
VALUES ('TYPE_VIEWER'),
       ('TYPE_TECNICO')
;