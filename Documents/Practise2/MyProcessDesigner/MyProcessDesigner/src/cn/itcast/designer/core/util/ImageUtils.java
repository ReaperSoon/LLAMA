/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tyg
 */
public class ImageUtils {

    /**
     * 加载图片
     * @param resource
     * @return
     */
    public static BufferedImage readImage(String resource) {
        try {
            return ImageIO.read(ImageUtils.class.getClassLoader().getResource(resource));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
