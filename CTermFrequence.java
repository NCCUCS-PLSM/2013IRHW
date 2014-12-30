
import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class CTermFrequence{
    public Integer compute(String filename, String term) throws IOException
    {
        
/*TODO: if term is not a single word but a compound statement, call tokenizer again and return an object(Integer or List<String>)*/


		File f = new File(filename); 
		InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8"); 
		BufferedReader br=new BufferedReader(read);
		String str = "";
		List<String> listing = new ArrayList<String>();
		while((str = br.readLine()) != null)
		{		
			listing.add(str);
		}
        /*Do word count*/
        Map<String, Integer> map = new HashMap<String, Integer>();  
        
        final int COUNT = 1;            //declare a constant to initialize count of each word
        int accumulator = 0;
        Integer result;
        for(String data : listing)
        {
            if(data.equals(term))
            {
                if(map.get(data) == null)
                    map.put(data, COUNT);
                else if((accumulator = map.get(data)) != 0)   
                    map.put(data, ++accumulator);
            }
        }
        
        try{
            result = map.get(term);
            return result;
        }
        catch(NullPointerException e){
        /*TODO: Write error log (After class and deployment to wenshang.plsm)*/            
            return null;
        }
    }//end of function compute


	public Map<String, CPair> call(String term) throws IOException 
    {
		int c=0;
		CTermFrequence computer = new CTermFrequence();
		File dir = new File("/opt/tomcat-8.0.0/webapps/2013IRHW/chineses");
		File[] fList = dir.listFiles(); 
        int total = 0;

        Map<String, CPair> each = new HashMap<String, CPair>();
        String Corpus = "";
        int accumulator = 0;
        CPair tp;

        for (File file : fList)
        {
            if (file.isFile())
            {
			    try
                {
                    Corpus = file.getName();
				    c = computer.compute("/opt/tomcat-8.0.0/webapps/2013IRHW/chineses/"+Corpus, term);
                    if(c>0)
                    {
                        total=total + c;

                        /*把Corpus直接拿掉數字留下英文部份*/
                        if(Corpus.contains("red"))
                            Corpus = "紅樓夢";
                        else if(Corpus.contains("siyo"))
                            Corpus = "西遊記";
                        else if(Corpus.contains("three"))
                            Corpus = "三國演義";

                       if(each.get(Corpus) == null)
                       {
                            CPair cp = new CPair(Corpus, c);
                            each.put(Corpus, cp);
                       }
                       else
                       {
                            tp = each.get(Corpus);
                            accumulator = c + tp.getCount();
                            tp.setCount(accumulator);       
                            each.put(Corpus, tp);
                       }
                    }//end of if(c>0)
			    }//end of try
			    catch(NullPointerException e)
                {
                    				    
			    }
            }//end if(file.isFile())
        }//end of for
				   
        /*Output to files*/
        CPair summary = new CPair("total", total);
        each.put("total", summary);

        tp = null;

        return each;
	}//end of function call

}//end of class CTermFrequence


class CPair
{
    private String dirNumber;
    private int termCount;
    
    CPair(String dirNum, int termCnt)
    {
        this.dirNumber = dirNum;
        this.termCount = termCnt;
    }
    
    int getCount()
    {
        return this.termCount;
    }

    String getNumber()
    {
        return this.dirNumber;
    }

    void setCount(int termCnt)
    {
        this.termCount = termCnt;    
    }
    
}
