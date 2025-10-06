CREATE TYPE emp_status AS ENUM ('ACTIVE','INACTIVE');


CREATE table if not exists employee
(
    id SERIAL PRIMARY KEY,
    emp_id VARCHAR(15) NOT NULL UNIQUE,
    emp_name VARCHAR(100) NOT NULL unique,
    uuid VARCHAR,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    status emp_status NOT NULL,
    manager_id INT,
    CONSTRAINT fk_manager FOREIGN KEY (manager_id) REFERENCES employee(id)
);

