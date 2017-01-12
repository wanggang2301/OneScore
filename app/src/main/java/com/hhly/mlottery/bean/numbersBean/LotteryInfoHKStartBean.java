package com.hhly.mlottery.bean.numbersBean;

import java.util.List;

/**
 * desc:香港详情统计 Bean
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class LotteryInfoHKStartBean {

    private int result;
    private Data data;

    public class Data {
        private Content fourThousand;
        private Content fiveHundred;
        private Content oneHundred;
        private Content oneThousand;

        public class Content {
            private List<Body> zodiac;
            private List<Body> number;
            private List<Body> mantissa;
            private List<Body> bo;

            public List<Body> getZodiac() {
                return zodiac;
            }

            public void setZodiac(List<Body> zodiac) {
                this.zodiac = zodiac;
            }

            public List<Body> getNumber() {
                return number;
            }

            public void setNumber(List<Body> number) {
                this.number = number;
            }

            public List<Body> getMantissa() {
                return mantissa;
            }

            public void setMantissa(List<Body> mantissa) {
                this.mantissa = mantissa;
            }

            public List<Body> getBo() {
                return bo;
            }

            public void setBo(List<Body> bo) {
                this.bo = bo;
            }
        }

        public class Body {
            private String coedAppear;
            private String numberAppear;
            private String coedNotAppear;
            private String key;

            public String getCoedAppear() {
                return coedAppear;
            }

            public void setCoedAppear(String coedAppear) {
                this.coedAppear = coedAppear;
            }

            public String getNumberAppear() {
                return numberAppear;
            }

            public void setNumberAppear(String numberAppear) {
                this.numberAppear = numberAppear;
            }

            public String getCoedNotAppear() {
                return coedNotAppear;
            }

            public void setCoedNotAppear(String coedNotAppear) {
                this.coedNotAppear = coedNotAppear;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        public Content getFourThousand() {
            return fourThousand;
        }

        public void setFourThousand(Content fourThousand) {
            this.fourThousand = fourThousand;
        }

        public Content getFiveHundred() {
            return fiveHundred;
        }

        public void setFiveHundred(Content fiveHundred) {
            this.fiveHundred = fiveHundred;
        }

        public Content getOneHundred() {
            return oneHundred;
        }

        public void setOneHundred(Content oneHundred) {
            this.oneHundred = oneHundred;
        }

        public Content getOneThousand() {
            return oneThousand;
        }

        public void setOneThousand(Content oneThousand) {
            this.oneThousand = oneThousand;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
