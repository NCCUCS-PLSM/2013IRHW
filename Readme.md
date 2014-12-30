
#Information
* Introduction: This is the project of 2013 Information Retrieval class.
* Author: Veck Hsiao, Nick Cheng @ PLSM Lab, NCCU, Taipei, Taiwan
* Last Update: 2014/01/16 22:33
* Developing Platform:
	* Operating System: Linux Mint 13
	* JDK Version: 1.7.x
	* Lucene Version: 4.6.0
	* Tomcat Version: 7.0.x
	* Chinese Segmenter: mmseg4j 1.8.2

#File Structure
```
.
├── CDrawChart.java
├── Chinese.java
├── chineses/
├── CTermFrequence.java
├── DrawChart.java
├── HW.java
├── index
├── IndexFiles.java
├── index.jsp
├── Readme.md
├── SearchFiles.java
├── SegChinese.java
├── TermFrequence.java
├── TFTemp
│   └── README
└── WEB-INF
    ├── classes (Please compile and move class files manually)
    │   ├── CDrawChart.class
    │   ├── Chinese.class
    │   ├── CPair.class
    │   ├── CTermFrequence.class
    │   ├── dirPair.class
    │   ├── DrawChart.class
    │   ├── HW.class
    │   ├── IndexFiles.class
    │   ├── Pair.class
    │   ├── SearchFiles.class
    │   └── slate/
    ├── lib/
    └── web.xml        
```	

* English Part: 
	* HW.java - Main class of English query
	* DrawChart.java - Drawing chart for query with Google Chart API
	* IndexFiles.java - Indexing corpus with not yet segmented
	* TermFrequence.java - Counting term frequency for specific query
* Chinese Part:
	* Chinese.java
	* CDrawChart.java
	* SegChinese.java  - Chinese segmenter
	* CTermFrequency.java
* Others:
	* index.jsp - Homepage.
	* SearchFiles.java - Search file in index

#Before Using
1. Please copy or setup classpath of Java Compiler with JAR in WEB/lib.
2. Please compile following files:
	* Classess to be moved to WEB-INF/classes: 
		1. HW.java
		2. Chinese.java 
	* Classese to be use as Tools: IndexFiles.java, SegChinese.java
3. After compilation of IndexFiles.java, please index WEB-INF/classese/slate/* with IndexFiles: `java org.apache.lucene.demo.IndexFiles -index index -docs WEB-INF/classese/slate/*` or `java IndexFiles -index index -docs WEB-INF/classese/slate/*`.
4. After compilation of SegChinese.java, please segment Chinsese corpus with it. Default corpus are part chapters of `紅樓夢` and `西遊記`. When you finish segmenting, remember to put them in `chinese` folder(as dictionary).
5. Note that English implementation does not contain segmenter. It's only use indexer that lucenen offers. However, Chinese part uses a segmenter called mmseg4j and does not do indexing. 
6. Some path should be modified since they were hard-coded in this implementation:
	* SearchFiles.java: String index, filename
	* HW.java, Chinese.java: logger, HTML Output anchor
	* SegChinese.java: System.setProperty
7. Deploying with tomcat.

#How it Work
###English Query
0. Indexing corpus
1. Given a query
2. Searching file with index 
3. Computing the number of term of the query in each document
4. Sending result to Google Chart API and display the responsed chart

###Chinese Query
0. Segmenting corpus
1. Given a query 
2. Searching file with dictionary in chinese
3. Computing the number of term of the query in each document
4. Sending result to Google Chart API and display the responsed chart

#Issue
1. This project is considered to be rewritten in Python with  PyLucene(Indexer and Querier) and Jieba(Segmenter)
2. Should make a good new user interface for both Chinese and English search.
