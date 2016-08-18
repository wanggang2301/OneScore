package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * 描述:  足球走势图
 * 作者:  wangg@13322.com
 * 时间:  2016/8/17 18:27
 */
public class FootballTrendBean {

    private TrendFormBean trendForm;

    private int result;

    public TrendFormBean getTrendForm() {
        return trendForm;
    }

    public void setTrendForm(TrendFormBean trendForm) {
        this.trendForm = trendForm;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static class TrendFormBean {
        private ShotBean shot;
        private ShepianBean shepian;
        private DangerousAttackBean dangerousAttack;
        private AttackBean attack;

        public ShotBean getShot() {
            return shot;
        }

        public void setShot(ShotBean shot) {
            this.shot = shot;
        }

        public ShepianBean getShepian() {
            return shepian;
        }

        public void setShepian(ShepianBean shepian) {
            this.shepian = shepian;
        }

        public DangerousAttackBean getDangerousAttack() {
            return dangerousAttack;
        }

        public void setDangerousAttack(DangerousAttackBean dangerousAttack) {
            this.dangerousAttack = dangerousAttack;
        }

        public AttackBean getAttack() {
            return attack;
        }

        public void setAttack(AttackBean attack) {
            this.attack = attack;
        }

        public static class ShotBean {
            /**
             * time : 1952166
             * flag : 0
             */

            private List<HomeBean> home;
            /**
             * time : 563554
             * flag : 0
             */

            private List<GuestBean> guest;

            public List<HomeBean> getHome() {
                return home;
            }

            public void setHome(List<HomeBean> home) {
                this.home = home;
            }

            public List<GuestBean> getGuest() {
                return guest;
            }

            public void setGuest(List<GuestBean> guest) {
                this.guest = guest;
            }

            public static class HomeBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }

            public static class GuestBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }
        }

        public static class ShepianBean {
            /**
             * time : 1991185
             * flag : 0
             */

            private List<HomeBean> home;
            /**
             * time : 818854
             * flag : 0
             */

            private List<GuestBean> guest;

            public List<HomeBean> getHome() {
                return home;
            }

            public void setHome(List<HomeBean> home) {
                this.home = home;
            }

            public List<GuestBean> getGuest() {
                return guest;
            }

            public void setGuest(List<GuestBean> guest) {
                this.guest = guest;
            }

            public static class HomeBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }

            public static class GuestBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }
        }

        public static class DangerousAttackBean {
            /**
             * time : 99608
             * flag : 0
             */

            private List<HomeBean> home;
            /**
             * time : 2617202
             * flag : 2
             */

            private List<GuestBean> guest;

            public List<HomeBean> getHome() {
                return home;
            }

            public void setHome(List<HomeBean> home) {
                this.home = home;
            }

            public List<GuestBean> getGuest() {
                return guest;
            }

            public void setGuest(List<GuestBean> guest) {
                this.guest = guest;
            }

            public static class HomeBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }

            public static class GuestBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }
        }

        public static class AttackBean {
            /**
             * time : 16959
             * flag : 0
             */

            private List<HomeBean> home;
            /**
             * time : 2617202
             * flag : 2
             */

            private List<GuestBean> guest;

            public List<HomeBean> getHome() {
                return home;
            }

            public void setHome(List<HomeBean> home) {
                this.home = home;
            }

            public List<GuestBean> getGuest() {
                return guest;
            }

            public void setGuest(List<GuestBean> guest) {
                this.guest = guest;
            }

            public static class HomeBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }

            public static class GuestBean {
                private int time;
                private int flag;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }
            }
        }
    }
}
