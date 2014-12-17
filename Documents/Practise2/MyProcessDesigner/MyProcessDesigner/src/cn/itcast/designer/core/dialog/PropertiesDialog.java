/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.dialog;

import cn.itcast.designer.core.model.base.ProcessElement;
import cn.itcast.designer.view.MainFrameImpl;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * 最基本的属性配置对话框，有确定与取消按钮
 * @author tyg
 */
public class PropertiesDialog extends JDialog {

    private ProcessElement configurableElement; // 当前要配置的对象
    private JPanel okCancelPanel;
    private PropertiesPanel propertiesPanel;

    public PropertiesDialog(Component component, ProcessElement configurableElement) {
        this.configurableElement = configurableElement;

        this.okCancelPanel = new OkCancelPanel(this);
        this.okCancelPanel.setSize(360, 43); // 设置下面的确定取消按钮区的大小

        this.propertiesPanel = configurableElement.getPropertiesPanel();
        this.propertiesPanel.setProperties(configurableElement.getProperties());

        // 居中显示
        int width = propertiesPanel.getWidth();
        int height = propertiesPanel.getHeight() + okCancelPanel.getHeight();
        int x = component.getX() + (component.getWidth() / 2) - width / 2;
        int y = component.getY() + (component.getHeight() / 2) - height / 2;

        this.setBounds(x, y, width, height); // 设置边界
        this.setResizable(true); // 能改变大小
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL); // 设置为始终在最上面，不能失去焦点

        this.add(propertiesPanel, BorderLayout.CENTER); // 属性显示在中间
        this.add(okCancelPanel, BorderLayout.SOUTH); // “确定”与“取消”按钮在下面
    }

    /**
     * 点击了“确定”按钮后，要做的事：保存配置，并关闭对话框
     */
    public void ok() {
        // 1，配置元素
        for (String key : propertiesPanel.getProperties().stringPropertyNames()) {
            String value = propertiesPanel.getProperties().getProperty(key);
            configurableElement.setProperty(key, value);
        }

        // 2，关闭对话框
        this.dispose();
    }

    /**
     * 点击了“取消”按钮后，要做的事：只关闭对话框，不保存配置
     */
    public void cancel() {
        // 关闭对话框
        this.dispose();
    }
}
