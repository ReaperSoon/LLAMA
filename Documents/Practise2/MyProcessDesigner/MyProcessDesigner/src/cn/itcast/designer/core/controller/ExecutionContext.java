/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.model.base.ProcessElement;
import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.view.DesignerPanel;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tyg
 */
public class ExecutionContext {

    private static ExecutionContext instance;
    /**
     * 鼠标按下时的点（开始点）的坐标
     */
    public int startX;
    public int startY;
    /**
     * 当前正在使用功能的类型
     */
    public FuncType funcType;
    /**
     * 当前已有的所有元素（TODO 把节点与连线分开保存？）
     */
    private List<ProcessElement> allElements = new ArrayList<ProcessElement>();
    /**
     * 当前选中的流程节点元素
     */
    private List<BaseNode> selectedNodes = new ArrayList<BaseNode>();
    /**
     * 是否需要重绘画图区
     */
    private boolean needRepaint = false;
    /**
     * 绘图区
     */
    private DesignerPanel designerPanel;
    /**
     * 辅助类、工具类
     */
    private ControllerHelper helper;

    /**
     * 外面不能随意创建实例，实例全局应只有一个
     * @param designerPanel
     */
    private ExecutionContext(DesignerPanel designerPanel) {
        this.designerPanel = designerPanel;
        this.helper = new ControllerHelper(this);
    }

    /**
     * 获取实例（单例）
     * @param designerPanel
     * @return
     */
    public static ExecutionContext getInstance(DesignerPanel designerPanel) {
        synchronized (ExecutionContext.class) {
            if (instance == null) {
                instance = new ExecutionContext(designerPanel);
            }
        }
        return instance;
    }

    /**
     * 鼠标按下时：记下开始点的坐标
     */
    public void setStartPoint(int x, int y) {
        startX = x;
        startY = y;
    }

    /**
     * 设置需要重绘画图区
     */
    public void needRepaint() {
        this.needRepaint = true;
    }

    /**
     * 流程图中添加一个新元素
     */
    public void addNewElement(ProcessElement e) {
        this.allElements.add(e);
    }

    /**
     * 检测当前是否选中的某些节点元素
     * @return
     */
    public boolean hasSelectedNodes() {
        return this.selectedNodes.size() > 0;
    } 

    /**
     * 获取当前所有的节点类型的元素
     * @return
     */
    public List<BaseNode> getAllNodes() {
        List<BaseNode> nodes = new ArrayList<BaseNode>();
        for (ProcessElement e : allElements) {
            if (e instanceof BaseNode) {
                nodes.add((BaseNode) e);
            }
        }
        return nodes;
    }

    // =================================
    public ControllerHelper getHelper() {
        return helper;
    }

    public DesignerPanel getDesignerPanel() {
        return designerPanel;
    }

    public Graphics getGraphics() {
        // 一定要每次都重新获取才可以
        return designerPanel.getGraphics();
    }

    public List<ProcessElement> getAllElements() {
        return allElements;
    }

    public List<BaseNode> getSelectedNodes() {
        return selectedNodes;
    }

    public boolean isNeedRepaint() {
        return needRepaint;
    }

    public void setNeedRepaint(boolean needRepaint) {
        this.needRepaint = needRepaint;
    }
}
