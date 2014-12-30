
import com.googlecode.charts4j.*;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.UrlUtil;
import com.googlecode.charts4j.LinearGradientFill;
import static com.googlecode.charts4j.Color.*;

import java.util.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class DrawChart 
{
    public String draw(Map<Integer, dirPair> map) 
    {
        Collection collection = map.values();
        Iterator iterator = collection.iterator();
        dirPair pair;
        int count=0, max = 0;    // for LinearGradienFill (set the maximum bound) 
        List<BarChartPlot> list = new ArrayList<BarChartPlot>();

        while(iterator.hasNext()) {
            pair = (dirPair)iterator.next();
            count = pair.getCount();
            BarChartPlot document = Plots.newBarChartPlot(Data.newData(count), LIMEGREEN, "Directory:" + String.valueOf(pair.getNumber()));
            list.add(document);

            if(count > max)
                max = count;
        }


        // Instantiating chart.
        BarChart chart = GCharts.newBarChart(list);

        // Defining axis info and styles
        AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 13, AxisTextAlignment.CENTER);
        AxisLabels freq = AxisLabelsFactory.newAxisLabels("Frequence", 5);
        freq.setAxisStyle(axisStyle);
        AxisLabels dir = AxisLabelsFactory.newAxisLabels("Subdirectory of slate", 5);
        dir.setAxisStyle(axisStyle);

        // Adding axis info to chart.        
        /*Set the maximum bound*/
        int bound = 0;    
        if(max < 10)
            bound = 10;
        else if(max < 50 && max >=10)
            bound = 50;
        else if(max < 100 && max >= 50)
            bound = 100;
        else if(max < 500 && max >= 100)
            bound = 500;
        else
            bound = 1000;
        chart.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, bound));
        chart.addYAxisLabels(freq);
        chart.addXAxisLabels(dir);

        chart.setSize(600, 450);    //不會調整？？
        chart.setBarWidth(10);
        chart.setSpaceWithinGroupsOfBars(10);
        chart.setTitle("Document Frequence", BLACK, 16);
        chart.setGrid(100, 10, 3, 2);
        chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));
    
        LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
        fill.addColorAndOffset(WHITE, 0);
        chart.setAreaFill(fill);
        String url = chart.toURLString();
        // EXAMPLE CODE END. Use this url string in your web or
        // Internet application.

        return url;
    }
}
