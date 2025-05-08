-- Database: Hospital Information System

-- DROP DATABASE IF EXISTS "Hospital Information System";

-- 1. Core Employees
CREATE TABLE employees (
    employee_id   BIGSERIAL    PRIMARY KEY,
    first_name    VARCHAR(50)   NOT NULL,
    last_name     VARCHAR(50)   NOT NULL,
    address       TEXT,
    phone         VARCHAR(20)
);

-- 2. Doctors (must come after employees)
CREATE TABLE doctors (
    employee_id   BIGINT        PRIMARY KEY,
    specialty     VARCHAR(100)  NOT NULL,
    FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
);

-- 3. Departments (now properly references a doctor as director)
CREATE TABLE departments (
    department_id   SERIAL      PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    building        VARCHAR(100) NOT NULL,
    director_id     BIGINT      NOT NULL,
    FOREIGN KEY (director_id)
        REFERENCES doctors(employee_id)
);

-- 4. Nurses (each tied to one department)
CREATE TABLE nurses (
    employee_id    BIGINT        PRIMARY KEY,
    rotation       VARCHAR(50)   NOT NULL,
    department_id  INT           NOT NULL,
    salary         NUMERIC(10,2) NOT NULL,
    FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id),
    FOREIGN KEY (department_id)
        REFERENCES departments(department_id)
);

-- 5. Wards (ward_number unique per department)
CREATE TABLE wards (
    ward_id         SERIAL       PRIMARY KEY,
    department_id   INT          NOT NULL,
    ward_number     SMALLINT     NOT NULL,
    beds            SMALLINT     NOT NULL,
    supervisor_id   BIGINT       NOT NULL,
    FOREIGN KEY (department_id)
        REFERENCES departments(department_id),
    FOREIGN KEY (supervisor_id)
        REFERENCES nurses(employee_id),
    UNIQUE (department_id, ward_number)
);

-- 6. Patients
CREATE TABLE patients (
    patient_id   BIGSERIAL      PRIMARY KEY,
    first_name   VARCHAR(50)    NOT NULL,
    last_name    VARCHAR(50)    NOT NULL,
    address      TEXT,
    phone        VARCHAR(20)
);

-- 7. Patient Admissions
CREATE TABLE patient_admissions (
    admission_id     BIGSERIAL    PRIMARY KEY,
    patient_id       BIGINT       NOT NULL,
    ward_id          INT          NOT NULL,
    bed_number       SMALLINT     NOT NULL,
    diagnosis        VARCHAR(255) NOT NULL,
    date_admitted    DATE         NOT NULL,
    date_discharged  DATE,
    FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id),
    FOREIGN KEY (ward_id)
        REFERENCES wards(ward_id)
);

-- 8. Patient Treatments
CREATE TABLE patient_treatments (
    treatment_id    BIGSERIAL    PRIMARY KEY,
    admission_id    BIGINT       NOT NULL,
    doctor_id       BIGINT       NOT NULL,
    treatment_date  DATE         NOT NULL,
    notes           TEXT,
    FOREIGN KEY (admission_id)
        REFERENCES patient_admissions(admission_id),
    FOREIGN KEY (doctor_id)
        REFERENCES doctors(employee_id)
);

-- 9. Patient Transfers
CREATE TABLE patient_transfers (
    transfer_id     BIGSERIAL    PRIMARY KEY,
    admission_id    BIGINT       NOT NULL,
    from_ward_id    INT          NOT NULL,
    to_ward_id      INT          NOT NULL,
    reason          VARCHAR(255) NOT NULL,
    FOREIGN KEY (admission_id)
        REFERENCES patient_admissions(admission_id),
    FOREIGN KEY (from_ward_id)
        REFERENCES wards(ward_id),
    FOREIGN KEY (to_ward_id)
        REFERENCES wards(ward_id)
);

-- 10. User Accounts
CREATE TABLE user_accounts (
    user_id        BIGSERIAL    PRIMARY KEY,
    employee_id    BIGINT       UNIQUE,
    username       VARCHAR(50)  NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    is_admin       BOOLEAN      NOT NULL DEFAULT FALSE,
    FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
);
