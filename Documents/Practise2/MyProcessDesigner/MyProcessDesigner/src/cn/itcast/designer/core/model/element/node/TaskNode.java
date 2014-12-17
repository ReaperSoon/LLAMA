/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.element.node;

import cn.itcast.designer.core.dialog.PropertiesPanel;
import cn.itcast.designer.core.dialog.impl.TaskPropertiesPanel;
import cn.itcast.designer.core.model.element.TextNode;
import cn.itcast.designer.core.model.element.Transition;
import cn.itcast.designer.core.util.NodeUtils;
import java.awt.Graphics;

/**
 *
 * @author tyg
 */
public class TaskNode extends TextNode {

    private static int count = 0;

    public TaskNode() {
        setName("Task" + (++count)); // 任务名就是显示的文本
        canResizeByRightBottomCorner = true;
    }

    public PropertiesPanel getPropertiesPanel() {
        return new TaskPropertiesPanel();
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        NodeUtils.drawNode(TaskNode.class, g, x, y, width, height);
    }

    @Override
    public String toXml() {
        String assignee = getProperty("assignee");
        StringBuffer xml = new StringBuffer("<task name=\"" + getName() + "\" assignee=\"" + assignee + "\" g=\"" + getCoordinateStringForXml() + "\">").append("\n");//
        for (Transition t : getLeavingTransitions()) {
            xml.append(t.toXml()).append("\n");
        }
        xml.append("</task>");
        return xml.toString();
    }
}
