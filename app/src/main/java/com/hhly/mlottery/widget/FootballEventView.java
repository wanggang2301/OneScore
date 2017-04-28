package com.hhly.mlottery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/1/4.
 */
public class FootballEventView extends View {

    private static final String SCORE = "1029";//主队进球
    private static final String RED_CARD = "1032";
    private static final String POINT = "point";//点球进球数据没有
    private static final String YELLOW_CARD = "1034";
    private static final String SUBSTITUTION = "1055";
    private static final String CORNER = "1025";
    private static final String YTORED = "1045";//两黄变一红
    //客队事件
    private static final String SCORE1="2053";//客队进球
    private static final String RED_CARD1= "2056";
    private static final String POINT1 = "point";//点球进球数据没有
    private static final String YELLOW_CARD1 = "2058";
    private static final String SUBSTITUTION1 = "2079";
    private static final String CORNER1 = "2049";
    private static final String YTORED1 = "2069";//两黄变一红

    private Context mContext;

    private Paint mPaint = new Paint();

    private Canvas mCanvas = new Canvas();


    private Map<String, List<MatchTimeLiveBean>> mTimeEventMap1;
    private Map<String, List<MatchTimeLiveBean>> mTimeEventMap2;

    //事件的图片
    private Bitmap bitmapScore;//进球
    private Bitmap bitmapRed;//rc
    private Bitmap bitmapYellow;//黄牌
    private Bitmap bitmapPointScore;//点球
    private Bitmap bitmapSubstitution;//主队换人
    private Bitmap bitmapSubstitution2;//客队换人
    private Bitmap bitmapCorner;//角球
    private Bitmap bitmapYtoRed;//两黄变一红

    /**
     * 自定义控件的宽高
     */
    private int width;
    private int height;

    /**
     * 控件左右半场的时间抽
     */
    private float halfWidth;
    private float midBlackWidth;//中间黑色部分的宽度
    private int proportion;
    private float margin;//距离左边的距离
//    public static boolean isFirstHalf=true;//时间为46分钟，是上半场还是下半场？
    public static int minute=1;

    private final static String TAG = "FootballHorizontalEvent";

    private final static String GRAVITY_BUTTOM = "b";
    private final static String GRAVITY_TOP = "t";

    private String mImageGravity;

    public FootballEventView(Context context) {
        this(context, null);
    }

    public FootballEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FootballEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FootballEventView);
        mImageGravity = typedArray.getString(R.styleable.FootballEventView_image_gravity);
        proportion=typedArray.getInteger(R.styleable.FootballEventView_midwidth_proportion, 30);
        margin=typedArray.getDimension(R.styleable.FootballEventView_mdymargin, 30.0f);
        typedArray.recycle();
    }

    /**
     * 初始化操作
     */
    private void init(AttributeSet attrs) {
        //提前加载好图片
        bitmapScore = BitmapFactory.decodeResource(getResources(), R.mipmap.event_goal);
        bitmapRed = BitmapFactory.decodeResource(getResources(), R.mipmap.rc);
        bitmapYellow = BitmapFactory.decodeResource(getResources(), R.mipmap.yc);
        bitmapCorner = BitmapFactory.decodeResource(getResources(), R.mipmap.corner);
        bitmapPointScore = BitmapFactory.decodeResource(getResources(), R.mipmap.pointscore);
        bitmapSubstitution = BitmapFactory.decodeResource(getResources(), R.mipmap.substitution);
        bitmapSubstitution2 = BitmapFactory.decodeResource(getResources(), R.mipmap.substitution2);
        bitmapYtoRed=BitmapFactory.decodeResource(getResources(),R.mipmap.y2r);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }


        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mTimeEventMap1 == null) {
            return;
        }

        this.mCanvas = canvas;

        midBlackWidth=width/proportion;
        halfWidth = (width - midBlackWidth-2*margin) / 2;//跟时间轴的一样。黑色占1/20

