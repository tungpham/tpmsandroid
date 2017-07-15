//package com.ethan.morephone.presentation.usage.voice;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.ethan.morephone.R;
//import com.ethan.morephone.presentation.BaseFragment;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//
//import java.util.ArrayList;
//
//import static com.ethan.morephone.R.id.chart;
//
///**
// * Created by Ethan on 4/22/17.
// */
//
//public class VoiceUsageFragment extends BaseFragment {
//
//    public static VoiceUsageFragment getInstance(){
//        return new VoiceUsageFragment();
//    }
//
//    private LineChart mLineChart;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_usage_voice, container, false);
//
//        mLineChart = (LineChart) view.findViewById(chart);
//
//        mLineChart.getDescription().setEnabled(false);
//        mLineChart.setDrawGridBackground(false);
//
//        XAxis xAxis = mLineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
////        xAxis.setTypeface(mTf);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(true);
//
//        YAxis leftAxis = mLineChart.getAxisLeft();
////        leftAxis.setTypeface(mTf);
//        leftAxis.setLabelCount(5, false);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = mLineChart.getAxisRight();
////        rightAxis.setTypeface(mTf);
//        rightAxis.setLabelCount(5, false);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        // set data
//        mLineChart.setData(generateDataLine(1));
//
//        // do not forget to refresh the chart
//        // holder.chart.invalidate();
//        mLineChart.animateX(750);
//
//        return view;
//    }
//
//    private LineData generateDataLine(int cnt) {
//
//        ArrayList<Entry> e1 = new ArrayList<Entry>();
//
//        for (int i = 0; i < 31; i++) {
//            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
//        }
//
//        LineDataSet d1 = new LineDataSet(e1, "");
//        d1.setLineWidth(2.5f);
//        d1.setCircleRadius(4.5f);
//        d1.setHighLightColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        d1.setDrawValues(false);
//
////        ArrayList<Entry> e2 = new ArrayList<Entry>();
////
////        for (int i = 0; i < 12; i++) {
////            e2.add(new Entry(i, e1.get(i).getY() - 30));
////        }
//
////        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
////        d2.setLineWidth(2.5f);
////        d2.setCircleRadius(4.5f);
////        d2.setHighLightColor(Color.rgb(244, 117, 117));
////        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
////        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
////        d2.setDrawValues(false);
//
//        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
//        sets.add(d1);
////        sets.add(d2);
//
//        LineData cd = new LineData(sets);
//        return cd;
//    }
//}
