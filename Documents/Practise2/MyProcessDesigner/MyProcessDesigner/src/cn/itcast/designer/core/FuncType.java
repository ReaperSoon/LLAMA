/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core;

/**
 * 功能类型
 * @author tyg
 */
public enum FuncType {

    /**
     * 选择功能
     */
    SELECT,
    /**
     * 画连线
     */
    PROCESS_TRANSITION,
    /**
     * 画任务节点
     */
    PROCESS_NODE_TASK,
    /**
     * 画开始节点
     */
    PROCESS_NODE_START,
    /**
     * 画结束结点
     */
    PROCESS_NODE_END,
}
