drop database test;
create database test;

use test;

create table student (
	idStudent int,
    firstName VARCHAR(50),
    middleName VARCHAR(50),
    lastName VARCHAR(50),
    studentNumber VARCHAR(10),
    studentEmail VARCHAR(200)
);

create table assessment (
	idCriteria int,
    idProject int,
    idStudent int,
    idMarker int,
    score double
);

create table selectedComment (
	idCriteria int,
    idProject int,
    idStudent int,
    idMarker int,
    idExpandedComment int
);

