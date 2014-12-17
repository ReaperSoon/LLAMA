/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.util;

import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.core.model.element.node.EndNode;
import cn.itcast.designer.core.model.element.node.StartNode;
import cn.itcast.designer.core.model.element.node.TaskNode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author tyg
 */
public class NodeUtils {

    /**
     * 生成对应类型（ProcessNode子类）的对象实例
     * @param clazz
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static BaseNode newProcessNode(Class<? extends BaseNode> clazz, int x, int y, int width, int height) {
        try {
            BaseNode node = (BaseNode) clazz.newInstance();
            node.x = x;
            node.y = y;
            node.width = width;
            node.height = height;

            return node;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * TODO 画出指定类型的节点  ， 应由具体Node子类实现？但这样怎么实现画节点时不停的更新节点？
     * @param clazz
     * @param g
     * @param x 左上角的x坐标
     * @param y 左上角的y坐标
     * @param width
     * @param height
     */
    public static void drawNode(Class clazz, Graphics g, int x, int y, int width, int height) {
        if (clazz == StartNode.class) {
            //画开始节点（圆形）
            g.drawOval(x, y, width, height);
        } else if (clazz == EndNode.class) {
            // 画结束节点（双圆形）
            g.drawOval(x + 5, y + 5, width - 10, height - 10);
            g.drawOval(x, y, width, height);
        } else if (clazz == TaskNode.class) {
            //画任务节点（圆角矩形）
            Graphics2D g2d = (Graphics2D) g;
              // TODO 通过该方法使图形去除锯齿状
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//            g2d.setColor(new Color(0xFFFFCE)); // 设置背景色
//            g.fillRoundRect(x, y, width, height, 20, 20);

             g2d.setColor(new Color(0x006B9C)); // 设置前景色，jbpm4-GPD的Task的边框颜色
//            g2d.setColor(new Color(0x7B7B7B)); // 设置前景色，jbpm4-Signavio的Task的边框颜色
            g2d.setStroke(new BasicStroke(1.5F));
            g.drawRoundRect(x, y, width, height, 20, 20);

             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }
}
