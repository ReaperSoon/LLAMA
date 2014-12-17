/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.element;

import cn.itcast.designer.core.dialog.PropertiesPanel;
import cn.itcast.designer.core.dialog.impl.NamePropertiesPanel;
import cn.itcast.designer.core.model.base.ProcessElement;
import cn.itcast.designer.core.util.Shape2DUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/**
 *
 * @author tyg
 */
public class Transition extends ProcessElement {
    // --------------------------------------------
    // 连接从哪个元素到哪个元素，是有方向的

    private BaseNode from;
    private BaseNode to;

    /**
     * 画一条从fromNode的中心点到指定坐标的连线，
     * @param g
     * @param fromNode
     * @param x2
     * @param y2
     */
    public static void drawTranstionFromConterPointTo(Graphics g, BaseNode fromNode, int x2, int y2) {
        // 设置线条宽度
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f));

        // 画线从from到to
        Point p1 = fromNode.getCenterPoint();
        g.drawLine(p1.x, p1.y, x2, y2);
    }

    /**
     * 画一条从fromNode到toNode的连线：显示从From图形到To图形的单向箭头线段，是连接两个图形中心点的线段，但不包含与图形重叠的部分。
     * @param g
     * @param fromNode
     * @param toNode
     */
    public static void drawTransition(Graphics g, BaseNode fromNode, BaseNode toNode) {
        // 1，获取两个节点元素的中心点坐标
        Point p1 = fromNode.getCenterPoint();
        Point p2 = toNode.getCenterPoint();

        // 2，找出连线与fromNode重叠的点
        Point lineFromPoint = null;
        for (Point p3 : Shape2DUtils.getBoundsPoints(fromNode)) {
            // 在同一个象限中（排除在延长线上的可能，注意坐标原点不能弄错），并且在同一条线上，就是这个点。
            if (Shape2DUtils.isInSameQuadrant(p1, p2, p3) && Shape2DUtils.isInOneLine(p1, p2, p3)) {
                lineFromPoint = p3;
                break;
            }
        }

        // 3，找出连线与toNode重叠的点
        Point lineToPoint = null;
        for (Point p3 : Shape2DUtils.getBoundsPoints(toNode)) {
            // 在同一个象限中（排除在延长线上的可能，注意坐标原点不能弄错），并且在同一条线上，就是这个点。
            if (Shape2DUtils.isInSameQuadrant(p2, p1, p3) && Shape2DUtils.isInOneLine(p1, p2, p3)) {
                lineToPoint = p3;
                break;
            }
        }

        if (lineFromPoint == null || lineToPoint == null) {
            System.out.println(" lineFromPoint = " + lineFromPoint + "\t lineToPoint = " + lineToPoint);
            System.out.println("From图形：" + fromNode);
            System.out.println("To图形  ：" + toNode);
            System.out.println("From中心点：" + p1);
            System.out.println("To中心点  ：" + p2);
        }

        // 4，画连线
        {
            Graphics2D g2d = (Graphics2D) g;

            //画连线与元素重叠的部分（应该不画）
//            g2d.setColor(Color.lightGray);
//            g2d.drawLine(p1.x, p1.y, lineFromPoint.x, lineFromPoint.y); // From中的部分线段
//            g2d.drawLine(p2.x, p2.y, lineToPoint.x, lineToPoint.y); // To中的部分线段

            if (lineFromPoint != null && lineToPoint != null) {
                // 1，设置线条宽度
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f));

                // 2，画线从from到to，不包含与两个元素重叠的部分
                g2d.drawLine(lineFromPoint.x, lineFromPoint.y, lineToPoint.x, lineToPoint.y);

                // 3，TODO 画一个高为15，角度为30度的三角箭头
//                int x = lineToPoint.x;
//                int y = lineToPoint.y;
//                g2d.fillRect(x - 5, y - 5, 10, 10);
                drawArrow(g2d, lineFromPoint.x, lineFromPoint.y, lineToPoint.x, lineToPoint.y);
            }
        }
    }

    /**
     * TODO 从别处拷过来的，还没有细看
     * @param g2
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private static void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double len = 15.0D;
        double slopy = Math.atan2(y2 - y1, x2 - x1);
        double cosy = Math.cos(slopy);
        double siny = Math.sin(slopy);
        int[] xPoints = {0, x2, 0};
        int[] yPoints = {0, y2, 0};
        double a = len * siny;
        double b = len * cosy;
        double c = len / 2.5 * siny;
        double d = len / 2.5 * cosy;
        xPoints[0] = (int) Math.round(x2 - (b + c));
        yPoints[0] = (int) Math.round(y2 - (a - d));
        xPoints[2] = (int) Math.round(x2 - (b - c));
        yPoints[2] = (int) Math.round(y2 - (d + a));

        // TODO 通过该方法使图形去除锯齿状
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    public PropertiesPanel getPropertiesPanel() {
        return new NamePropertiesPanel();
    }

    public Transition(BaseNode from, BaseNode to) {
        // 设置双向的关系
        this.from = from;
        this.to = to;
        from.getLeavingTransitions().add(this);
        to.getArrivingTransitions().add(this);
    }

    public void drawMe(Graphics g) {
        Transition.drawTransition(g, from, to);
    }

    public String toXml() {
        StringBuffer xml = new StringBuffer("<transition ");
        if (getName() != null) {
            xml.append("name=\"" + getName() + "\" ");
        }
        xml.append("to=\"" + to.getName() + "\" ");
        xml.append("g=\"-?,-?\"/>"); // TODO Transition的坐标只有两个数字，是什么意思？
        return xml.toString();
    }
}
