/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.element.BaseNode;
import java.awt.event.MouseEvent;

/**
 * TODO 需要依赖SelectNode功能，使用Command方式更好？
 * @author tyg
 */
public class MoveNodeController extends ControllerAdapter {

    /**
     * 是否能移动
     */
    private boolean canMove = false;

    @Override
    public void doWhenMousePressed(ExecutionContext ec, MouseEvent evt) {
        // 先还原状态
        canMove = false;

        if (ec.funcType == FuncType.SELECT && ec.hasSelectedNodes()) { // 如果选择了元素
            // 如果当前坐标在某个选中的元素中，就是可移动状态
            BaseNode node = ec.getHelper().getFromSelectedNodesWhichContains(evt.getX(), evt.getY());
            if (node != null) {
                canMove = true;
            }
        }
    }

    // 移动选中的所有节点（如果需要并可以移动）
    @Override
    public void doWhenMouseDragged(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT && ec.hasSelectedNodes()) { // 如果选择了元素
            // TODO 如果不是调整图形大小!!，且当前坐标在某一选中元素中，就是移动所有已选元素
            if (canMove) {
                // 移动所有已选择的节点
                for (BaseNode node : ec.getSelectedNodes()) {
                    node.x = (node.x + evt.getX() - ec.startX);
                    node.y = (node.y + evt.getY() - ec.startY);
                }
                ec.startX = evt.getX();
                ec.startY = evt.getY();
                ec.needRepaint(); // 需要重绘
            }
        }
    }

    @Override
    public void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt) {
        if (canMove) {
            canMove = false;
        }
    }
}
