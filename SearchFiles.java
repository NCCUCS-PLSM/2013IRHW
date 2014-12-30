
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.MultiFields;
/*import org.apache.lucene.index.StoredDocument;  //It seems to be deprecated in version 4.6.0*/
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.*;

/** Simple command-line based search demo. */
public class SearchFiles 
{
  public String doQuery(String line) throws Exception 
  {

    String index = "/opt/tomcat-8.0.0/webapps/2013IRHW/index";
    String field = "contents";
    String filename = "/opt/tomcat-8.0.0/webapps/2013IRHW/TFTemp/term_frequence_"+System.currentTimeMillis();
    FileWriter fw = new FileWriter(filename, true);  // Create output file, true for append mode
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46); // :Post-Release-Update-Version.LUCENE_XY:
    QueryParser parser = new QueryParser(Version.LUCENE_46, field, analyzer);    // :Post-Release-Update-Version.LUCENE_XY:


    /*NOTE: line ==  null 會在前端用 javascript 處理*/
    line = line.trim();   //eliminate the space in the end of input string 
     
    Query query = parser.parse(line);     // create the querier of input string with parser

    BufferedReader in =  new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

    doPagingSearch(in, searcher, query, fw);
       
    fw.close();
    fw = null;
    
    reader.close();
    
    return filename;
  }// end of do Query function



  public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, FileWriter fw) throws IOException 
  {
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 4535);     //Total number of text files is 4532
    ScoreDoc[] hits = results.scoreDocs;

    int numTotalHits = results.totalHits;
    String docFreq = "Total matching documents:" + numTotalHits + "\n"; 
    fw.write(docFreq);

    int start = 0;
    int end = Math.min(numTotalHits, 4535);
     
      
      for (int i = start; i < end; i++) 
      {
 
        Document doc = searcher.doc(hits[i].doc);   //hits[i].doc returns the hit doc num
        String path = doc.get("path");

        if (path != null) 
        {
            /*DEBUG: Get the context and compute term frequence of each file*/
            TermFrequence tf = new TermFrequence();
            Integer count = tf.compute(doc.get("context"), query.toString("contents")); //TODO: 之後加入句子查詢功能要改成 Object obj = tf.compute();
            if(count != null)   //output to specified file
            {
                fw.write(path+":"+ count.toString() +"\n");
                count = null;
            }
            tf = null;              
        }                          
      }//end of for
  }//end of function doPagingSearch
}//end of class


