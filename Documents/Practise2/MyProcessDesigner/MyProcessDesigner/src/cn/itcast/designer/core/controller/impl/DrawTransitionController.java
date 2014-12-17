/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.FuncType;
import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.core.model.element.Transition;
import java.awt.Color;
import java.awt.event.MouseEvent; 

/**
 *
 * @author tyg
 */
public class DrawTransitionController extends ControllerAdapter {

    /**
     * 当前正在画的连线的From节点
     */
    public BaseNode fromNode;
    /**
     * 当前正在画的连线的To节点（刚移动到的节点，但还未点击）
     */
    public BaseNode toNode;
    /**
     * 当前正在显示中心圆的节点
     */
    public BaseNode currentShowingCenterCircleNode;

    // 鼠标移动时：清除上次正在显示的节点的中心圆，并显示光标所在的节点的中心圆
    @Override
    public void doWhenMouseMoved(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.PROCESS_TRANSITION) { // 画连线
            // 1，先重画，以便擦除上次画的节点的中心圆 // TODO 如果没有移出正在显示的节点上，就不用这么样擦除再重画了
            if (currentShowingCenterCircleNode != null) {
                currentShowingCenterCircleNode.setShowCenterCircle(false);
                currentShowingCenterCircleNode = null;
                ec.needRepaint();
            }

            // 2，在移到某元素上时，在元素中点显示一个小圆点
            BaseNode node = ec.getHelper().getFromAllNodeElementWhichContains(evt.getX(), evt.getY());
            if (node != null) {
                node.setShowCenterCircle(true);  // 让元素在中央显示一个小圆
                currentShowingCenterCircleNode = node;
                ec.needRepaint();
            }
        }
    }

    // 鼠标按下时：开始画连线，记下连线的开始节点，并在开始节点中显示红色的中心圆
    @Override
    public void doWhenMousePressed(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.PROCESS_TRANSITION) { // 画连线
            BaseNode node = ec.getHelper().getFromAllNodeElementWhichContains(evt.getX(), evt.getY());
            if (node != null) {
                node.setCenterCircleColor(Color.RED); // 显示红色的中心圆
                node.setShowCenterCircle(true);
                fromNode = node;
                ec.needRepaint();
            }
        }
    }

    // 鼠标拖拽时：更新正在画的连线
    @Override
    public void doWhenMouseDragged(ExecutionContext ec, MouseEvent evt) {
        if (fromNode != null) {  // 不停的更新连线，不需要判断是否是画连线状态，因为在MousePressed时已判断过了
            // 1，先重画，以便擦除上次画的小圆点
            if (toNode != null) { // From元素是要一直显示中心圆的
                toNode.setShowCenterCircle(false);
                toNode = null;
            }

            // 2，找出当前坐标所在的节点，并在光标所在的节点中显示中心圆
            toNode = ec.getHelper().getFromAllNodeElementWhichContains(evt.getX(), evt.getY());
            if(toNode == fromNode){
                toNode = null;  // 如果to和from是一个节点，就不需要重复显示中心圆
            }
            if (toNode != null) {
                toNode.setShowCenterCircle(true); // 显示中心圆
            }

            // 3，显示从From节点到当前位置的连线
            ec.getHelper().repaintDesignerPanel(); // 需要先重画
            Transition.drawTranstionFromConterPointTo(ec.getGraphics(), fromNode, evt.getX(), evt.getY());
        }
    }

    // 鼠标释放时：添加或取消一个Transition
    @Override
    public void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt) {
        if (ec.funcType == FuncType.PROCESS_TRANSITION) { // 画连线
            //  1，如果有From与To元素，且不是同一个元素，就创建并添加一个新的连线元素
            if (fromNode != null && toNode != null && fromNode != toNode) {
                Transition transition = new Transition(fromNode, toNode);
                ec.addNewElement(transition); // 添加
            }

            // 2，不再显示小中心圆
            if (fromNode != null) {
                fromNode.setShowCenterCircle(false);
                fromNode.setCenterCircleColor(Color.BLUE); // TODO 还原中心点的默认颜色
                fromNode = null;
            }
            if (toNode != null) {
                toNode.setShowCenterCircle(false);
                toNode = null;
            }

            // 3，需要重新显示
            ec.needRepaint();
        }
    }
}
