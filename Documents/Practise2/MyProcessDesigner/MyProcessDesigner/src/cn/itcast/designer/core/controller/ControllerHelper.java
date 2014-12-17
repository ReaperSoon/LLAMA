/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller;

import cn.itcast.designer.core.model.base.ProcessElement;
import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.view.DesignerPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.border.Border;

/**
 *
 * @author tyg
 */
public class ControllerHelper {

    /**
     * 当前的所有信息
     */
    private ExecutionContext ec;

    public ControllerHelper(ExecutionContext executionContext) {
        this.ec = executionContext;
    }

    /**
     * 获取包含指定坐标的节点元素。是从后向前找，这样就就是先取出显示在上层的元素
     * @param x
     * @param y
     * @return
     */
    public BaseNode getFromAllNodeElementWhichContains(int x, int y) {
        for (int i = ec.getAllElements().size() - 1; i >= 0; i--) {
            ProcessElement e = ec.getAllElements().get(i);
            if (e instanceof BaseNode) {
                BaseNode node = (BaseNode) e;
                if (node.contains(x, y)) {
                    return node;
                }
            }
        }

        return null;
    }

    /**
     * 获取包含指定坐标的、已选择的节点元素。是从后向前找，这样就能确保优化获取最上层的节点。（不包括连线）
     * @param x
     * @param y
     * @return
     */
    public BaseNode getFromSelectedNodesWhichContains(int x, int y) {
        for (int i = ec.getSelectedNodes().size() - 1; i >= 0; i--) {
            ProcessElement e = ec.getSelectedNodes().get(i);
            if (e instanceof BaseNode) {
                BaseNode node = (BaseNode) e;
                if (node.contains(x, y)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     *  检测当前是否移到了某选择的节点的右下角的小矩形手柄上（这个手柄可以让图形改变大小）
     * TODO 改为getxxx?
     * @param x
     * @param y
     * @return
     */
    public boolean isSelectedNodeRightBottonCornerRectContains(int x, int y) {
        for (int i = ec.getSelectedNodes().size() - 1; i >= 0; i--) {
            BaseNode node = ec.getSelectedNodes().get(i);
            if (node.getRightBottomCornerRectHandle().contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算出左上角坐标与宽度高度并返回
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public Rectangle calcRectBounds(int x1, int y1, int x2, int y2) {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);

        return new Rectangle(x, y, width, height);
    }

    /**
     * 画图到指定的Image中
     * @param image
     */
    public void drawAllToImage(Image image) {
        DesignerPanel designerPanel = ec.getDesignerPanel();
        Graphics g = image.getGraphics();

        // 1，先清空
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, designerPanel.getWidth(), designerPanel.getHeight());

        // 2，画边框
        Border border = designerPanel.getBorder();
        if (border != null) {
            border.paintBorder(designerPanel, g, 0, 0, designerPanel.getWidth(), designerPanel.getHeight());
        }

        // 3，画元素
        g.setColor(Color.BLACK);
        for (ProcessElement e : ec.getAllElements()) {
            e.drawMe(g);
        }
    }

    /**
     * 重画绘图区
     */
    public void repaintDesignerPanel() {
        ec.getDesignerPanel().paint(ec.getDesignerPanel().getGraphics());
    }

    /**
     * 如果需要重画，则重画，并还原状态
     */
    public void repaintDesignerPanelIfNeed() {
        if (ec.isNeedRepaint()) {
            this.repaintDesignerPanel();
            ec.setNeedRepaint(false); // 设置needRepatin为false
        }
    }

    /**
     * 删除当前所选的所有节点元还素，并删除与之相关的所有Transition
     * TODO 放到DeleteSelectNodesAction中？
     * 删除选中的节点，并删除与被删除节点关联的所有连线
     */
    public void deleteSelectedNodes() {
        for (BaseNode node : ec.getSelectedNodes()) {
            // 1，删除所有选中的节点元素
            ec.getAllElements().remove(node);
            // 2，删除与之关联的连线
            ec.getAllElements().removeAll(node.getLeavingTransitions());
            ec.getAllElements().removeAll(node.getArrivingTransitions());
        }
        ec.getHelper().repaintDesignerPanel(); // 需要重绘
    }
}
