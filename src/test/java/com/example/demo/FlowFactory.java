package com.example.demo;

/**
 * @author huzipeng
 * @version 1.0
 */
public class FlowFactory {
    public static Flow get(int type) {
        Flow flow = new Flow();
        switch (type) {
            case 1:
                flow.name = "基础流程";
                flow.isDeductInventory = true;
                flow.isLottery = true;
                return flow;
            case 2:
                flow.name = "基础流程";
                flow.isDeductInventory = false;
                flow.isLottery = true;
                break;
            default:
                break;
        }
        return null;
    }
}
