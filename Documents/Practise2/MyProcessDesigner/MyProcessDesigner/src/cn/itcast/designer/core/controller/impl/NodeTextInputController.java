/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller.impl;

import cn.itcast.designer.core.controller.ControllerAdapter;
import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.element.TextNode;
import cn.itcast.designer.core.model.element.BaseNode;
import java.awt.event.MouseEvent;

/**
 *
 * @author tyg
 */
public class NodeTextInputController extends ControllerAdapter {

    /**
     * 当前正在显示文本输入框的节点（TODO 用Element？（因为Transition也可以输入文本））
     */
    public TextNode currentShowingTextInputFieldNode;

    // 鼠标双击时：处理显示文本输入框有关的事情
    @Override
    public void doWhenMouseDoubleClickButton1(ExecutionContext ec, MouseEvent evt) {
        // TODO 鼠标左键双击 if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
        BaseNode node = ec.getHelper().getFromAllNodeElementWhichContains(evt.getX(), evt.getY());
        if (node != null && node instanceof TextNode) {

            // 如果当前双击点的坐标在一个文本矩形中，就输入显示文本字段让用户输入文字（这个元素在MousePressed时已中被选中了）
            currentShowingTextInputFieldNode = ((TextNode) node);
            currentShowingTextInputFieldNode.showTextInputField(ec.getDesignerPanel());
            ec.needRepaint(); // 需要重绘
        }
    }

    // 鼠标按下时：如果当前正在显示元素的文本输入框，就完成文字输入并隐藏文本输入框
    @Override
    public void doWhenMousePressed(ExecutionContext ec, MouseEvent evt) {
        if (currentShowingTextInputFieldNode != null) {
            currentShowingTextInputFieldNode.completeTextInput(ec.getDesignerPanel());
            currentShowingTextInputFieldNode = null;
        }
    }
}
