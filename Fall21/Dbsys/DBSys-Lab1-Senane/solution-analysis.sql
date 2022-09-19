--4 Data Analysis
--4.1 Queries

--q1
--Find the top 20 authors with the largest number of publications

SELECT a.ID, Name, Count(pubid) as NumPublications
	FROM Author AS a INNER JOIN published AS p ON a.ID = p.ID
        Group by a.id
        Order by NumPublications desc
        limit 20;

/*
   id    |         name         | numpublications
---------+----------------------+-----------------
 2017054 | H. Vincent Poor      |            2316
 2200247 | Mohamed-Slim Alouini |            1765
 2588699 | Philip S. Yu         |            1630
 2305992 | Wei Zhang            |            1591
 1730155 | Wei Wang             |            1560
  302415 | Yu Zhang             |            1503
 2852511 | Lajos Hanzo          |            1485
  572285 | Yang Liu             |            1480
  533441 | Lei Zhang            |            1386
  138012 | Xin Wang             |            1336
  242670 | Lei Wang             |            1336
 2250762 | Zhu Han              |            1335
  152146 | Victor C. M. Leung   |            1322
 2229333 | Wen Gao 0001         |            1316
 2790533 | Dacheng Tao          |            1277
  167510 | Hai Jin 0001         |            1273
  532689 | Witold Pedrycz       |            1262
 1833894 | Wei Li               |            1249
  737600 | Jun Wang             |            1228
    8360 | Luca Benini          |            1198
(20 lignes)*/




-- q2. 
-- Find the top 20 authors with the largest number of publications in STOC. 

SELECT a.ID, Name, COUNT(p.PubID) as NumPublicationsInStoc
	 FROM Author as a INNER JOIN published as p  ON a.ID = p.ID
         inner join Publication as pub on p.pubid=pub.pubid
         inner join Field as f on pub.pubkey=f.k
         where f.p = 'booktitle' AND f.v LIKE '%STOC%'
         GROUP BY a.ID
	 ORDER BY NumPublicationsInStoc DESC
	 LIMIT 20;

/*

   id    |           name            | numpublicationsinstoc
---------+---------------------------+-----------------------
 1737355 | Avi Wigderson             |                    58
 1773848 | Robert Endre Tarjan       |                    33
  964104 | Ran Raz                   |                    30
 2611527 | Moni Naor                 |                    29
  545234 | Noam Nisan                |                    29
 1470999 | Uriel Feige               |                    28
 2666604 | Santosh S. Vempala        |                    27
 1382868 | Rafail Ostrovsky          |                    27
 2292358 | Venkatesan Guruswami      |                    26
 1501136 | Mihalis Yannakakis        |                    26
 2315798 | Oded Goldreich 0001       |                    25
 1464395 | Frank Thomson Leighton    |                    25
 1205565 | Prabhakar Raghavan        |                    24
 2062784 | Christos H. Papadimitriou |                    24
 1073086 | Mikkel Thorup             |                    24
 1404390 | Moses Charikar            |                    23
 1740185 | Yin Tat Lee               |                    23
 1827900 | Madhu Sudan               |                    22
  721173 | Rocco A. Servedio         |                    22
 2785071 | Noga Alon                 |                    22
(20 lignes) */


-- Find the top 20 authors with the largest number of publications in SIGMOD. 

			
SELECT a.ID, Name, COUNT(p.PubID) as NumPublicationsInSigmod
	 FROM Author as a INNER JOIN published as p  ON a.ID = p.ID
         inner join Publication as pub on p.pubid=pub.pubid
         inner join Field as f on pub.pubkey=f.k
         WHERE f.p = 'booktitle' AND f.v LIKE '%SIGMOD%'
         GROUP BY a.ID
	 ORDER BY NumPublicationsInSigmod DESC
	 LIMIT 20;

