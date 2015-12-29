package com.example.jbtang.agi.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbtang on 12/12/2015.
 */
public class BarChartView {

    private static int margins[] = new int[]{70, 70, 70, 70};
    private static String[] titles = new String[]{"PUCCH", "PUSCH"};
    private List<int[]> values = new ArrayList<>();
    private static int[] colors = new int[]{Color.RED, Color.BLUE};
    private XYMultipleSeriesRenderer renderer;
    private Context mContext;
    private String mTitle;
    private List<String> option;

    public BarChartView(Context context) {
        this.mContext = context;
        this.renderer = new XYMultipleSeriesRenderer();
    }

    public void initData(int[] pucchList, int[] puschList, List<String> option, String title) {
        values.add(pucchList);
        values.add(puschList);
        mTitle = title;
        this.option = option;
    }

    public View getBarChartView() {
        buildBarRenderer();
        setChartSettings(renderer, mTitle, "", "", 0, 6, 0, 100, Color.BLACK, Color.BLACK);
        renderer.getSeriesRendererAt(0).setDisplayBoundingPoints(true);
        renderer.getSeriesRendererAt(1).setDisplayBoundingPoints(true);
        int size = option.size();
        for (int i = 0; i < size; i++) {
            renderer.addXTextLabel(i, option.get(i));
        }
        renderer.setMargins(margins);
        renderer.setMarginsColor(0x00ffffff);
        renderer.setPanEnabled(false, false);
        renderer.setZoomEnabled(false, false);// 设置x，y方向都不可以放大或缩�?
        renderer.setZoomRate(1.0f);
        renderer.setInScroll(false);
        renderer.setBackgroundColor(0x00ffffff);
        renderer.setApplyBackgroundColor(false);
        renderer.setShowGrid(true);
        renderer.setYLabelsPadding(15);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setXLabelsColor(Color.BLACK);
        View view = ChartFactory.getBarChartView(mContext, buildBarDataset(titles, values), renderer, BarChart.Type.DEFAULT); // Type.STACKED
        view.setBackgroundColor(0x00ffffff);
        return view;
    }

    private XYMultipleSeriesDataset buildBarDataset(String[] titles, List<int[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < titles.length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            int[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle,
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setXLabels(0);
        renderer.setYLabels(10);
        renderer.setLabelsTextSize(20);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        // renderer.setXLabelsColor(0xff000000);//设置X轴上的字体颜�?
        // renderer.setYLabelsColor(0,0xff000000);//设置Y轴上的字体颜�?

    }

    /*
     * 初始化柱子风�?
     */
    protected void buildBarRenderer() {
        if (null == renderer) {
            return;
        }
        renderer.setBarWidth(23);
        renderer.setBarSpacing(20);
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        for (int i = 0; i < colors.length; i++) {
            XYSeriesRenderer xyr = new XYSeriesRenderer();
            xyr.setChartValuesTextAlign(Paint.Align.RIGHT);
            xyr.setChartValuesTextSize(15);
            xyr.setDisplayChartValues(true);
            xyr.setColor(colors[i]);
            renderer.addSeriesRenderer(xyr);
        }
    }
}
