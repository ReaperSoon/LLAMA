/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.element;

import cn.itcast.designer.core.util.ImageUtils;
import java.awt.image.BufferedImage;

/**
 * 节点显示为一个图片，如开始节点、结束节点、决策节点等。这种节点不显示文本（name属性）
 * @author tyg
 */
public abstract class ImageNode extends BaseNode {

    private static BufferedImage image;

    public ImageNode() {
//        setName("start");
//        this.hasText = false; // 没有文本（显示图片）

        // 加载图片文件
        image = ImageUtils.readImage(getImageResource());
    }

    /**
     * 获取图片的路径，要是在classpath下，默认是在 cn/itcast/designer/resources/{类名}.png
     * 子类也可以覆盖这个方法以提供其他路径
     * @return
     */
    public String getImageResource() {
        return "resources/" + this.getClass().getSimpleName() + ".png";
    }
}
