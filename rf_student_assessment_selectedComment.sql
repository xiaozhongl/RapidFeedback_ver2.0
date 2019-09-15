drop database test;
create database test;

use test;

create table student (
	idStudent int NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    middleName VARCHAR(50),
    lastName VARCHAR(50) NOT NULL,
    studentNumber VARCHAR(10) NOT NULL,
    studentEmail VARCHAR(200) NOT NULL,
    PRIMARY KEY (idStudent)
);

create table assessment (
	idCriteria int NOT NULL,
    idProject int NOT NULL,
    idStudent int NOT NULL,
    idMarker int NOT NULL,
    score double,
    PRIMARY KEY (idCriteria, idProject, idStudent, idMarker),
    FOREIGN KEY (idStudent) REFERENCES student (idStudent)
    /*FOREIGN KEY (idMarker) REFERENCES marker (id),
    FOREIGN KEY (idProject, idCriteria) REFERENCES ProjectSetCriteria (idProject, idCriteria)*/
);

create table selectedComment (
	idCriteria int NOT NULL,
    idProject int NOT NULL,
    idStudent int NOT NULL,
    idMarker int NOT NULL,
    idExpandedComment int NOT NULL,
    PRIMARY KEY (idCriteria, idProject, idStudent, idMarker, idExpandedComment),
    FOREIGN KEY (idCriteria, idProject, idStudent, idMarker) REFERENCES assessment (idCriteria, idProject, idStudent, idMarker)
);

