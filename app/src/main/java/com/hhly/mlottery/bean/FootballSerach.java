package com.hhly.mlottery.bean;

import java.util.List;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description: 足球资料库模糊搜索尸体bean
 * @data: 2016/9/8 15:34
 */
public class FootballSerach {

    /**
     * lgId : 67
     * lgName : 欧洲杯
     * kind : 2
     */

    public List<FootballSerach> leagues;

        public static class LeaguesBean {
            public int lgId;
            public String lgName;
            public int kind;
    }
}
