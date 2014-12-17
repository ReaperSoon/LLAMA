/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.core.model.element.node.EndNode;
import cn.itcast.designer.core.model.element.node.StartNode;
import cn.itcast.designer.core.model.element.node.TaskNode;
import cn.itcast.designer.core.util.NodeUtils;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 * @author tyg
 */
public class DrawNodeController extends ControllerAdapter {

    // 鼠标拖拽时：不断的更新正在画的节点
    @Override
    public void doWhenMouseDragged(ExecutionContext ec, MouseEvent evt) {
        // 1，获得当前正在画的节点的类型
        Class clazz = getDrawingNodeType(ec);

        // 2，如果当前正在画节点
        if (clazz != null) {
            // 1，计算出矩形范围
            Rectangle r = ec.getHelper().calcRectBounds(ec.startX, ec.startY, evt.getX(), evt.getY());
            // 2，先重画，再画出从开始点到当前坐标大小的当前节点元素。
            ec.getHelper().repaintDesignerPanel();
            NodeUtils.drawNode(clazz, ec.getGraphics(), r.x, r.y, r.width, r.height);
        }
    }

    //鼠标释放时：完成画节点的操作
    @Override
    public void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt) {
        // 1，获得当前正在画的节点的类型
        Class clazz = getDrawingNodeType(ec);

        // 2，如果是正在画节点，则添加新画好的节点元素
        if (clazz != null) {
            // 1，计算出矩形范围，生成新节点对象
            Rectangle r = ec.getHelper().calcRectBounds(ec.startX, ec.startY, evt.getX(), evt.getY());
            BaseNode newNode = NodeUtils.newProcessNode(clazz, r.x, r.y, r.width, r.height);
            //  2，添加新节点
            ec.addNewElement(newNode);
            System.out.println("newNode: " + newNode);
            ec.needRepaint(); // 需要重绘
        }
    }

    /**
     * 获取当前正在画的节点的类型，如果不是画流程节点元素，就返回null
     * @param ec
     * @return
     */
    private Class<? extends BaseNode> getDrawingNodeType(ExecutionContext ec) {
        Class clazz = null;
        if (ec.funcType == FuncType.PROCESS_NODE_START) {
            clazz = StartNode.class;
        } else if (ec.funcType == FuncType.PROCESS_NODE_END) {
            clazz = EndNode.class;
        } else if (ec.funcType == FuncType.PROCESS_NODE_TASK) {
            clazz = TaskNode.class;
        }
        return clazz;
    }
}
