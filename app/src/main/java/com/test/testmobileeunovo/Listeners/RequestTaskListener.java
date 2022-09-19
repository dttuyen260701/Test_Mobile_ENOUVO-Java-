package com.test.testmobileeunovo.Listeners;

public interface RequestTaskListener {
    public void onPre();
    public void onEnd(boolean value, boolean done, int next_id);
}
