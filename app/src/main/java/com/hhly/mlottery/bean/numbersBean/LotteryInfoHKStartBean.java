package com.hhly.mlottery.bean.numbersBean;

import java.util.List;

/**
 * desc:香港详情统计 Bean
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class LotteryInfoHKStartBean {

    private int result;
    private DateBean date;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DateBean getDate() {
        return date;
    }

    public void setDate(DateBean date) {
        this.date = date;
    }

    public static class DateBean {

        private FourThousandBean fourThousand;// 四千
        private FiveHundredBean fiveHundred;// 五百
        private OneHundredBean oneHundred;// 一百
        private OneThousandBean oneThousand;// 一千

        public FourThousandBean getFourThousand() {
            return fourThousand;
        }

        public void setFourThousand(FourThousandBean fourThousand) {
            this.fourThousand = fourThousand;
        }

        public FiveHundredBean getFiveHundred() {
            return fiveHundred;
        }

        public void setFiveHundred(FiveHundredBean fiveHundred) {
            this.fiveHundred = fiveHundred;
        }

        public OneHundredBean getOneHundred() {
            return oneHundred;
        }

        public void setOneHundred(OneHundredBean oneHundred) {
            this.oneHundred = oneHundred;
        }

        public OneThousandBean getOneThousand() {
            return oneThousand;
        }

        public void setOneThousand(OneThousandBean oneThousand) {
            this.oneThousand = oneThousand;
        }

        public static class FourThousandBean {
            private List<ZodiacBean> zodiac;
            private List<NumberBean> number;
            private List<MantissaBean> mantissa;
            private List<BoBean> bo;

            public List<ZodiacBean> getZodiac() {
                return zodiac;
            }

            public void setZodiac(List<ZodiacBean> zodiac) {
                this.zodiac = zodiac;
            }

            public List<NumberBean> getNumber() {
                return number;
            }

            public void setNumber(List<NumberBean> number) {
                this.number = number;
            }

            public List<MantissaBean> getMantissa() {
                return mantissa;
            }

            public void setMantissa(List<MantissaBean> mantissa) {
                this.mantissa = mantissa;
            }

            public List<BoBean> getBo() {
                return bo;
            }

            public void setBo(List<BoBean> bo) {
                this.bo = bo;
            }

            public static class ZodiacBean {
                /**
                 * coedAppear : 331
                 * numberAppear : 2115
                 * coedNotAppear : 11
                 * key : 鼠
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class NumberBean {
                /**
                 * coedAppear : 95
                 * numberAppear : 479
                 * coedNotAppear : 23
                 * key : 19
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class MantissaBean {
                /**
                 * coedAppear : 394
                 * numberAppear : 2419
                 * coedNotAppear : 11
                 * key : 3
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class BoBean {
                /**
                 * coedAppear : 1331
                 * numberAppear : 7748
                 * coedNotAppear : 0
                 * key : 蓝
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }

        public static class FiveHundredBean {
            private List<ZodiacBeanFiveHunderd> zodiac;
            private List<NumberBeanFiveHunderd> number;
            private List<MantissaBeanFiveHunderd> mantissa;
            private List<BoBeanFiveHunderd> bo;

            public List<ZodiacBeanFiveHunderd> getZodiac() {
                return zodiac;
            }

            public void setZodiac(List<ZodiacBeanFiveHunderd> zodiac) {
                this.zodiac = zodiac;
            }

            public List<NumberBeanFiveHunderd> getNumber() {
                return number;
            }

            public void setNumber(List<NumberBeanFiveHunderd> number) {
                this.number = number;
            }

            public List<MantissaBeanFiveHunderd> getMantissa() {
                return mantissa;
            }

            public void setMantissa(List<MantissaBeanFiveHunderd> mantissa) {
                this.mantissa = mantissa;
            }

            public List<BoBeanFiveHunderd> getBo() {
                return bo;
            }

            public void setBo(List<BoBeanFiveHunderd> bo) {
                this.bo = bo;
            }

            public static class ZodiacBeanFiveHunderd {
                /**
                 * coedAppear : 45
                 * numberAppear : 253
                 * coedNotAppear : 11
                 * key : 鼠
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class NumberBeanFiveHunderd {
                /**
                 * coedAppear : 13
                 * numberAppear : 47
                 * coedNotAppear : 23
                 * key : 19
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class MantissaBeanFiveHunderd {
                /**
                 * coedAppear : 45
                 * numberAppear : 294
                 * coedNotAppear : 11
                 * key : 3
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class BoBeanFiveHunderd {
                /**
                 * coedAppear : 175
                 * numberAppear : 991
                 * coedNotAppear : 0
                 * key : 蓝
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }

        public static class OneHundredBean {
            private List<ZodiacBeanOneHundred> zodiac;
            private List<NumberBeanOneHundred> number;
            private List<MantissaBeanOneHundred> mantissa;
            private List<BoBeanOneHundred> bo;

            public List<ZodiacBeanOneHundred> getZodiac() {
                return zodiac;
            }

            public void setZodiac(List<ZodiacBeanOneHundred> zodiac) {
                this.zodiac = zodiac;
            }

            public List<NumberBeanOneHundred> getNumber() {
                return number;
            }

            public void setNumber(List<NumberBeanOneHundred> number) {
                this.number = number;
            }

            public List<MantissaBeanOneHundred> getMantissa() {
                return mantissa;
            }

            public void setMantissa(List<MantissaBeanOneHundred> mantissa) {
                this.mantissa = mantissa;
            }

            public List<BoBeanOneHundred> getBo() {
                return bo;
            }

            public void setBo(List<BoBeanOneHundred> bo) {
                this.bo = bo;
            }

            public static class ZodiacBeanOneHundred {
                /**
                 * coedAppear : 5
                 * numberAppear : 47
                 * coedNotAppear : 11
                 * key : 鼠
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class NumberBeanOneHundred {
                /**
                 * coedAppear : 2
                 * numberAppear : 12
                 * coedNotAppear : 23
                 * key : 19
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class MantissaBeanOneHundred {
                /**
                 * coedAppear : 10
                 * numberAppear : 62
                 * coedNotAppear : 11
                 * key : 3
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class BoBeanOneHundred {
                /**
                 * coedAppear : 35
                 * numberAppear : 173
                 * coedNotAppear : 0
                 * key : 蓝
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }

        public static class OneThousandBean {
            private List<ZodiacBeanOneThousand> zodiac;
            private List<NumberBeanOneThousand> number;
            private List<MantissaBeanOneThousand> mantissa;
            private List<BoBeanOneThousand> bo;

            public List<ZodiacBeanOneThousand> getZodiac() {
                return zodiac;
            }

            public void setZodiac(List<ZodiacBeanOneThousand> zodiac) {
                this.zodiac = zodiac;
            }

            public List<NumberBeanOneThousand> getNumber() {
                return number;
            }

            public void setNumber(List<NumberBeanOneThousand> number) {
                this.number = number;
            }

            public List<MantissaBeanOneThousand> getMantissa() {
                return mantissa;
            }

            public void setMantissa(List<MantissaBeanOneThousand> mantissa) {
                this.mantissa = mantissa;
            }

            public List<BoBeanOneThousand> getBo() {
                return bo;
            }

            public void setBo(List<BoBeanOneThousand> bo) {
                this.bo = bo;
            }

            public static class ZodiacBeanOneThousand {
                /**
                 * coedAppear : 87
                 * numberAppear : 501
                 * coedNotAppear : 11
                 * key : 鼠
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class NumberBeanOneThousand {
                /**
                 * coedAppear : 19
                 * numberAppear : 88
                 * coedNotAppear : 23
                 * key : 19
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class MantissaBeanOneThousand {
                /**
                 * coedAppear : 94
                 * numberAppear : 586
                 * coedNotAppear : 11
                 * key : 3
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }

            public static class BoBeanOneThousand {
                /**
                 * coedAppear : 336
                 * numberAppear : 2028
                 * coedNotAppear : 0
                 * key : 蓝
                 */

                private int coedAppear;
                private int numberAppear;
                private int coedNotAppear;
                private String key;

                public int getCoedAppear() {
                    return coedAppear;
                }

                public void setCoedAppear(int coedAppear) {
                    this.coedAppear = coedAppear;
                }

                public int getNumberAppear() {
                    return numberAppear;
                }

                public void setNumberAppear(int numberAppear) {
                    this.numberAppear = numberAppear;
                }

                public int getCoedNotAppear() {
                    return coedNotAppear;
                }

                public void setCoedNotAppear(int coedNotAppear) {
                    this.coedNotAppear = coedNotAppear;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }
    }

}
