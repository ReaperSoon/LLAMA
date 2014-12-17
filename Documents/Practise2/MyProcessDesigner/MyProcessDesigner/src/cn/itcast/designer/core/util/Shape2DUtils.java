/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * 2D图形有关的工具方法
 * @author tyg
 */
public class Shape2DUtils {

    /**
     * 获取一个矩形中所有边界上的点
     * @param rect
     * @return
     */
    public static List<Point> getBoundsPoints(Rectangle rect) {
        List<Point> list = new ArrayList();
        int x1 = rect.x;
        int y1 = rect.y;
        int x2 = rect.x + rect.width;
        int y2 = rect.y + rect.height;

        // 两个竖线上的点（含4角的点）
        for (int y = y1; y < y2; y++) {
            list.add(new Point(x1, y));
            list.add(new Point(x2, y));
        }

        // 两个横线上的点（不含4角的点）
        for (int x = x1 + 1; x < x2 - 1; x++) {
            list.add(new Point(x, y1));
            list.add(new Point(x, y2));
        }
        return list;
    }

    /**
     * 计算两点之间的距离
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(Point p1, Point p2) {
        double a = Math.pow((p2.x - p1.x), 2);
        double b = Math.pow((p2.y - p1.y), 2);
        return Math.pow(a + b, 0.5); // 不能写 1/2 ，因为 1/2=0 （都是int型）
    }

    /**
     * 检测指定两点是否在同一个象限内或是在某轴上（以origin为原点）
     * @param origin 坐标原点
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isInSameQuadrant(Point origin, Point p1, Point p2) {
        if ((p1.x - origin.x) * (p2.x - origin.x) >= 0 && (p1.y - origin.y) * (p2.y - origin.y) >= 0) { // 积都等于0说明要轴上
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断指定的三点是否在同一条直线上
     * @param originPoint 充当坐标原点
     * @param p2
     * @param p3
     * @return
     */
    // 有直线上两点A、B，点C与AB是同一条直线的条件是：AC+CB=AB || |AB-AC|=CB （要注意锐角三角形与钝角三角形）
    // 应使用斜率判断
    public static boolean isInOneLine(Point p1, Point p2, Point p3) {
        // 再判断是否在同一条直线中
        double a = getDistance(p1, p2);
        double b = getDistance(p2, p3);
        double c = getDistance(p3, p1);

        //System.out.println("a=" + a + ", b=" + b + ", c=" + c + ", a+b=" + (a + b) + ", a-b=" + (a - b));
        double precision = 0.05;
        return (Math.abs(a + b - c) < precision) || (Math.abs(Math.abs(a - b) - c) < precision);
    }
}
