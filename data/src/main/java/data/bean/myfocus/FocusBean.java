package data.bean.myfocus;

import java.util.List;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/26  9:45.
 */

public class FocusBean {

    private String name;

    private boolean isSelected;

    private List<Bean> list;


    public List<Bean> getList() {
        return list;
    }

    public void setList(List<Bean> list) {
        this.list = list;
    }

    public FocusBean(String name, boolean isSelected, List<Bean> list) {
        this.name = name;
        this.isSelected = isSelected;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

   public static class Bean {
        private String name;

        private boolean isSelected;

        public Bean(String name, boolean isSelected) {
            this.name = name;
            this.isSelected = isSelected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
