insert into users(id, email, password, role)
VALUES (1, 'admin@gmail.com', '$2a$12$O8HXfulPUIINsFmtBeVLj.9KVv8.CNTvV6N.CX1BLRaHOJ3FSFkHW', 'ADMIN'),
       (2, 'instructor@gmail.com', '$2a$12$OMw.k8L5UXMCexXCuaiXU.Ub0iie87VVehG8MrjLdfwEeXYIfrpZu', 'INSTRUCTOR'),
       (3, 'student@gmail.com', 'students244com', 'STUDENT');

insert into groups(id, date_of_start, group_description, group_image, group_name)
values (1, '2022/11/10', 'Group Description', 'Group image link', 'Group name');

insert into courses(id, course_description, course_image, course_name, date_of_start)
values (1, 'Course Description', 'link image', 'course name', '2022/11/10');

insert into instructors(id, first_name, last_name, phone_number, specialization, user_id)
values (1, 'Instructor firs name', 'Instructor last name', '555231245', 'Java', 1);

insert into students(id, email, first_name, last_name, phone_number, study_format, group_id, user_id)
values (1, 'student@gmail.com', 'Student fist name', 'Student last name', ' 556545652', 'OFFLINE', 1, 1);

insert into lessons(id, lesson_name, course_id)
values (1, 'Lesson name', 1);

insert into links(id, link, link_text, lesson_id)
values (1, 'link', 'The link text', 1);

insert into presentations(id, presentation_description, presentation_link, presentation_name, lesson_id)
values (1, 'Presentation description', 'presentation link', 'Presentation name', 1);

insert into videos(id, video_description, video_link, video_name, lesson_id)
values (1, 'Video description', 'Video link', 'Video name', 1);

insert into tasks(id, task_name, lesson_id)
values (1, 'Task name', 1);

insert into contents(id, content_format, content_name, content_value, task_id)
values (1, 'VIDEO', 'content name', 'Value', 1);

insert into tests(id, is_enable, test_name, lesson_id)
values (1, false, 'Test name', 1);

insert into questions(id, question, question_type)
values (1, 'question1', 'SINGLETON');

insert into options(id, is_true, option_value)
values (1, true, 'option value1'),
       (2, false, 'option value2');

insert into results(id, date_of_pass, percent, student_id, test_id)
values (1, '2022/11/10', 100, 1, 1);