

--Create a Database
createdb dblp
PSQL dblp

-- Create the Publication Schema in SQL

CREATE TABLE Author (
		Id INT NOT NULL,
		Name TEXT NOT NULL,
		Homepage TEXT,
                Primary Key(Id)
		);

CREATE TABLE Publication (
		PubID INT NOT NULL,
		PubKey TEXT NOT NULL,
		Title TEXT,
		Year TEXT,
                Primary Key(PubID)
		);
		
CREATE TABLE Published (
		Id INT NOT NULL,
		PubID INT NOT NULL,
		PRIMARY KEY (Id, PubID),
                Foreign key(Id) REFERENCES Author (Id),
                Foreign Key(PubID) REFERENCES Publication (PubID)
		);
		
CREATE TABLE Article (
		PubID INT UNIQUE NOT NULL,
		Journal TEXT,
		Month TEXT,
		Volume TEXT,
		Number TEXT,
		PRIMARY KEY (PubID),
                Foreign Key(PubID) REFERENCES Publication (PubID)
		);
		
CREATE TABLE Book (
		PubID INT UNIQUE NOT NULL,
		Publisher TEXT,
		ISBN TEXT,
		PRIMARY KEY (PubID),
                Foreign Key(PubID) REFERENCES Publication (PubID)
		);
		
CREATE TABLE InCollection (
		PubID INT UNIQUE NOT NULL,
		BookTitle TEXT,
		Publisher TEXT,
		ISBN TEXT,
		PRIMARY KEY (PubID),
		Foreign Key(PubID) REFERENCES Publication (PubID));
		
CREATE TABLE InProceedings (
		PubID INT UNIQUE NOT NULL,
		BookTitle TEXT,
		Editor TEXT,
		PRIMARY KEY (PubID),
		Foreign Key(PubID) REFERENCES Publication (PubID));
		