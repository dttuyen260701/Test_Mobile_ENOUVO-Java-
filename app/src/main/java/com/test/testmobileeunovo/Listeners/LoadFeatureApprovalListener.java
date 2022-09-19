package com.test.testmobileeunovo.Listeners;


import com.test.testmobileeunovo.Models.Feature_Approval;

import java.util.ArrayList;

public interface LoadFeatureApprovalListener {

    public void onPre();
    public void onEnd(ArrayList<Feature_Approval> list_result, boolean done);
}
