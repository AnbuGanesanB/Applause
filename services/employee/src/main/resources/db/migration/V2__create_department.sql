CREATE table if not exists department
(
    id SERIAL PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL UNIQUE,
    manager INT,
    CONSTRAINT fk_manager_dept FOREIGN KEY (manager) REFERENCES employee(id)
);