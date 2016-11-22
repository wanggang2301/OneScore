package com.hhly.mlottery.bean;

import java.util.List;

/**
 * Created by yuely198 on 2016/11/15.
 */

public class ChoseStartBean {


    /**
     * result : 200
     * data : {"female":[{"headIcon":"http://pic.13322.com/icons/avatar/female/0.png"},{"headIcon":"http://pic.13322.com/icons/avatar/female/1.png"},{"headIcon":"http://pic.13322.com/icons/avatar/female/2.png"}],"male":[{"headIcon":"http://http://pic.13322.com/icons/avatar/male/0.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/1.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/2.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/3.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/4.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/5.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/6.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/7.png"},{"headIcon":"http://http://pic.13322.com/icons/avatar/male/8.png"}]}
     */

    private int result;
    private DataBean data;



    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        private List<FemaleBean> female;
        private List<MaleBean> male;



        public List<FemaleBean> getFemale() {
            return female;
        }

        public void setFemale(List<FemaleBean> female) {
            this.female = female;
        }

        public List<MaleBean> getMale() {
            return male;
        }

        public void setMale(List<MaleBean> male) {
            this.male = male;
        }

        public static class FemaleBean {
            /**
             * headIcon : http://pic.13322.com/icons/avatar/female/0.png
             */
            private boolean isChecked;
            public boolean isChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean isChecked) {
                this.isChecked = isChecked;
            }
            private String headIcon;

            public String getHeadIcon() {
                return headIcon;
            }

            public void setHeadIcon(String headIcon) {
                this.headIcon = headIcon;
            }
        }

        public static class MaleBean {
            /**
             * headIcon : http://http://pic.13322.com/icons/avatar/male/0.png
             */
            private boolean isChecked;
            public boolean isChecked() {
                return isChecked;
            }
            public void setIsChecked(boolean isChecked) {
                this.isChecked = isChecked;
            }
            private String headIcon;

            public String getHeadIcon() {
                return headIcon;
            }

            public void setHeadIcon(String headIcon) {
                this.headIcon = headIcon;
            }
        }
    }
}
