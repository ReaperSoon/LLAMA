/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.view.MainFrame;
import java.awt.event.MouseEvent;

/**
 *
 * @author tyg
 */
public class ConfigureNodeController extends ControllerAdapter {

    /**
     * 属性按钮所在的Frame
     */
    private MainFrame mainFrame;

    public ConfigureNodeController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * TODO 对选中的单个元素进行配置（没有选中或多选中不可用） TODO 目前只能对节点进行配置，应也能对连线配置
     */
    @Override
    public void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt) {
        // 只有选中一个节点时，“属性”按钮可用
        if (ec.funcType == FuncType.SELECT && ec.getSelectedNodes().size() == 1) {
            mainFrame.getBtnProperties().setEnabled(true);
        } else {
            mainFrame.getBtnProperties().setEnabled(false);
        }
    }
}
