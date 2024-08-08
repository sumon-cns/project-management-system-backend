create table if not exists project_pm_user
(
    project_id int references projects (id),
    pm_user_id int references pm_users (id)
);