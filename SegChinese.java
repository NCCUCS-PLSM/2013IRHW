import java.io.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class SegChinese { 
	protected Dictionary dic;
	
	public SegChinese() {
		System.setProperty("mmseg.dic.path", "./src/HelloChinese/data");
		dic = Dictionary.getInstance();
	}

	protected Seg getSeg() {
		return new ComplexSeg(dic);
	}
	
	public String segWords(String txt, String wordSpilt) throws IOException {
		Reader input = new StringReader(txt);
		StringBuilder sb = new StringBuilder();
		Seg seg = getSeg();
		MMSeg mmSeg = new MMSeg(input, seg);
		Word word = null;
		boolean first = true;
		while((word=mmSeg.next())!=null) {
			if(!first) {
				sb.append(wordSpilt);
			}
			String w = word.getString();
			sb.append(w);
			first = false;
			
		}
		return sb.toString();
	}
	
	protected String run(String args) throws IOException {

		return segWords(args, "\n");
	}
	
	public static void main(String[] args) throws IOException {				
		
		File f = new File("three2.txt"); 
		InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8"); 
		BufferedReader br=new BufferedReader(read); 
		String c = "";
		String str = "";
		while((c = br.readLine()) != null)
		{
			str = str + c;
		}
		SegChinese seg = new SegChinese();
		String word = seg.run(str);
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("out_three2.txt"),"UTF-8");
		out.write(word);
		out.close();
	}
}


