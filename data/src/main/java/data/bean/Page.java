package data.bean;

/**
 * 描    述：Page实体类
 * 作    者：mady@13322.com
 * 时    间：2017/6/6
 */
public class Page {

    public int index;
    public int size;

    public Page() {
    }

    public Page(int index, int size) {
        this.index = index;
        this.size = size;
    }
}
