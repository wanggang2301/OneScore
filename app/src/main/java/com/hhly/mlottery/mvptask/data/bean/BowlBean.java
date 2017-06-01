package com.hhly.mlottery.mvptask.data.bean;

import java.util.List;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/5/27  14:30.
 */

public class BowlBean  {

    /**
     * result : 200
     * matchoddlist : [{"time":"89","score":"2-2","odd":{"left":"0.47","middle":"0.00","right":"1.63","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"89","score":"2-2","odd":{"left":"0.44","middle":"0.00","right":"1.72","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"89","score":"2-2","odd":{"left":"0.43","middle":"0.00","right":"1.73","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"89","score":"2-2","odd":{"left":"0.44","middle":"0.00","right":"1.72","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"88","score":"2-2","odd":{"left":"0.47","middle":"0.00","right":"1.62","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"88","score":"2-2","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"88","score":"1-2","odd":{"left":"0.42","middle":"0.00","right":"1.76","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"88","score":"1-2","odd":{"left":"0.41","middle":"0.00","right":"1.81","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"88","score":"1-2","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"88","score":"1-2","odd":{"left":"1.96","middle":"0.25","right":"0.37","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"88","score":"1-2","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"86","score":"1-2","odd":{"left":"0.41","middle":"0.00","right":"1.80","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"86","score":"1-2","odd":{"left":"0.40","middle":"0.00","right":"1.85","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"86","score":"1-2","odd":{"left":"0.41","middle":"0.00","right":"1.80","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"86","score":"1-2","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"85","score":"0-2","odd":{"left":"1.61","middle":"0.25","right":"0.45","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"84","score":"0-2","odd":{"left":"1.61","middle":"0.25","right":"0.47","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"84","score":"0-2","odd":{"left":"1.54","middle":"0.25","right":"0.47","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"84","score":"0-2","odd":{"left":"1.54","middle":"0.25","right":"0.48","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"84","score":"0-2","odd":{"left":"1.54","middle":"0.25","right":"0.50","leftUp":1,"middleUp":0,"rightUp":1}},{"time":"82","score":"0-2","odd":{"left":"1.45","middle":"0.25","right":"0.46","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"82","score":"0-2","odd":{"left":"1.45","middle":"0.25","right":"0.48","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"82","score":"0-2","odd":{"left":"1.40","middle":"0.25","right":"0.49","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"82","score":"0-2","odd":{"left":"1.40","middle":"0.25","right":"0.50","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"82","score":"0-2","odd":{"left":"1.34","middle":"0.25","right":"0.52","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"81","score":"0-2","odd":{"left":"1.34","middle":"0.25","right":"0.53","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"81","score":"0-2","odd":{"left":"1.31","middle":"0.25","right":"0.53","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"81","score":"0-2","odd":{"left":"1.31","middle":"0.25","right":"0.55","leftUp":-1,"middleUp":0,"rightUp":-1}},{"time":"81","score":"0-2","odd":{"left":"1.36","middle":"0.25","right":"0.56","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"80","score":"0-2","odd":{"left":"1.36","middle":"0.25","right":"0.57","leftUp":1,"middleUp":0,"rightUp":1}},{"time":"80","score":"0-2","odd":{"left":"1.33","middle":"0.25","right":"0.55","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"80","score":"0-2","odd":{"left":"1.33","middle":"0.25","right":"0.56","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"80","score":"0-2","odd":{"left":"1.30","middle":"0.25","right":"0.56","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"79","score":"0-2","odd":{"left":"1.30","middle":"0.25","right":"0.58","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"79","score":"0-2","odd":{"left":"1.24","middle":"0.25","right":"0.59","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"78","score":"0-2","odd":{"left":"1.19","middle":"0.25","right":"0.60","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"78","score":"0-2","odd":{"left":"1.19","middle":"0.25","right":"0.63","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"77","score":"0-2","odd":{"left":"1.19","middle":"0.25","right":"0.64","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"77","score":"0-2","odd":{"left":"1.19","middle":"0.25","right":"0.66","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"76","score":"0-2","odd":{"left":"1.17","middle":"0.25","right":"0.71","leftUp":1,"middleUp":0,"rightUp":1}},{"time":"76","score":"0-2","odd":{"left":"1.13","middle":"0.25","right":"0.69","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"75","score":"0-2","odd":{"left":"1.13","middle":"0.25","right":"0.70","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"74","score":"0-2","odd":{"left":"1.06","middle":"0.25","right":"0.75","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"73","score":"0-2","odd":{"left":"1.06","middle":"0.25","right":"0.78","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"73","score":"0-2","odd":{"left":"1.06","middle":"0.25","right":"0.79","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"73","score":"0-2","odd":{"left":"1.06","middle":"0.25","right":"0.80","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"73","score":"0-2","odd":{"left":"1.04","middle":"0.25","right":"0.80","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"72","score":"0-2","odd":{"left":"1.00","middle":"0.25","right":"0.81","leftUp":-1,"middleUp":0,"rightUp":0}},{"time":"72","score":"0-2","odd":{"left":"1.03","middle":"0.25","right":"0.81","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"72","score":"0-2","odd":{"left":"1.03","middle":"0.25","right":"0.82","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"72","score":"0-2","odd":{"left":"1.02","middle":"0.25","right":"0.83","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"71","score":"0-2","odd":{"left":"1.02","middle":"0.25","right":"0.84","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"71","score":"0-2","odd":{"left":"1.01","middle":"0.25","right":"0.84","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"71","score":"0-2","odd":{"left":"1.00","middle":"0.25","right":"0.86","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"71","score":"0-2","odd":{"left":"0.99","middle":"0.25","right":"0.87","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"70","score":"0-2","odd":{"left":"0.97","middle":"0.25","right":"0.89","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"70","score":"0-2","odd":{"left":"0.96","middle":"0.25","right":"0.90","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"70","score":"0-2","odd":{"left":"0.95","middle":"0.25","right":"0.91","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"68","score":"0-2","odd":{"left":"0.91","middle":"0.25","right":"0.95","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"67","score":"0-2","odd":{"left":"0.90","middle":"0.25","right":"0.96","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"67","score":"0-2","odd":{"left":"0.89","middle":"0.25","right":"0.97","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"67","score":"0-2","odd":{"left":"0.88","middle":"0.25","right":"0.98","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"65","score":"0-2","odd":{"left":"0.82","middle":"0.25","right":"1.04","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"64","score":"0-2","odd":{"left":"0.81","middle":"0.25","right":"1.05","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"64","score":"0-2","odd":{"left":"0.80","middle":"0.25","right":"1.06","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"63","score":"0-2","odd":{"left":"0.79","middle":"0.25","right":"1.07","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"62","score":"0-2","odd":{"left":"0.76","middle":"0.25","right":"1.10","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"60","score":"0-2","odd":{"left":"0.74","middle":"0.25","right":"1.13","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"60","score":"0-2","odd":{"left":"0.73","middle":"0.25","right":"1.13","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"60","score":"0-2","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"58","score":"0-2","odd":{"left":"1.63","middle":"0.75","right":"0.47","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"57","score":"0-2","odd":{"left":"1.61","middle":"0.75","right":"0.48","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"57","score":"0-2","odd":{"left":"1.58","middle":"0.75","right":"0.49","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"56","score":"0-2","odd":{"left":"1.56","middle":"0.75","right":"0.50","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"55","score":"0-2","odd":{"left":"1.53","middle":"0.75","right":"0.51","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"54","score":"0-2","odd":{"left":"1.49","middle":"0.75","right":"0.53","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"54","score":"0-2","odd":{"left":"1.47","middle":"0.75","right":"0.54","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"53","score":"0-2","odd":{"left":"1.44","middle":"0.75","right":"0.55","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"53","score":"0-2","odd":{"left":"1.38","middle":"0.75","right":"0.56","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"53","score":"0-2","odd":{"left":"1.38","middle":"0.75","right":"0.57","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"52","score":"0-2","odd":{"left":"1.38","middle":"0.75","right":"0.58","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"52","score":"0-2","odd":{"left":"1.37","middle":"0.75","right":"0.58","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"52","score":"0-2","odd":{"left":"1.36","middle":"0.75","right":"0.59","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"51","score":"0-2","odd":{"left":"1.34","middle":"0.75","right":"0.60","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"50","score":"0-2","odd":{"left":"1.31","middle":"0.75","right":"0.62","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"50","score":"0-2","odd":{"left":"1.29","middle":"0.75","right":"0.63","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"49","score":"0-2","odd":{"left":"1.28","middle":"0.75","right":"0.64","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"49","score":"0-2","odd":{"left":"1.27","middle":"0.75","right":"0.64","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"49","score":"0-2","odd":{"left":"1.19","middle":"0.75","right":"0.70","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"49","score":"0-2","odd":{"left":"1.14","middle":"0.75","right":"0.73","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"48","score":"0-2","odd":{"left":"1.13","middle":"0.75","right":"0.74","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"48","score":"0-2","odd":{"left":"1.12","middle":"0.75","right":"0.75","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"47","score":"0-2","odd":{"left":"1.11","middle":"0.75","right":"0.76","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"47","score":"0-2","odd":{"left":"1.09","middle":"0.75","right":"0.77","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"47","score":"0-2","odd":{"left":"1.08","middle":"0.75","right":"0.78","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"48","score":"0-2","odd":{"left":"1.06","middle":"0.75","right":"0.80","leftUp":0,"middleUp":0,"rightUp":1}},{"time":"48","score":"0-2","odd":{"left":"1.06","middle":"0.75","right":"0.79","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"48","score":"0-2","odd":{"left":"1.02","middle":"0.75","right":"0.84","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"48","score":"0-2","odd":{"left":"1.06","middle":"0.75","right":"0.80","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"46","score":"0-2","odd":{"left":"1.02","middle":"0.75","right":"0.84","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"44","score":"0-2","odd":{"left":"0.99","middle":"0.75","right":"0.87","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"44","score":"0-2","odd":{"left":"0.98","middle":"0.75","right":"0.88","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"42","score":"0-2","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"39","score":"0-1","odd":{"left":"0.69","middle":"0.50","right":"1.20","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"37","score":"0-1","odd":{"left":"0.66","middle":"0.50","right":"1.22","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"37","score":"0-1","odd":{"left":"0.70","middle":"0.50","right":"1.19","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"35","score":"0-1","odd":{"left":"0.69","middle":"0.50","right":"1.20","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"35","score":"0-1","odd":{"left":"0.68","middle":"0.50","right":"1.21","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"33","score":"0-1","odd":{"left":"0.66","middle":"0.50","right":"1.22","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"33","score":"0-1","odd":{"left":"0.66","middle":"0.50","right":"1.24","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"33","score":"0-1","odd":{"left":"0.70","middle":"0.50","right":"1.19","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"32","score":"0-1","odd":{"left":"0.69","middle":"0.50","right":"1.20","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"32","score":"0-1","odd":{"left":"0.71","middle":"0.50","right":"1.16","leftUp":-1,"middleUp":0,"rightUp":0}},{"time":"31","score":"0-1","odd":{"left":"0.72","middle":"0.50","right":"1.16","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"30","score":"0-1","odd":{"left":"0.71","middle":"0.50","right":"1.17","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"29","score":"0-1","odd":{"left":"0.70","middle":"0.50","right":"1.18","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"29","score":"0-1","odd":{"left":"0.72","middle":"0.50","right":"1.16","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"29","score":"0-1","odd":{"left":"0.77","middle":"0.50","right":"1.09","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"27","score":"0-1","odd":{"left":"0.76","middle":"0.50","right":"1.11","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"27","score":"0-1","odd":{"left":"0.74","middle":"0.50","right":"1.13","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"26","score":"0-1","odd":{"left":"0.82","middle":"0.50","right":"1.04","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"26","score":"0-1","odd":{"left":"0.81","middle":"0.50","right":"1.05","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"25","score":"0-1","odd":{"left":"0.80","middle":"0.50","right":"1.06","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"24","score":"0-1","odd":{"left":"0.84","middle":"0.50","right":"1.02","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"24","score":"0-1","odd":{"left":"0.83","middle":"0.50","right":"1.03","leftUp":0,"middleUp":0,"rightUp":1}},{"time":"24","score":"0-1","odd":{"left":"0.83","middle":"0.50","right":"1.02","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"24","score":"0-1","odd":{"left":"0.83","middle":"0.50","right":"1.03","leftUp":0,"middleUp":0,"rightUp":1}},{"time":"24","score":"0-1","odd":{"left":"0.83","middle":"0.50","right":"0.97","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"24","score":"0-1","odd":{"left":"0.83","middle":"0.50","right":"1.00","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"23","score":"0-1","odd":{"left":"-","middle":"-","right":"-","leftUp":0,"middleUp":0,"rightUp":0}},{"time":"21","score":"0-0","odd":{"left":"1.39","middle":"0.75","right":"0.57","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"20","score":"0-0","odd":{"left":"1.36","middle":"0.75","right":"0.59","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"20","score":"0-0","odd":{"left":"1.31","middle":"0.75","right":"0.60","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"17","score":"0-0","odd":{"left":"1.31","middle":"0.75","right":"0.61","leftUp":0,"middleUp":0,"rightUp":-1}},{"time":"17","score":"0-0","odd":{"left":"1.31","middle":"0.75","right":"0.62","leftUp":1,"middleUp":0,"rightUp":0}},{"time":"17","score":"0-0","odd":{"left":"1.30","middle":"0.75","right":"0.62","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"16","score":"0-0","odd":{"left":"1.28","middle":"0.75","right":"0.63","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"13","score":"0-0","odd":{"left":"1.21","middle":"0.75","right":"0.68","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"11","score":"0-0","odd":{"left":"1.20","middle":"0.75","right":"0.69","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"10","score":"0-0","odd":{"left":"1.19","middle":"0.75","right":"0.70","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"9","score":"0-0","odd":{"left":"1.17","middle":"0.75","right":"0.71","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"7","score":"0-0","odd":{"left":"1.16","middle":"0.75","right":"0.72","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"6","score":"0-0","odd":{"left":"1.14","middle":"0.75","right":"0.73","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"5","score":"0-0","odd":{"left":"1.13","middle":"0.75","right":"0.74","leftUp":-1,"middleUp":0,"rightUp":1}},{"time":"5","score":"0-0","odd":{"left":"1.16","middle":"0.75","right":"0.72","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"4","score":"0-0","odd":{"left":"1.14","middle":"0.75","right":"0.73","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"2","score":"0-0","odd":{"left":"1.13","middle":"0.75","right":"0.74","leftUp":1,"middleUp":0,"rightUp":-1}},{"time":"2","score":"0-0","odd":{"left":"1.08","middle":"0.75","right":"0.78","leftUp":1,"middleUp":0,"rightUp":-1}}]
     */

