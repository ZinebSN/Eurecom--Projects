--2.4 Queries on raw data
--q1
select p as PublicationType, Count(k) as NumberOfPublications from Pub p group by p;

/*

 publicationtype |  NumberOfPublications
-----------------+-----------------------
 article         | 2685596
 book            |   19019
 incollection    |   67439
 inproceedings   | 2904087
 mastersthesis   |      12
 phdthesis       |   81781
 proceedings     |   48670
 www             | 2854130
(8 lignes)
*/

--q2
-- We have 8 types of publications, so we should have exactly 8 occurations, one for each type by using the distinct
select f.p as occuredFields from Field f inner join Pub p ON f.k=p.k group by f.p Having count(distinct p.p) =8;

/*
 occuredfields
---------------
 author
 ee
 note
 title
 year
(5 lignes)
*/