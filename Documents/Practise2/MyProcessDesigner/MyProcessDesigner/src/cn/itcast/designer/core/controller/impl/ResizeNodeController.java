/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.element.BaseNode;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

/**
 *
 * @author tyg
 */
public class ResizeNodeController extends ControllerAdapter {

    /**
     * 当前正做调整大小操作的节点元素
     */
    private BaseNode currentResizingNode;

    // 鼠标移动时： 如果当前选择了图形，并且移到了可改变大小的实心手柄上（可调整大小的），就显示相应的光标
    @Override
    public void doWhenMouseMoved(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT && ec.hasSelectedNodes()) {
            // 根据情况改变光标
            if (ec.getHelper().isSelectedNodeRightBottonCornerRectContains(evt.getX(), evt.getY())) {
                ec.getDesignerPanel().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            } else {
                //  TODO 在SelectController中会设置光标
//                ec.getDesignerPanel().setCursor(CURSOR_DEFAULT);
            }
        }
    }

    // 鼠标按下时：记下当前要调整大小的节点
    @Override
    public void doWhenMousePressed(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT) { // 选择功能
            for (BaseNode node : ec.getSelectedNodes()) {
                if (node.getRightBottomCornerRectHandle().contains(evt.getX(), evt.getY())) {
                    currentResizingNode = node; // 记下当前要调整大小的元素
                    break;
                }
            }
        }
    }

    // 鼠标拖拽时：不停的调整指定节点的大小
    @Override
    public void doWhenMouseDragged(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT) { // 选择功能
            if (currentResizingNode != null) { // TODO 不需要再判断是否是选择功能，因为在MousePressed时已判断过了 ??
                // 通过右下角的矩形手柄调整图形大小
                currentResizingNode.resizeByRightBottomCorner(evt.getX(), evt.getY());
                ec.needRepaint();
            }
        }
    }

    // 完成调整大小操作，还原相关状态
    @Override
    public void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt) {
        if (currentResizingNode != null) {
            // TODO currentResizingShape.adjustCorner();
            currentResizingNode = null;
            ec.needRepaint();
        }
    }
}
