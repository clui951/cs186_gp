/* Test Rollback and Recovery Here */

set transaction read write;

insert into data (data.f1, data.f2) values (6, 60);
insert into data (data.f1, data.f2) values (10, 33);
insert into s (s.sid, s.age, s.name) values (6, 6, 'Michael');
insert into s (s.sid, s.age, s.name) values (7, 60, 'Michelle');
update data set data.f2=1 where data.f1=1;

### flush;
rollback; 


/* update table and end transaction above */

select * from data;
select * from s;