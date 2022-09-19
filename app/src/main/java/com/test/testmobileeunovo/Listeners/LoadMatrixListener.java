package com.test.testmobileeunovo.Listeners;

import com.test.testmobileeunovo.Models.Matrix;

import java.util.ArrayList;

public interface LoadMatrixListener {
    public void onPre();
    public void onEnd(ArrayList<Matrix> list_result, boolean done);
}
