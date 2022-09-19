--3 Data transformation
-- Firt we will create temporary tables for each information

create table tempJournal(PUBKEY TEXT, Journal text);

create table tempMonth(PUBKEY TEXT, month text);

create table tempVolume(PUBKEY TEXT, volume text);

create table tempNumber(PUBKEY TEXT, number text);

create table tempName(PUBKEY TEXT, Name text);

create table tempHomepage(Name TEXT, Homepage text);

create table tempPublisher(PUBKEY TEXT, publisher text);

create table tempIsbn(PUBKEY TEXT, isbn text);

create table tempBooktitle(PUBKEY TEXT, Booktitle text);

create table tempEditor(PUBKEY TEXT, editor text);

create table tempTitle(PUBKEY TEXT, title text);

create table tempYear(PUBKEY TEXT, Year text);

-- We fill these temporary tables using field and pub tables 

INSERT INTO tempName (SELECT k, v FROM Field WHERE p = 'author');
	

-- While using direct insert for other temporary tables i had errors showing that there are many pubkey duplicated, so we need to make partition on pubkeys 
-- and then take only one raw of duplicated pubkeys

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, f.k, v FROM Field f WHERE p = 'title')
INSERT INTO tempTitle (SELECT tab.k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'year')
INSERT INTO tempYear (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'journal')
INSERT INTO tempJournal (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'publisher')
INSERT INTO tempPublisher (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'month')
INSERT INTO tempMonth (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'volume')
INSERT INTO tempVolume (SELECT k, v FROM tab WHERE r = 1);
	
WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'number')
INSERT INTO tempNumber (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'isbn')
INSERT INTO tempISBN (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'booktitle')
INSERT INTO tempBookTitle (SELECT k, v FROM tab WHERE r = 1);

WITH tab AS (SELECT ROW_NUMBER() OVER (PARTITION BY k) AS r, k, v FROM Field WHERE p = 'editor')
INSERT INTO tempEditor (SELECT k, v FROM tab WHERE r = 1);


CREATE SEQUENCE sequenceAuthor;
CREATE SEQUENCE sequencePublication;

WITH tab AS (SELECT row_number() over (partition BY tN.Name) AS r, tN.Name, f.v AS hp
				FROM tempName tN INNER JOIN Field f ON tN.PubKey = f.k
				WHERE f.k like 'homepage%' and f.p = 'url'  )
INSERT INTO tempHomepage (SELECT Name, hp FROM tab WHERE tab.r = 1);

--Populate the tables of pubSchema

INSERT INTO Author (
	SELECT NEXTVAL('sequenceAuthor'), tN.Name, th.Homepage
		FROM (SELECT DISTINCT Name FROM tempName ) AS tN 
				LEFT JOIN tempHomepage AS tH ON tN.Name = tH.Name
	);

drop sequence seqAuthor;


INSERT INTO Publication (
	SELECT NEXTVAL('sequencePublication'), p.k, tT.Title, tY.Year
		FROM Pub AS p 
	        LEFT JOIN tempTitle AS tT ON p.k = tt.PubKey
		LEFT JOIN tempYear AS tY ON p.k = ty.PubKey
		WHERE p.p in ('article', 'book', 'inproceedings', 'incollection')
	);

drop sequence seqPublication;

insert into Article (
	SELECT p.PubID, tJ.Journal, tM.Month, tV.Volume, tN.Number
		FROM Publication AS p
			LEFT JOIN tempJournal AS tj ON p.PubKey = tj.PubKey
			LEFT JOIN tempMonth AS tm ON p.PubKey = tm.PubKey
			LEFT JOIN tempVolume AS tv ON p.PubKey = tv.PubKey
			LEFT JOIN tempNumber AS tn ON p.PubKey = tn.PubKey
		        JOIN Pub on p.PubKey=Pub.k
			WHERE Pub.p = 'article'		
 	);

INSERT INTO Book (
	SELECT p.PubID, tp.Publisher, ti.ISBN
		FROM Publication AS p
			LEFT  JOIN tempPublisher AS tp ON p.PubKey = tp.PubKey
			LEFT  JOIN tempISBN AS ti ON p.PubKey = ti.PubKey
		        JOIN Pub on p.PubKey=Pub.k
			WHERE Pub.p = 'book' and p.PubID not in (11530,32741,32655,32748)	
	);

INSERT INTO InCollection (
	SELECT p.PubID, tb.BookTitle, tp.Publisher, ti.ISBN
		FROM Publication AS p
			LEFT JOIN tempBookTitle AS tb ON p.PubKey = tb.PubKey
			LEFT JOIN tempPublisher AS tp ON p.PubKey = tp.PubKey
			LEFT JOIN tempISBN AS ti ON p.PubKey = ti.PubKey
		        JOIN Pub on Pub.K=p.PubKey
			WHERE Pub.p = 'incollection'	
	);
	
INSERT INTO InProceedings (
	SELECT p.PubID, tb.BookTitle, te.Editor
		FROM Publication AS p
			LEFT JOIN tempBookTitle AS tb ON p.PubKey = tb.PubKey
			LEFT JOIN tempEditor AS te ON p.PubKey = te.PubKey
		        JOIN Pub on Pub.k=p.PubKey
			WHERE Pub.p = 'inproceedings'	
	);

-- To populate published table we need to drop foreign and primary key constraints first to speed up the transformation

alter table published drop constraint published_pkey Primary Key (id,pubid);
alter table published drop constraint published_fkey_id Foreign Key (id) references author(id);
alter table published drop constraint published_fkey_pubid Foreign Key (pubid) references publication(pubid);


INSERT INTO Published (
	SELECT DISTINCT a.ID, p.PubID
		FROM tempName AS tN 
			INNER JOIN Author AS a on tN.Name = a.Name
			INNER JOIN Publication AS p ON tN.PubKey = p.PubKey
	);

-- adding the constraints using alter table
alter table published add constraint published_pkey Primary Key (id,pubid);
alter table published add constraint published_fkey_id Foreign Key (id) references author(id);
alter table published add constraint published_fkey_pubid Foreign Key (pubid) references publication(pubid);

--Drop all temporary tables

DROP TABLE tempName;
DROP TABLE tempTitle;
DROP TABLE tempYear;
DROP TABLE tempJournal;
DROP TABLE tempPublisher;
DROP TABLE tempMonth;
DROP TABLE tempVolume;
DROP TABLE tempNumber;
DROP TABLE tempISBN;
DROP TABLE tempBookTitle;
DROP TABLE tempEditor;