/*

   id    |         name          | numpublicationsinsigmod
---------+-----------------------+-------------------------
 1478233 | Surajit Chaudhuri     |                      59
 1523021 | Divesh Srivastava     |                      58
 1482326 | H. V. Jagadish        |                      53
 2649101 | Jeffrey F. Naughton   |                      47
 1800769 | Michael J. Carey 0001 |                      46
 1493784 | Michael J. Franklin   |                      46
 1223000 | Michael Stonebraker   |                      45
  467571 | Beng Chin Ooi         |                      44
 2899425 | Jiawei Han 0001       |                      43
 2700169 | Tim Kraska            |                      42
  516215 | Samuel Madden         |                      41
 2698464 | David J. DeWitt       |                      40
  367111 | Donald Kossmann       |                      39
  709910 | Johannes Gehrke       |                      39
  665778 | Hector Garcia-Molina  |                      38
  844221 | Dan Suciu             |                      38
  860597 | Joseph M. Hellerstein |                      38
  767757 | Raghu Ramakrishnan    |                      37
  369880 | Guoliang Li 0001      |                      36
 1346685 | Kian-Lee Tan          |                      34
(20 lignes) */

--q3
--  (a) Find all authors who published at least 10 SIGMOD papers but never published a PODS paper

			 
CREATE VIEW PODS AS (SELECT p.ID, COUNT(p.PubID) AS NumPublicationsOfPods
			FROM published AS p
			INNER JOIN Publication AS pub ON p.PubID = pub.PubID
			INNER JOIN Field AS f ON pub.PubKey = f.k
                        WHERE f.p = 'booktitle' AND f.v LIKE '%PODS%'		        
                        GROUP BY p.ID
			ORDER BY NumPublicationsOfPods DESC);

CREATE VIEW SIGMOD AS (SELECT p.ID, COUNT(p.PubID) AS NumPublicationsOfSigmod
		        FROM published AS p
			INNER JOIN Publication AS pub ON p.PubID = pub.PubID
			INNER JOIN Field AS f ON pub.PubKey = f.k
                        WHERE f.p = 'booktitle' AND f.v LIKE '%SIGMOD%'
			GROUP BY p.ID
			ORDER BY NumPublicationsOfSigmod DESC);

SELECT s.ID, a.Name, s.NumPublicationsOfSigmod
	FROM PODS AS p 
	FULL JOIN SIGMOD AS s ON p.ID = s.ID
	LEFT JOIN Author AS a ON s.ID = a.ID
	WHERE p.NumPublicationsOfPods IS NULL AND s.NumPublicationsOfSigmod >= 10
	ORDER BY s.NumPublicationsOfSigmod DESC;

