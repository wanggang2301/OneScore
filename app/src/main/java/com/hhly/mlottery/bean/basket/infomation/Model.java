package com.hhly.mlottery.bean.basket.infomation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {


    public static List<Map<String, Bean>> getdata() {
        List<Map<String, Bean>> list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Map<String, Bean> map = new HashMap<>();
            map.put("d0", getlist());
            map.put("d1", getlist1());
            map.put("d2", getlist2());
            map.put("d3", getlist3());
            list.add(map);
        }


        return list;
    }


    public static List<List<Be>> getdata2() {
        List<List<Be>> lists = new ArrayList<>();

        lists.add(getlists());
        lists.add(getlists2());
        lists.add(getlists3());


        return lists;

    }

    public static List<Be> getlists() {
        List<Be> list = new ArrayList<>();
        list.add(getBe());
        list.add(getBe11());
        list.add(getBe111());
        list.add(getBe1111());
        return list;
    }




    private static Be getBe() {
        B b = new B("中国", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new B("中国" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }

    private static Be getBe11() {
        B b = new B("新闻", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new B("皆苦" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }
    private static Be getBe111() {
        B b = new B("哈哈", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(new B("无敌" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }
    private static Be getBe1111() {
        B b = new B("扣扣", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(new B("流量" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }



    public static List<Be> getlists2() {
        List<Be> list = new ArrayList<>();
        list.add(getBe2());
        list.add(getBe22());
        list.add(getBe222());
        list.add(getBe2222());
        return list;
    }


    private static Be getBe2() {
        B b = new B("美国", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new B("方法" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }
    private static Be getBe22() {
        B b = new B("日本", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add(new B("天天" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }
    private static Be getBe222() {
        B b = new B("哥本哈根", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new B("女人" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }
    private static Be getBe2222() {
        B b = new B("葡萄牙", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new B("足球" + i, "www.baidu"));
        }
        Be be = new Be(b, list);
        return be;
    }



    public static List<Be> getlists3() {
        List<Be> list = new ArrayList<>();
        list.add(getBe3());
        list.add(getBe33());
        list.add(getBe333());
       // list.add(getBe3333());
        return list;
    }


    private static Be getBe3() {
        B b = new B("英国", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new B("好好" + i, "www.baidu"));
        }
        Be be = new Be(b,list);
        return be;
    }


    private static Be getBe33() {
        B b = new B("德国", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new B("汽车" + i, "www.baidu"));
        }
        Be be = new Be(b,list);
        return be;
    }
    private static Be getBe333() {
        B b = new B("法国", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(new B("香水" + i, "www.baidu"));
        }
        Be be = new Be(b,list);
        return be;
    }
    private static Be getBe3333() {
        B b = new B("社会", "www.baidu");
        List<B> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(new B("视频" + i, "www.baidu"));
        }
        Be be = new Be(b,list);
        return be;
    }




    // 父listview的文本数据数组
    public static String[] EXPANDABLE_GRIDVIEW_TXT = new String[]{"新闻", "军事", "微博", "体育", "娱乐", "财经"};
    // 子listview的文本数据
    public static String[][] EXPANDABLE_MOREGRIDVIEW_TXT =
            {
                    {"国内", "社会", "国际", "评论", "传媒", "排行", "视频"},
                    {"新闻", "图片", "中国军情", "专栏", "视频"},
                    {"注册", "名人堂", "人气热榜", "客户端", "热门微博", "随便看看"},
                    {"NBA", "中超", "欧冠", "英超", "意甲", "西甲"},
                    {"明星", "电影", "电视", "音乐", "韩娱", "毒蛇女", "八卦"},
                    {"产经", "消费", "理财", "外汇", "股票", "行情", "基金", "自选股"}
            };


    public static Bean getlist() {
        Bean bean = new Bean();
        bean.setTitle("军事");
        List<String> list = new ArrayList<>();
        list.add("评论");
        list.add("传媒");
        list.add("视频");
        bean.setList(list);
        return bean;
    }

    public static Bean getlist1() {
        Bean bean = new Bean();
        bean.setTitle("微博");
        List<String> list = new ArrayList<>();
        list.add("注册");
        list.add("名人堂");
        list.add("中国军情");
        bean.setList(list);
        return bean;
    }

    public static Bean getlist2() {
        Bean bean = new Bean();
        bean.setTitle("体育");
        List<String> list = new ArrayList<>();
        list.add("理财");
        list.add("单独");
        list.add("嘎嘎嘎");
        bean.setList(list);
        return bean;
    }

    public static Bean getlist3() {
        Bean bean = new Bean();
        bean.setTitle("如图");
        List<String> list = new ArrayList<>();
        list.add("发个广告");
        list.add("大尺度");
        list.add("宝宝");
        bean.setList(list);
        return bean;
    }
}