//        drawFootballImage(mTimeEventMap1, true);
//       drawFootballImage(mTimeEventMap2,false);
        drawFootballImageByState(mTimeEventMap1);

       
    }

    /**
     * 上下半场分开绘制，根据上半场结束code。
     * @param eventMap
     * @param isFirst
     */
    private void drawFootballImage(Map<String, List<MatchTimeLiveBean>> eventMap,boolean isFirst) {


        Bitmap bitmapEvent = BitmapFactory.decodeResource(getResources(), R.mipmap.event_goal);

        for (String time : eventMap.keySet()) {

            /**
             * 图片的x轴的坐标值
             */
            float imageX = 0;
            float timeF = Float.parseFloat(time)/minute;//完场的时间是毫秒数还没有除60000.赛中的在外面已经除过了
            if(timeF>=90){
                timeF=90;
            }
            if (timeF <= 45) {
                imageX = timeF * halfWidth / 45+margin;
            } else if (timeF > 45) {
                if(!isFirst){//下半场
                    imageX = margin+halfWidth + midBlackWidth + halfWidth / 45 * (timeF - 45);
                }
                else{//上半场时间还大于45，是补时，继续画在上半场最后刻度
                    timeF=45;
                    imageX = timeF * halfWidth / 45+margin;
                }


            }

            for(MatchTimeLiveBean data:eventMap.get(time)){
                //判断是什么事件
                if (data.getCode().equals(SCORE)||data.getCode().equals(SCORE1)) {
                    bitmapEvent = bitmapScore;
                } else if (data.getCode().equals(RED_CARD)||data.getCode().equals(RED_CARD1)) {
                    bitmapEvent = bitmapRed;
                } else if (data.getCode().equals(YELLOW_CARD)||data.getCode().equals(YELLOW_CARD1)) {
                    bitmapEvent = bitmapYellow;
                } else if (data.getCode().equals(POINT)||data.getCode().equals(POINT1)) {
                    bitmapEvent = bitmapPointScore;
                } else if (data.getCode().equals(CORNER)||data.getCode().equals(CORNER1)) {
                    bitmapEvent = bitmapCorner;
                } else if (data.getCode().equals(SUBSTITUTION)||data.getCode().equals(SUBSTITUTION1)) {
                    //主客队换人的图标不一样
                    if (mImageGravity.equals(GRAVITY_BUTTOM)) {
                        bitmapEvent = bitmapSubstitution;
                    } else {
                        bitmapEvent = bitmapSubstitution2;
                    }
                }
                else if(data.getCode().equals(YTORED)||data.getCode().equals(YTORED1)){
                    bitmapEvent=bitmapYtoRed;
                }
//                else if(data.getCode().equals("1")){//上半场结束的事件也在集合中。
//                    continue;
//                }
                //判断是主队还是客队
                if (mImageGravity.equals(GRAVITY_BUTTOM)) {
                    mCanvas.drawBitmap(bitmapEvent, imageX - bitmapEvent.getWidth() / 2, getHeight() - bitmapEvent.getHeight(), mPaint);//准确位置是球的中间，即减去球宽度一半
                } else {
                    mCanvas.drawBitmap(bitmapEvent, imageX - bitmapEvent.getWidth() / 2, 0, mPaint);
                }

                imageX+=5;
            }
        }
    }

    /**
     * 通过比赛状态展示事件
     * @param eventMap
     *
     */
    private void drawFootballImageByState(Map<String, List<MatchTimeLiveBean>> eventMap) {


        Bitmap bitmapEvent = BitmapFactory.decodeResource(getResources(), R.mipmap.event_goal);
         List<Map.Entry<String,List<MatchTimeLiveBean>>> mappingList=null;
        mappingList=new ArrayList<>(eventMap.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String, List<MatchTimeLiveBean>>>() {//同一时间内既有别的事件又有进球。进球会因为
            //别的事件绘制而绘制。如果在下一时间又发生别的事件。那么进球就会被盖。对map进行排序。有进球事件就放在后面。
            @Override
            public int compare(Map.Entry<String, List<MatchTimeLiveBean>> lhs, Map.Entry<String, List<MatchTimeLiveBean>> rhs) {
                int compare = 0;
                if(lhs.getValue().size()>1||rhs.getValue().size()>1){//只有一个事件不需要排序。
                    for (MatchTimeLiveBean bean : lhs.getValue()) {
                        if (bean.getCode().equals(SCORE) || bean.getCode().equals(SCORE1)) {
                            compare = 1;
                        }
                        for (MatchTimeLiveBean bean1 : rhs.getValue()) {
                            if (bean1.getCode().equals(SCORE) || bean1.getCode().equals(SCORE1)) {
                                compare = -1;
                            }
                        }
                    }
                }

                return compare;
            }
        });
        eventMap=new LinkedHashMap<>();
        for(int i=0;i<mappingList.size();i++){
            eventMap.put(mappingList.get(i).getKey(),mappingList.get(i).getValue());
        }

        for (String time : eventMap.keySet()) {

            /**
             * 图片的x轴的坐标值
             */
            float imageX = 0;
            float timeF = Float.parseFloat(time);
            if(timeF>=90){
                timeF=90;
            }
            if (timeF <= 45) {
                imageX = timeF * halfWidth / 45+margin;
            } else if (timeF > 45) {
                        imageX = margin+halfWidth + midBlackWidth + halfWidth / 45 * (timeF - 45);
            }
            for(MatchTimeLiveBean data:eventMap.get(time)){
                //判断是什么事件
                if (data.getCode().equals(SCORE)||data.getCode().equals(SCORE1)) {
                    bitmapEvent = bitmapScore;
                } else if (data.getCode().equals(RED_CARD)||data.getCode().equals(RED_CARD1)) {
                    bitmapEvent = bitmapRed;
                } else if (data.getCode().equals(YELLOW_CARD)||data.getCode().equals(YELLOW_CARD1)) {
                    bitmapEvent = bitmapYellow;
                } else if (data.getCode().equals(POINT)||data.getCode().equals(POINT1)) {
                    bitmapEvent = bitmapPointScore;
                } else if (data.getCode().equals(CORNER)||data.getCode().equals(CORNER1)) {
                    bitmapEvent = bitmapCorner;
                } else if (data.getCode().equals(SUBSTITUTION)||data.getCode().equals(SUBSTITUTION1)) {
                    //主客队换人的图标不一样
                    if (mImageGravity.equals(GRAVITY_BUTTOM)) {
                        bitmapEvent = bitmapSubstitution;
                    } else {
                        bitmapEvent = bitmapSubstitution2;
                    }
                }
                else if(data.getCode().equals(YTORED)||data.getCode().equals(YTORED1)){
                    bitmapEvent=bitmapYtoRed;
                }
                else if(data.getCode().equals("1")){//上半场结束的事件也在集合中。
                    continue;
                }
                //判断是主队还是客队
                if (mImageGravity.equals(GRAVITY_BUTTOM)) {
                    mCanvas.drawBitmap(bitmapEvent, imageX - bitmapEvent.getWidth() / 2, getHeight() - bitmapEvent.getHeight(), mPaint);//准确位置是球的中间，即减去球宽度一半
                } else {
                    mCanvas.drawBitmap(bitmapEvent, imageX - bitmapEvent.getWidth() / 2, 0, mPaint);
                }

                imageX+=5;
            }
        }
    }

    /**
     * 设置足球事件
     * @param timeEventMap1
     * @param timeEventMap2
     */
    public void setEventImage(Map<String, List<MatchTimeLiveBean>> timeEventMap1,Map<String, List<MatchTimeLiveBean>> timeEventMap2) {
        this.mTimeEventMap1 = timeEventMap1;
        this.mTimeEventMap2=timeEventMap2;

        invalidate();
    }

    /**
     * 根据状态
     * @param timeEventMap1
     */
    public void setEventImageByState(Map<String, List<MatchTimeLiveBean>> timeEventMap1){
        this.mTimeEventMap1=timeEventMap1;
        invalidate();
    }
}