/* 

   id    |           name            | numpublicationsofsigmod
---------+---------------------------+-------------------------
 2899425 | Jiawei Han 0001           |                      43
 2700169 | Tim Kraska                |                      42
  367111 | Donald Kossmann           |                      39
  369880 | Guoliang Li 0001          |                      36
  348189 | Carsten Binnig            |                      33
  239277 | Elke A. Rundensteiner     |                      31
 1436070 | Jeffrey Xu Yu             |                      31
 1832277 | Feifei Li 0001            |                      31
 1379038 | Xiaokui Xiao              |                      30
 1960172 | Volker Markl              |                      28
  411398 | Stratos Idreos            |                      27
 1346495 | Bin Cui 0001              |                      26
 2199628 | Alfons Kemper             |                      24
 1496562 | Juliana Freire            |                      24
  599215 | Jignesh M. Patel          |                      22
  959232 | Ihab F. Ilyas             |                      22
  591485 | Eugene Wu 0002            |                      21
   48812 | Anthony K. H. Tung        |                      21
  896634 | Nan Tang 0001             |                      21
  828899 | Gao Cong                  |                      20
 2533970 | Arun Kumar 0001           |                      20
 2302736 | Sihem Amer-Yahia          |                      20
 2772440 | Jian Pei                  |                      19
   83675 | AnHai Doan                |                      19
  505152 | Jun Yang 0001             |                      19
  151646 | Andrew Pavlo              |                      19
  217667 | Mourad Ouzzani            |                      19
 1100763 | David B. Lomet            |                      18
 2741122 | Kevin Chen-Chuan Chang    |                      18
  854378 | Jim Gray 0001             |                      18
 1902972 | Wook-Shin Han             |                      17
 1709598 | Badrish Chandramouli      |                      17
 1196340 | Barzan Mozafari           |                      17
 1592587 | Guy M. Lohman             |                      17
 2499983 | Daniel J. Abadi           |                      17
  432051 | Krithi Ramamritham        |                      16
 1061091 | Louiqa Raschid            |                      16
 2403522 | Hans-Arno Jacobsen        |                      16
 2101393 | Bingsheng He              |                      16
   20236 | Aditya G. Parameswaran    |                      16
  738458 | Jiannan Wang              |                      15
 2308443 | Nick Roussopoulos         |                      15
  154434 | Stanley B. Zdonik         |                      15
  633423 | James Cheng               |                      15
 2024638 | Gang Chen 0001            |                      14
   10882 | Lu Qin                    |                      14
   33688 | Suman Nath                |                      14
  807657 | Dirk Habich               |                      14
  887172 | Ugur Ãetintemel           |                      14
 1138818 | Raymond Chi-Wing Wong     |                      14
 1664556 | Jingren Zhou              |                      14
 1912031 | Ioana Manolescu           |                      14
 2185735 | Kaushik Chakrabarti       |                      14
 2552541 | Ahmed K. Elmagarmid       |                      14
 1884106 | Kevin S. Beyer            |                      13
 2559031 | Aaron J. Elmore           |                      13
 2032727 | Wei Wang 0011             |                      13
 1045412 | Tilmann Rabl              |                      12
 1028384 | Xifeng Yan                |                      12
  394785 | Torsten Grust             |                      12
  542351 | Jianhua Feng              |                      12
  980665 | Alvin Cheung              |                      12
  546941 | Yinghui Wu                |                      12
  922337 | Nicolas Bruno             |                      12
 1440975 | Jayavel Shanmugasundaram  |                      12
 2384001 | Goetz Graefe              |                      12
 2009048 | Sudipto Das               |                      12
 2335067 | Carlo Curino              |                      12
  590910 | Jorge-Arnulfo QuianÚ-Ruiz |                      12
  172002 | Ashraf Aboulnaga          |                      12
  273763 | Immanuel Trummer          |                      12
 1098291 | Michael J. Cafarella      |                      12
 1234173 | Sanjay Krishnan           |                      12
 1519885 | Xiaofang Zhou 0001        |                      11
 1678272 | Xu Chu                    |                      11
 1695174 | Luis Gravano              |                      11
 1049123 | Peter Bailis              |                      11
 1739574 | Lijun Chang               |                      11
 2849925 | Mohamed F. Mokbel         |                      11
  978073 | Clement T. Yu             |                      11
  857571 | Nan Zhang 0004            |                      11
  775044 | Jens Teubner              |                      11
 2180862 | Cong Yu 0001              |                      11
  671869 | Carlos Ordonez 0001       |                      11
 2261998 | Christian S. Jensen       |                      11
 2296485 | Ce Zhang 0001             |                      11
  429994 | Zhifeng Bao               |                      11
  161060 | Bolin Ding                |                      11
 1454638 | Vasilis Vassalos          |                      11
 1402062 | Viktor Leis               |                      11
 1335807 | Fatma Ízcan               |                      11
  787775 | K. Selþuk Candan          |                      10
  848894 | Theodoros Rekatsinas      |                      10
 2350099 | Rajasekar Krishnamurthy   |                      10
  885908 | Boris Glavic              |                      10
 1866641 | Zhenjie Zhang             |                      10
 2505311 | Chee Yong Chan            |                      10
 1021265 | Abolfazl Asudeh           |                      10
 1098552 | Bruce G. Lindsay 0001     |                      10
 1259693 | Sebastian Schelter        |                      10
 1100759 | Sailesh Krishnamurthy     |                      10
 2702687 | Meihui Zhang              |                      10
 1697643 | Jianliang Xu              |                      10
  734494 | Qiong Luo 0001            |                      10
  176646 | Yinan Li                  |                      10
 2885991 | Byron Choi                |                      10
 2101253 | Arash Termehchy           |                      10
 2203586 | Shuigeng Zhou             |                      10
 1154555 | JosÚ A. Blakeley          |                      10
 2251278 | Themis Palpanas           |                      10
 1437778 | Chengkai Li               |                      10
 1165298 | Eric Lo 0001              |                      10
(112 lignes) */

--  (b) Find all authors who published at least 5 PODS papers but never published a SIGMOD paper

SELECT p.ID, a.Name, p.NumPublicationsOfPods
	FROM Sigmod AS s 
		FULL JOIN PODS AS p ON p.ID = s.ID
		LEFT JOIN Author AS a ON p.ID = a.ID
	WHERE s.NumPublicationsOfSigmod IS NULL AND p.NumPublicationsOfPods >= 5
	ORDER BY p.NumPublicationsOfPods DESC;

Drop view sigmod;
Drop view pods;

