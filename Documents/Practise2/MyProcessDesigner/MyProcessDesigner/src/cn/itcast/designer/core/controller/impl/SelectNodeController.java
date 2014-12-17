/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.element.BaseNode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

/**
 *
 * @author tyg
 */
public class SelectNodeController extends ControllerAdapter {

    private Color rangeSelectRectColor = new Color(0x316BC6); // 矩形选择的边框颜色
    private Stroke rangeSelecteRectStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{5F, 5F}, 0.0f); // 虚线

    @Override
    public void doWhenMouseMoved(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT) {
            if (ec.getHelper().getFromAllNodeElementWhichContains(evt.getX(), evt.getY()) != null) {
                ec.getDesignerPanel().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 显示手型的光标
            } else {
                ec.getDesignerPanel().setCursor(CURSOR_DEFAULT);
            }
        }
    }

    //  鼠标按下时：适时取消选中或选中某些节点元素
    @Override
    public void doWhenMousePressed(ExecutionContext ec, MouseEvent evt) {
        // ，如果是选择功能，就适时的选中或操作元素
        if (ec.funcType == FuncType.SELECT) { // 选择功能
            // 如果当前点击的坐标不在的已选中某个元素中、且不在右下角的调整大小的手柄中时，才执行新选择功能。因为如果在，就是可能要执行移动功能
            BaseNode currentNode = ec.getHelper().getFromSelectedNodesWhichContains(evt.getX(), evt.getY());
            boolean isInSelectedNodeRightBottonCornerRect = ec.getHelper().isSelectedNodeRightBottonCornerRectContains(evt.getX(), evt.getY());
            if (currentNode == null && !isInSelectedNodeRightBottonCornerRect) {
                // 1，先消选择之前的元素
                deselectProcessNodeIfSelected(ec);
                // TODO 如果当未选中元素，就执行选择单个元素操作；如果当前选择了元素，也不在任一选中元素中，且不在4角的调整大小的手柄中，就是尝试选择一个元素了
                doSelectSingleProcessNode(ec, evt);
            }
        } else {
            // 如果要画元素，就取消所有已选的元素
            deselectProcessNodeIfSelected(ec);
        }
    }

    // 鼠标拖拽时：如果是矩形范围选择，就不断的重画虚线的矩形（范围）
    @Override
    public void doWhenMouseDragged(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT) {  // 选择功能
            if (!ec.hasSelectedNodes()) { // 如果当前还没有选中元素，就是使用范围方式选择元素，这里需要显示虚线的矩形
                // 1，设置画笔
                Graphics2D g2d = (Graphics2D) ec.getGraphics();
                g2d.setColor(rangeSelectRectColor);
                g2d.setStroke(rangeSelecteRectStroke); // 虚线

                // 2，先重画已有元素（反过来就会看不到正在拖拽的元素）
                ec.getHelper().repaintDesignerPanel();

                // 3，再画正在拖拽的元素
                Rectangle r = ec.getHelper().calcRectBounds(ec.startX, ec.startY, evt.getX(), evt.getY());
                g2d.drawRect(r.x, r.y, r.width, r.height);
            }
        }
    }

    // 选择矩形所完全包含的的多个流程节点，并显示小矩形手柄，如果没有选择新元素时，这个方法不会清除之前选择的元素。
    @Override
    public void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.SELECT) {
            if (!ec.hasSelectedNodes()) { // 如果没有选中元素，就执行选择操作
                // 1，计算出本次的选择范围
                Rectangle rangeRect = ec.getHelper().calcRectBounds(ec.startX, ec.startY, evt.getX(), evt.getY());

                // 2，找出当前选中的节点元素。（不用从后往前取，因为是选中所有完全包含的元素）
                for (BaseNode currentNode : ec.getAllNodes()) {
                    // 1，检测是否完全包含当前的的节点元素
                    BaseNode node = null;
                    if (rangeRect.contains(currentNode)) { // 如果本节点完全在选择范围内，就选中他（这样判断不包含宽或高为0的矩形）
                        node = currentNode;
                    } else if (currentNode.width == 0 || currentNode.height == 0) {
                        // 有的矩形只是一个点（宽高都是0），这时就判断是否包含元素的左上角
                        if (rangeRect.contains(currentNode.x, currentNode.y)) {
                            node = currentNode;
                        }
                    }

                    // 2，如果完全包含当前的的节点元素，就选中他并显示4角的小矩形
                    if (node != null) {
                        //  让当前选中的元素显示小矩形手柄，并记下最新的选中元素
                        node.setShowCornerRectHandle(true);
                        ec.getSelectedNodes().add(node);
                    }
                }
                // TODO      if (mainController.selectedProcessNodeList.size() == 1) {
                //    让属性按钮可用 designerPanel.processDesigner.setConfigurePropertiesButtonEnable(true);}
            }
            ec.needRepaint(); // 需要重绘
        }
    }

    /**
     * 选择指定坐标所在的一个节点元素，如果在某一个节点元素中的话。选中后，显示小矩形手柄，如果没有选择新节点元素时，这个方法不会清除之前选择的元素。
     * @param x
     * @param y
     */
    public void doSelectSingleProcessNode(ExecutionContext ec, MouseEvent evt) {
        // 1，获取当前点击所在的元素（从后往前取，以先获取上层的元素）
        BaseNode currentSelectedNode = ec.getHelper().getFromAllNodeElementWhichContains(evt.getX(), evt.getY());

        // 2，如果选中了元素
        if (currentSelectedNode != null) {
            // 1，把当前选中的元素记下来，并显示小矩形手柄
            currentSelectedNode.setShowCornerRectHandle(true);
            ec.getSelectedNodes().add(currentSelectedNode);
            // 2，需要重画
            ec.needRepaint();
            // TODO 让属性按钮可用 designerPanel.processDesigner.setConfigurePropertiesButtonEnable(true);
        }
    }

    /**
     * 取消选择现在已选择的节点元素
     */
    public void deselectProcessNodeIfSelected(ExecutionContext ec) {
        for (BaseNode prevSelectedNode : ec.getSelectedNodes()) {
            prevSelectedNode.setShowCornerRectHandle(false); // 不再显示4角的矩形
        }
        ec.getSelectedNodes().clear();
        // TODO 让属性按钮不可用 designerPanel.processDesigner.setConfigurePropertiesButtonEnable(false);
    }

//    public static void main(String[] args) {
//        Rectangle rangeRect = new Rectangle(0, 0, 10, 10);
//        boolean b = rangeRect.contains(new Rectangle(0, 0, 1, 0));
//        System.out.println("b = " + b);
//    }
}
