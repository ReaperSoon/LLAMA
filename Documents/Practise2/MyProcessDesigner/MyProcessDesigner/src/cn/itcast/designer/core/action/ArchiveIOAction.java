/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.action;

import cn.itcast.designer.core.controller.ExecutionContext;
import cn.itcast.designer.core.model.base.ProcessElement;
import cn.itcast.designer.core.model.element.BaseNode;
import cn.itcast.designer.view.DesignerPanel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author tyg
 */
public class ArchiveIOAction {

    /**
     * 保存为流程定义文档(.zip)，其中至少有两个文件：processdefinition.jpdl.xml、processimage.png
     * @param path
     */
    public void saveProcessArchive(ExecutionContext ec, String path) {
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(path));

            // 1，保存流程定义文件: process.jpdl.xml
            zipOut.putNextEntry(new ZipEntry("process.jpdl.xml"));
            zipOut.write(getProcessDefinitionXml(ec).getBytes("utf-8"));

            // 2，保存流程图片: processimage.png
            zipOut.putNextEntry(new ZipEntry("process.png"));
            zipOut.write(getProcessImageData(ec));

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    /**
     * TODO 流程的名称
     */
    private String processName = "test";

    private String getProcessDefinitionXml(ExecutionContext ec) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n"); // TODO 每句后面都有换行符
        xml.append("<process name=\"" + processName + "\" xmlns=\"http://jbpm.org/4.4/jpdl\">").append("\n");

        // 方法一：
        // 1，找出StartNode，应该只有一个
        // 2，从StartNode开始向后找到所有的节点，就是说所有的节点之间必须有联系，不能有独立的没有联系的节点
        // 说明：这样需要用到递归。

        // 方法二：直接循环所有的ProcessNode，把每个节点对应的xml拼接在一起就可以了。
        for (ProcessElement e : ec.getAllElements()) {
            if (e instanceof BaseNode) { // Transition是由所在的节点（fromNode）负责生成xml的
                BaseNode node = (BaseNode) e;
                xml.append(node.toXml());
            }
        }

        xml.append("</process> ");
        return xml.toString();
    }

    /**
     * 获取流程图片的内容
     * @return
     */
    private byte[] getProcessImageData(ExecutionContext ec) {
        // 创建图片，从网上查的，png格式对应的类型是 BufferedImage.TYPE_4BYTE_ABGR
        DesignerPanel designerPanel = ec.getDesignerPanel();
        BufferedImage bufferedImage = new BufferedImage(designerPanel.getWidth(), designerPanel.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        // 1，先画到图片上
       ec.getHelper(). drawAllToImage(bufferedImage);

        // 2，再保存图片
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
