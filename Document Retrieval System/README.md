## CMPE493 - Information Retrieval Project 1

#### About Project
In this assignment, I have implemented a document retrieval system for simple boolean queries using the inverted indexing scheme. I have used Reuters-21578 data set. 

In data proccessing, firstly I read all the files in reuters21578 folder. For each of the new coming line I decide whether it is a new document, new title or new body. according to that information I took the part of the lines that contain tokens. For these tokens I firstly split them accoring to white space characters. 
After that point I normalize each token, and then lowercase them. For the linguistic operations I do stemming. Also before stemming I remove tokens if they are stopwords.

For keeping the inverted index, I use a tree map. First parameter of tree map is a TokenClass object which contains a term and its frequency. this is the dictionary part.
For the posting lists part, which is also second parameter of tree map, I use a tree set so that I take just unique document ids in an order.

#### Running the System
For running program you should double click the onurmusaoglu.exe file within the folder "Onur Musaoglu Project Exeutable".
This executable requires a corpus.txt and stopwords.txt file within the same folder.
Also if you want to re take and re corpus data you should put given reuters21578 folder within the same folder with executable.
For the sake of space, I do not include reuters21578 folder with submission because it is 26,4 MB.