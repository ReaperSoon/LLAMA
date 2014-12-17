/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.element;

import cn.itcast.designer.core.model.base.ProcessElement;
import cn.itcast.designer.core.model.element.Transition;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * 父类有方法：
 * setLocation(int x, int y);       用于移动
 *
 * @author tyg
 */
public abstract class BaseNode extends ProcessElement {

    /**
     * 所有指向当前元素的连线
     */
    private List<Transition> arrivingTransitions = new ArrayList<Transition>();
    /**
     * 从当前元素指向其他元素的所有连线
     */
    private List<Transition> leavingTransitions = new ArrayList<Transition>();
    //--------------------------------------------
    //--------------------------------------------
    // 是否显示调整大小用的小矩形手柄，用于标示已选中或改变大小
    // 4角的矩形手柄的宽度
    // 4角的矩形手柄的高度
    private boolean showCornerRectHandle = false;
    private int cornerRectHandleWidth = 8;
    private int cornerRectHandleHeight = 8;
    protected boolean canResizeByRightBottomCorner = false; // 是否可以改变大小（TaskNode可以）
    //--------------------------------------------
    // 是否在图形中心显示一个小圆（中心圆）
    // 元素的中心小圆的大小（直径），要是偶数，因为在计算坐标时得做除以2的操作（int型的 5/2 结果是2）
    // 元素的中心小圆的颜色
    // 是否能显示中心圆（有时不让显示）
    private boolean showCenterCircle = false;
    private int centerCircleWidth = 8;
    private Color centerCircleColor = Color.BLUE;
    //--------------------------------------------

    /**
     * 显示中心点
     * @param g
     */
    public void drawCenterCircle(Graphics2D g2d) {
        if (showCenterCircle) { // 需要显示时才可以显示
            // 设置颜色
            g2d.setStroke(new BasicStroke(1F)); // 设置线条宽度
            g2d.setColor(centerCircleColor);

            // 计算坐标
            Point centerPoint = getCenterPoint();
            int x1 = centerPoint.x - (centerCircleWidth / 2);
            int y1 = centerPoint.y - (centerCircleWidth / 2);

            g2d.fillOval(x1, y1, centerCircleWidth, centerCircleWidth);
        }
    }

    /**
     * 显示四角的小矩形手柄
     * @param g
     */
    public void drawCornerRectHandle(Graphics2D g2d) {
        if (showCornerRectHandle) { // 需要显示时才可以显示
            // 设置颜色
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1F)); // 设置线条宽度

            // 左上角
            int x1 = x - cornerRectHandleWidth;
            int y1 = y - cornerRectHandleHeight;
            // 左下角
            int x2 = x - cornerRectHandleWidth;
            int y2 = y + height;
            // 右上角
            int x3 = x + width;
            int y3 = y - cornerRectHandleHeight;
            // 右下角
            int x4 = x + width;
            int y4 = y + height;

            g2d.drawRect(x1, y1, cornerRectHandleWidth, cornerRectHandleHeight); // 左上角
            g2d.drawRect(x2, y2, cornerRectHandleWidth, cornerRectHandleHeight); // 左下角
            g2d.drawRect(x3, y3, cornerRectHandleWidth, cornerRectHandleHeight); // 右上角
            if (canResizeByRightBottomCorner) {
                g2d.fillRect(x4, y4, cornerRectHandleWidth, cornerRectHandleHeight); // 右下角
            } else {
                g2d.drawRect(x4, y4, cornerRectHandleWidth, cornerRectHandleHeight); // 右下角
            }
        }
    }

    /**
     * 把自己画出来，完整的，可能包含中心圆与4角的小矩形手柄
     * @param g
     */
    public void drawMe(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1F));

        draw(g, x, y, width, height);
        drawCenterCircle(g2d);
        drawCornerRectHandle(g2d);
    }

    /**
     * 把自己这个具体图形画出来（仅图形），由具体的元素图形子类实现
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public abstract void draw(Graphics g, int x, int y, int width, int height);

    /**
     * 获取右下角的矩形手柄的信息
     * @return
     */
    public Rectangle getRightBottomCornerRectHandle() {
        int x4 = x + width;
        int y4 = y + height;
        return new Rectangle(x4, y4, cornerRectHandleWidth, cornerRectHandleHeight);
    }

    /**
     * 获取当前图形的中心点的坐标
     * @return
     */
    public Point getCenterPoint() {
        int x1 = x + (width / 2);
        int y1 = y + (height / 2);
        return new Point(x1, y1);
    }

    /**
     * 通过右下角的矩形手柄调整大小
     * @param x2 新的右下角的x坐标
     * @param y2 新的右下角的y坐标
     */
    public void resizeByRightBottomCorner(int x2, int y2) {
        if (canResizeByRightBottomCorner) {
            // TODO 暂不支持右下角变为成其三种角的调整
            this.x = Math.min(x, x2);
            this.y = Math.min(y, y2);
            this.width = Math.abs(x2 - x);
            this.height = Math.abs(y2 - y);
        }
    }

    /**
     * 获取生成xml时用的g属性值（坐标，格式为：x,y,width,height）
     * @return
     */
    public String getCoordinateStringForXml() {
        return x + "," + y + "," + width + "," + height;
    }

    public List<Transition> getArrivingTransitions() {
        return arrivingTransitions;
    }

    public void setArrivingTransitions(List<Transition> arrivingTransitions) {
        this.arrivingTransitions = arrivingTransitions;
    }

    public Color getCenterCircleColor() {
        return centerCircleColor;
    }

    public void setCenterCircleColor(Color centerCircleColor) {
        this.centerCircleColor = centerCircleColor;
    }

    public List<Transition> getLeavingTransitions() {
        return leavingTransitions;
    }

    public void setLeavingTransitions(List<Transition> leavingTransitions) {
        this.leavingTransitions = leavingTransitions;
    }

    public boolean isShowCenterCircle() {
        return showCenterCircle;
    }

    public void setShowCenterCircle(boolean showCenterCircle) {
        this.showCenterCircle = showCenterCircle;
    }

    public boolean isShowCornerRectHandle() {
        return showCornerRectHandle;
    }

    public void setShowCornerRectHandle(boolean showCornerRectHandle) {
        this.showCornerRectHandle = showCornerRectHandle;
    }
}
