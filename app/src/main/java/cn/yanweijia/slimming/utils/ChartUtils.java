package cn.yanweijia.slimming.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by weijia on 16/10/2017.
 *
 * @author weijia
 */

public class ChartUtils {

    /**
     * draw a Line Chart
     *
     * @param context          Application/Activity/View Context
     * @param lineChart        chart View
     * @param chartTitle       title,useless
     * @param desc             chart description ,right bottom
     * @param descriptionColor description color
     * @param values           chart sequence values
     * @param titles           chart sequence title
     * @param colors           chart sequence color
     * @param formatter        nullable, xValuesFormatter: display 'x axis unit' on x axis
     * @author weijia
     */
    public static boolean drawLineChart(Context context, LineChart lineChart, String chartTitle, String desc, Integer descriptionColor, List<ArrayList<Entry>> values, List<String> titles, List<Integer> colors, IAxisValueFormatter formatter) {
        lineChart.clear();
        if (null == lineChart || null == values || values.size() == 0) {
            return false;
        }
        for (ArrayList arrayList : values) {
            if (arrayList.size() == 0) {
                return false;
            }
        }
        //创建描述信息
        Description description = new Description();
        description.setText(desc);
        if (null == descriptionColor)
            descriptionColor = Color.GRAY;
        description.setTextColor(descriptionColor);
        description.setTextSize(20);
        lineChart.setDescription(description);//设置图表描述信息
        lineChart.setNoDataText("No chart data available.");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
        //lineChart.setBorderColor(); //设置 chart 边框线的颜色。
        //lineChart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
        //lineChart.setLogEnabled(true);//打印日志
        //lineChart.notifyDataSetChanged();//刷新数据
        //lineChart.invalidate();//重绘
        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            //LineDataSet每一个对象就是一条连接线
            LineDataSet lineDataSet;
            //设置数据1  参数1：数据源 参数2：图例名称
            lineDataSet = new LineDataSet(values.get(i), titles.get(i));
            lineDataSet.setColor(colors.get(i));
            lineDataSet.setCircleColor(colors.get(i));
            lineDataSet.setLineWidth(1f);//设置线宽
            lineDataSet.setCircleRadius(3f);//设置焦点圆心的大小
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            lineDataSet.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
            lineDataSet.setHighlightEnabled(true);//是否禁用点击高亮线
//            lineDataSet.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
            lineDataSet.setValueTextSize(9f);//设置显示值的文字大小
            lineDataSet.setDrawFilled(false);//设置禁用范围背景填充

            //格式化显示数据
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            lineDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });

            //设置范围背景填充
            lineDataSet.setFillColor(Color.BLACK);

            dataSets.add(lineDataSet);
        }
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        lineChart.setData(data);
        //绘制图表
        lineChart.invalidate();
        //获取此图表的x轴
        XAxis xAxis = lineChart.getXAxis();
        if (formatter != null)
            xAxis.setValueFormatter(formatter);
        xAxis.setValueFormatter(formatter);
        xAxis.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //设置竖线的显示样式为虚线
        //lineLength控制虚线段的长度
        //spaceLength控制线之间的空间
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        xAxis.setAxisMinimum(0f);//设置x轴的最小值
//        xAxis.setAxisMaximum(10f);//设置最大值
        xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setLabelRotationAngle(10f);//设置x轴标签的旋转角度
//        设置x轴显示标签数量  还有一个重载方法第二个参数为布尔值强制设置数量 如果启用会导致绘制点出现偏差
//        xAxis.setLabelCount(10);
//        xAxis.setTextColor(Color.BLUE);//设置轴标签的颜色
//        xAxis.setTextSize(24f);//设置轴标签的大小
//        xAxis.setGridLineWidth(10f);//设置竖线大小
//        xAxis.setGridColor(Color.RED);//设置竖线颜色
//        xAxis.setAxisLineColor(Color.GREEN);//设置x轴线颜色
//        xAxis.setAxisLineWidth(5f);//设置x轴线宽度
//        xAxis.setValueFormatter();//格式化x轴标签显示字符

            /*
             * Y轴默认显示左右两个轴线
             */
        //获取右边的轴线
        YAxis rightAxis = lineChart.getAxisRight();
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);
        //获取左边的轴线
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置网格线为虚线效果
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //是否绘制0所在的网格线
        leftAxis.setDrawZeroLine(false);


        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(true); //是否可以缩放 仅y轴
        lineChart.setPinchZoom(true);  //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        Legend l = lineChart.getLegend();//图例
//            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);//设置图例的位置
        l.setTextSize(10f);//设置文字大小
        l.setForm(Legend.LegendForm.CIRCLE);//正方形，圆形或线
        l.setFormSize(10f); // 设置Form的大小
        l.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        l.setFormLineWidth(10f);//设置Form的宽度

        lineChart.notifyDataSetChanged();//刷新数据
        lineChart.invalidate();//重绘
        return true;
    }
}
