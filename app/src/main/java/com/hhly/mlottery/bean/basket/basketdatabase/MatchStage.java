package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */

public class MatchStage {

    /**
     * stageId : 154
     * stageName : 常规赛
     * stages : [{"secondStageId":"265","secondStageName":"2016-04"},{"secondStageId":"289","secondStageName":"2016-03"}]
     */

    private String stageId;
    private String stageName;
    private List<MatchStage> stages;

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public List<MatchStage> getStages() {
        return stages;
    }

    public void setStages(List<MatchStage> stages) {
        this.stages = stages;
    }
}
