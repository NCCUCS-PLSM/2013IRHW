
import java.io.*;
import java.lang.*;
import java.util.*;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TermFrequence
{
    public Integer compute(String context, String term) throws IOException
    {
        TermTokenizer tknzr = new TermTokenizer();
        SimpleAnalyzer sa = new SimpleAnalyzer(Version.LUCENE_46);

/*TODO: if term is not a single word but a compound statement, call tokenizer again and return an object(Integer or List<String>)*/

        /*Call tokenizer doing string tokenization*/
        List<String> listing = tknzr.tokenizeString(sa, context);

        /*Do word count*/
        Map<String, Integer> map = new HashMap<String, Integer>();  
        /*
         * 因為 Map 無法取得 Key 的值，只有 value，所以 value 做成一個自定義的類別 Pair，含有詞和詞頻
         * Note: can't use primitive type int as second parameter type in Map
         */
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
}//end of class TermFrequence

final class TermTokenizer
{
    public TermTokenizer() {}

    public static List<String> tokenizeString(SimpleAnalyzer analyzer, String string) 
    {
        List<String> result = new ArrayList<String>();
        try{
            TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
            stream.reset();
             while(stream.incrementToken()) 
                result.add(stream.getAttribute(CharTermAttribute.class).toString());            
        }
        catch(IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }

        return result;
    }  
}

