package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * @author: Wangg
 * @Name：SnookerMatchBean
 * @Description:
 * @Created on:2017/2/20  14:57.
 */

public class SnookerMatchBean {
    /**
     * result : 200
     * data : [{"leagueId":"319970","leagueName":"斯诺克超级联赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/0425c0a5-ac87-4b63-98b2-94bb11af80d0.jpg"},{"leagueId":"319995","leagueName":"亚洲巡回赛","leagueLogo":null},{"leagueId":"319992","leagueName":"威尔士公开赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/2128b081-8be5-4147-928e-9d57da06783a.gif"},{"leagueId":"319993","leagueName":"印度公开赛","leagueLogo":null},{"leagueId":"319991","leagueName":"苏格兰公开赛","leagueLogo":null},{"leagueId":"319839","leagueName":"北爱尔兰锦标赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/d693aa78-2512-4d37-b25f-cc3ce266edfb.gif"},{"leagueId":"319978","leagueName":"斯诺克精英赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/9c2551f4-1395-4ccc-ad8d-7b32a371246d.jpg"},{"leagueId":"319999","leagueName":"中国锦标赛","leagueLogo":null},{"leagueId":"319977","leagueName":"斯诺克王中王赛","leagueLogo":null},{"leagueId":"319976","leagueName":"斯诺克冠军联赛7","leagueLogo":null},{"leagueId":"319900","leagueName":"世界斯诺克欧洲巡回赛6","leagueLogo":null},{"leagueId":"319651","leagueName":"北爱尔兰公开赛","leagueLogo":null},{"leagueId":"319899","leagueName":"世界斯诺克欧洲巡回赛3","leagueLogo":null},{"leagueId":"319898","leagueName":"皇家钟表大奖赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/5b41be09-fcce-4960-a8b7-cfbd6a7192e0.gif"},{"leagueId":"319982","leagueName":"斯诺克球员巡回赛","leagueLogo":null},{"leagueId":"319980","leagueName":"斯诺克世界系列赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/773f5008-2d3a-4dd6-a6ee-88f32f24fdd7.jpg"},{"leagueId":"319908","leagueName":"世界斯诺克巡回徐州公开赛","leagueLogo":null},{"leagueId":"319907","leagueName":"世界斯诺克欧洲巡回赛1","leagueLogo":null},{"leagueId":"319796","leagueName":"大师赛","leagueLogo":null},{"leagueId":"319797","leagueName":"海口公开赛","leagueLogo":null},{"leagueId":"319801","leagueName":"巴林锦标赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/d5451ab8-408a-4525-b796-5f51df688688.gif"},{"leagueId":"319895","leagueName":"上海大师赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/1e0b41ee-b721-4c3e-a8b5-194f72b7a871.jpg"},{"leagueId":"319655","leagueName":"澳大利亚公开赛","leagueLogo":null},{"leagueId":"319842","leagueName":"世界超级挑战赛","leagueLogo":null},{"leagueId":"319847","leagueName":"世界公开赛","leagueLogo":null},{"leagueId":"319787","leagueName":"国际锦标赛","leagueLogo":null},{"leagueId":"319618","leagueName":"PTC总决赛","leagueLogo":null},{"leagueId":"319916","leagueName":"世界斯诺克欧洲巡回赛2","leagueLogo":null},{"leagueId":"319883","leagueName":"世界球员巡回赛郑州站","leagueLogo":null},{"leagueId":"319953","leagueName":"斯诺克冠军联赛5","leagueLogo":null},{"leagueId":"319954","leagueName":"斯诺克冠军联赛6","leagueLogo":null},{"leagueId":"319957","leagueName":"斯诺克冠军联赛4","leagueLogo":null},{"leagueId":"320001","leagueName":"亚洲巡回赛宜兴公开赛","leagueLogo":null},{"leagueId":"319956","leagueName":"斯诺克冠军联赛3","leagueLogo":null},{"leagueId":"320000","leagueName":"无锡精英赛","leagueLogo":null},{"leagueId":"320003","leagueName":"英格兰公开赛","leagueLogo":null},{"leagueId":"319958","leagueName":"斯诺克大师赛","leagueLogo":null},{"leagueId":"320009","leagueName":"中国职业巡回赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/7e415016-29b4-496b-a6ba-f8fc9cf4d07c.jpg"},{"leagueId":"319853","leagueName":"德国大师赛","leagueLogo":null},{"leagueId":"319786","leagueName":"欧洲大师赛","leagueLogo":null},{"leagueId":"319673","leagueName":"欧洲球员巡回锦标赛","leagueLogo":null},{"leagueId":"319671","leagueName":"冠中冠邀请赛","leagueLogo":null},{"leagueId":"319876","leagueName":"世界球员巡回赛海宁公开赛","leagueLogo":null},{"leagueId":"319960","leagueName":"世界斯诺克元老锦标赛","leagueLogo":null},{"leagueId":"320010","leagueName":"温布利大师赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/ad1c0555-0725-4f74-acb0-4e4de202df11.gif"},{"leagueId":"319969","leagueName":"斯诺克冠军联赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/82fb0886-5d53-4971-8f46-548046293f1f.jpg"},{"leagueId":"320012","leagueName":"中外明星慈善挑战赛","leagueLogo":null},{"leagueId":"320011","leagueName":"英国锦标赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/3a6a1265-8595-4497-bec2-53dae1552269.jpg"},{"leagueId":"319921","leagueName":"世界斯诺克欧洲巡回赛5","leagueLogo":null},{"leagueId":"319922","leagueName":"世界斯诺克欧洲巡回赛4","leagueLogo":null},{"leagueId":"320016","leagueName":"中国公开赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/dde9157e-5f1d-48fe-8adf-f85d453f6c9c.gif"},{"leagueId":"319923","leagueName":"世界锦标赛","leagueLogo":"http://sports.win007.com/files/snooker/sclass/612b3f76-7310-4386-a153-7bab6bdc5c5d.gif"},{"leagueId":"319865","leagueName":"世界大奖赛","leagueLogo":null}]
     */

    private int result;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * leagueId : 319970
         * leagueName : 斯诺克超级联赛
         * leagueLogo : http://sports.win007.com/files/snooker/sclass/0425c0a5-ac87-4b63-98b2-94bb11af80d0.jpg
         */

        private String leagueId;
        private String leagueName;
        private String leagueLogo;

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getLeagueLogo() {
            return leagueLogo;
        }

        public void setLeagueLogo(String leagueLogo) {
            this.leagueLogo = leagueLogo;
        }
    }
}