/* 
   id    |          name           | numpublicationsofpods
---------+-------------------------+-----------------------
 2213866 | David P. Woodruff       |                    16
  446570 | Andreas Pieris          |                    13
  287152 | Thomas Schwentick       |                    12
  325958 | Rasmus Pagh             |                    11
 1664700 | Nicole Schweikardt      |                    11
 1154926 | Reinhard Pichler        |                    10
  506963 | Martin Grohe            |                     9
 1008650 | Giuseppe De Giacomo     |                     9
 1087579 | Stavros S. Cosmadakis   |                     8
 1913892 | Jef Wijsen              |                     7
 2120359 | Eljas Soisalon-Soininen |                     7
 1417248 | Kobbi Nissim            |                     6
  936492 | Francesco Scarcello     |                     6
  498027 | Cristian Riveros        |                     6
  686086 | Marco A. Casanova       |                     5
 2312747 | Domagoj Vrgoc           |                     5
  689163 | Kari-Jouko Rõihõ        |                     5
 2533850 | Vassos Hadzilacos       |                     5
 1389717 | Michael Mitzenmacher    |                     5
  996317 | Matthias Niewerth       |                     5
 1343604 | Miguel Romero 0001      |                     5
 1063414 | Marco Console           |                     5
 2618089 | Mikolaj Bojanczyk       |                     5
 1139085 | Nofar Carmeli           |                     5
  630963 | Alan Nash               |                     5
 2126531 | Srikanta Tirthapura     |                     5
 2134071 | Hubie Chen              |                     5
 2198120 | Nancy A. Lynch          |                     5
(28 lignes) */


-- q4. For each decade, compute the total number of publications in DBLP in that decade.

CREATE TABLE tempYear (
		Year INT,
		NumOfPublications INT
		);
INSERT INTO tempYear (
		SELECT CAST(Year AS INT), COUNT(PubKey)
			FROM Publication
			WHERE Year IS NOT NULL
			GROUP BY Year);

SELECT t1.Year AS StartYearOfDecade, SUM(t2.NumOfPublications) as NumberOfPublications
	FROM tempYear AS t1, tempYear AS t2
	WHERE t1.Year <= t2.Year AND t2.Year < t1.Year + 10 
        GROUP BY t1.Year
	ORDER BY t1.Year;

DROP TABLE tempYear;
/*
 startyearofdecade | numberofpublications
-------------------+----------------------
              1936 |                  113
              1937 |                  132
              1938 |                  127
              1939 |                  157
              1940 |                  191
              1941 |                  207
              1942 |                  234
              1943 |                  330
              1944 |                  489
              1945 |                  694
              1946 |                  888
              1947 |                 1199
              1948 |                 1525
              1949 |                 1935
              1950 |                 2583
              1951 |                 3103
              1952 |                 3941
              1953 |                 4985
              1954 |                 5819
              1955 |                 6673
              1956 |                 7707
              1957 |                 8803
              1958 |                10148
              1959 |                11805
              1960 |                13104
              1961 |                14608
              1962 |                16508
              1963 |                18838
              1964 |                21839
              1965 |                25449
              1966 |                28947
              1967 |                32914
              1968 |                36938
              1969 |                41294
              1970 |                45731
              1971 |                51006
              1972 |                56028
              1973 |                61571
              1974 |                67580
              1975 |                74153
              1976 |                81948
              1977 |                91703
              1978 |               101909
              1979 |               115072
              1980 |               130680
              1981 |               149186
              1982 |               170332
              1983 |               194129
              1984 |               222289
              1985 |               253427
              1986 |               285899
              1987 |               320561
              1988 |               358296
              1989 |               399904
              1990 |               445279
              1991 |               495180
              1992 |               547869
              1993 |               607439
              1994 |               678311
              1995 |               764575
              1996 |               869959
              1997 |               989896
              1998 |              1120239
              1999 |              1255512
              2000 |              1403728
              2001 |              1548093
              2002 |              1707878
              2003 |              1869474
              2004 |              2031018
              2005 |              2182779
              2006 |              2321894
              2007 |              2452821
              2008 |              2594235
              2009 |              2756317
              2010 |              2939720
              2011 |              3130211
              2012 |              3172199
              2013 |              2919281
              2014 |              2648694
              2015 |              2368284
              2016 |              2078783
              2017 |              1778097
              2018 |              1452694
              2019 |              1094105
              2020 |               695067
              2021 |               284479
              2022 |                 1071
(87 lignes) */


