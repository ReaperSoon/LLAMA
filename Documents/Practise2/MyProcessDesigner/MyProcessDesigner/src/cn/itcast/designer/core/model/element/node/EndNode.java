/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.element.node;

import cn.itcast.designer.core.dialog.PropertiesPanel;
import cn.itcast.designer.core.dialog.impl.NamePropertiesPanel;
import cn.itcast.designer.core.model.element.ImageNode;
import cn.itcast.designer.core.util.ImageUtils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author tyg
 */
public class EndNode extends ImageNode {

    private static BufferedImage image;
 
    public EndNode() {
        setName("end");

        // 加载图片文件
        String fileName = "EndNode.png";
        image = ImageUtils.readImage("resources/" + fileName);
    }

    public PropertiesPanel getPropertiesPanel() {
        return new NamePropertiesPanel();
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
//        ProcessNode.drawEndNode(g, x, y, width, height);

        // TODO 应在新建对象时，指定一个固定的width与height，因为是一个图片，应从父类中就支持
        this.width = image.getWidth();
        this.height = image.getHeight();

        g.drawImage(image, x, y, null);
    }

    @Override
    public String toXml() {
        StringBuffer xml = new StringBuffer("<end name=\"" + getName() + "\" g=\"" + getCoordinateStringForXml() + "\">").append("\n");//
        // TODO end元素没有Transition
        xml.append("</end>");
        return xml.toString();
    }
}
