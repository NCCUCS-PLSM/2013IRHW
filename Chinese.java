
import java.lang.*;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Calendar;
import java.util.Date;
import java.io.FileWriter;
import java.text.SimpleDateFormat;


import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Chinese extends HttpServlet 
{
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
           throws ServletException, IOException, NullPointerException
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter logger = new PrintWriter(new FileWriter("/opt/tomcat8/logs/clogger"+System.currentTimeMillis(), true));

        
        String queryString = request.getParameter("cwords");     // 取得輸入的查詢字串
        CTermFrequence ctf = new CTermFrequence();
  


        String filename = "", line = "", hitsCount = "", url = "";
        CPair tmp = new CPair("No files", 0);
        Map<String, CPair> map = new HashMap<String, CPair>();

        FileReader fr;
        BufferedReader br;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date current = new Date();

        try
        {
            /*查詢並取得三種的詞頻即Total*/
            map = ctf.call(queryString);

           /*取得全部後刪除這筆，因為Total 要在第1行列印，但在 Map 中未必是第1行，while 時不要走訪它*/
           tmp = map.get("total");     
           map.remove("total");

            /*呼叫 Google Char 繪圖*/
            CDrawChart chart = new CDrawChart();          // 建立 DrawChart 物件 (tmp)
            url = chart.draw(map);                  // 取得結合繪圖參數的 API Acess URL (tmp)

        }//end of try
        catch(Exception e)
        {
            logger.println(sdf.format(current)+": "+e.toString());

            StackTraceElement[] stack  = e.getStackTrace();
            for(StackTraceElement s: stack)
            {
                logger.println(s.toString()+"\n");
            }

        }

        logger.println(sdf.format(current)+": "+"Finished!");
        logger.close();

        /*HTML Output*/
        PrintWriter out = response.getWriter();     // 建立輸出物件
        Collection collection = map.values();
        Iterator iterator = collection.iterator();
        out.println("<html>");
        out.println("<body>");
        out.println("<a href='http://localhost:8080/2013IRHW'>回查詢頁</a><br>");
        out.println("<p>查詢字詞： "+queryString+"</p>");
        out.println("<p>總共找到： "+tmp.getCount() +"</p>");
        out.println("<p><img alt='Google Chart' src='"+ url +"'></p>");


        while(iterator.hasNext()) 
        {
           tmp = (CPair)iterator.next();
           out.println("<p>"+tmp.getNumber()+"： "+tmp.getCount()+"</p>");
        }
        
        out.println("</body>");
        out.println("</html>");
  }//end of doPost
}