    private int result;
    private List<MatchoddlistBean> matchoddlist;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<MatchoddlistBean> getMatchoddlist() {
        return matchoddlist;
    }

    public void setMatchoddlist(List<MatchoddlistBean> matchoddlist) {
        this.matchoddlist = matchoddlist;
    }

    public static class MatchoddlistBean {
        /**
         * time : 89
         * score : 2-2
         * odd : {"left":"0.47","middle":"0.00","right":"1.63","leftUp":0,"middleUp":0,"rightUp":0}
         */

        private String time;
        private String score;
        private OddBean odd;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public OddBean getOdd() {
            return odd;
        }

        public void setOdd(OddBean odd) {
            this.odd = odd;
        }

        public static class OddBean {
            /**
             * left : 0.47
             * middle : 0.00
             * right : 1.63
             * leftUp : 0
             * middleUp : 0
             * rightUp : 0
             */

            private String left;
            private String middle;
            private String right;
            private int leftUp;
            private int middleUp;
            private int rightUp;

            public String getLeft() {
                return left;
            }

            public void setLeft(String left) {
                this.left = left;
            }

            public String getMiddle() {
                return middle;
            }

            public void setMiddle(String middle) {
                this.middle = middle;
            }

            public String getRight() {
                return right;
            }

            public void setRight(String right) {
                this.right = right;
            }

            public int getLeftUp() {
                return leftUp;
            }

            public void setLeftUp(int leftUp) {
                this.leftUp = leftUp;
            }

            public int getMiddleUp() {
                return middleUp;
            }

            public void setMiddleUp(int middleUp) {
                this.middleUp = middleUp;
            }

            public int getRightUp() {
                return rightUp;
            }

            public void setRightUp(int rightUp) {
                this.rightUp = rightUp;
            }
        }
    }
}
