create table teeth(
    teethiD int primary key ,
    leftlimit integer  ,
    rightlimit integer ,
    toplimit integer ,
    bottomlimit integer 
);


create table patients (
    patientsId VARCHAR(250) primary key
);

CREATE TABLE CONSULTATION(
    CONSULTATIONID serial primary key ,
    patientsId VARCHAR(250) ,
    BUDGET DOUBLE PRECISION ,
    DATECONSULTATION DATE DEFAULT(NOW()),
    foreign key (patientsId) REFERENCES PATIENTS(patientsId)
);

-- les dents malades du patient par consultation
CREATE TABLE CLIENT_TEETH_ANOMALY(
    CLIENT_TEETH_ANOMALY_ID serial primary key, 
    CONSULTATIONID INT , 
    TOOTHID integer , -- dent malade du client
    note integer not null, -- la note de son dent
    foreign key (CONSULTATIONID) REFERENCES CONSULTATION(CONSULTATIONID),
    foreign key (TOOTHID) REFERENCES teeth(teethiD)
);

-- ny reparation ana nify miankina @ : inona lay nify amboarina , inona no etat anle nify(izay hahazoana hoe
-- inona no type ana reparation mifandray aminy)
create table REPARATION(
    reparationid serial primary key ,
    teethiD int ,
    note integer DEFAULT(10),
    price double PRECISION ,
    nextreparation int REFERENCES reparation(reparationid),
     -- = quelle reparation il faut faire apres cette reparation
    foreign key(teethiD) REFERENCES teeth(teethiD)
);


create view v_reparation as select rp.* , reparation.note as childnote 
from reparation as rp left join reparation on rp.nextreparation = reparation.reparationid ;


create table areaconditionnote(
    areaconditionnoteid serial primary key ,
    note integer,
    areainf double PRECISION,
    areaSup double PRECISION
);


drop view v_reparation ;
drop table reparation ,CLIENT_TEETH_ANOMALY ,consultation ,patients, teeth ;