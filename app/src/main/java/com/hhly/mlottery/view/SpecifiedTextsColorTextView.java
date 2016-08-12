package com.hhly.mlottery.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:   自定义搜索Textview
 * @data: 2016/8/11 14:25
 */
public class SpecifiedTextsColorTextView extends TextView {

    public SpecifiedTextsColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSpecifiedTextsColor(String text, String specifiedTexts, int color) {
        String lowerText=text.toLowerCase();
        String lowerSpecifiedTexts=specifiedTexts.toLowerCase();
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = lowerSpecifiedTexts.length();

            String temp = lowerText;

        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(lowerSpecifiedTexts);

            if(start != -1)
            {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = lowerText.substring(lengthFront);

            }

        }while(start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);

        for(Integer i : sTextsStartList)
        {
            styledText.setSpan(
                    new ForegroundColorSpan(color),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        sTextsStartList=null;
        setText(styledText);
    }
}
