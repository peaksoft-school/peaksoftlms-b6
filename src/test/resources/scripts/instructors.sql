insert into instructors(id, first_name, last_name, phone_number, specialization, user_id)
values (cast(1 as bigint), 'Instructor name', 'Instructor lastname', '555231245', 'DevOps', 1),
       (cast(2 as bigint), 'Chyngyz', 'Sharshekeev', '4423242', 'Java developer', 1);

insert into users(id, email, password, role)
VALUES (1, 'instructor@gmail.com', '$2a$12$c2vzSJ1UIpODKy4dLqa50OMKTBnMQM7dx6lexC013ykPi9bJEAq4q', 'INSTRUCTOR'),
       (2,'john@gmail.com','$2a$12$9tlaIRbbKDpCjYKBUIoIuu3mUT8fBMlgd1Aqd1sdkUpxf/BPRWNPW','INSTRUCTOR');