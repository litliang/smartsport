package com.lecloud.skin.ui;


public interface ILetvLiveUICon extends ILetvUICon {
    void setLetvLiveUIListener(LetvLiveUIListener mLetvLiveUIListener);

    void setTimeShiftChange(long serverTime, long currentTime, long begin);

    void showController(boolean b);

    void setMachineListener(ILetvSwitchMachineListener machineListener);

    void setVideoDrag(boolean isVideoDrag);

    void setLiveUIOnClickListener();

}

