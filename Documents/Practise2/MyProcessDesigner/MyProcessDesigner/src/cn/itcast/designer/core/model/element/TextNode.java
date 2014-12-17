/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * 节点中要显示文本，文本是name属性的值。
 * @author tyg
 */
public abstract class TextNode extends BaseNode {

    /**
     * 用于输入文本的输入框
     */
    private JTextField textInputField = new JTextField();
    /**
     * TODO 可配置 文本输入框的最小宽度
     */
    private int textInputFieldMinWidth = 100;
    /**
     * TODO 可配置 文本输入框的高度
     */
    private int textInputFieldHeight = 20;

    /**
     * 显示用于文本输入框
     * @param component 文本框的所在的组件
     */
    public void showTextInputField(final JComponent component) {
        // 文本输入框所应在的位置：根据文本框的大小计算出，效果是显示在矩形的中间
        int x1 = x + 5;
        int y1 = y + (height / 2) - (this.textInputFieldHeight / 2);
        int inputFieldWidth = (width > textInputFieldMinWidth) ? width : textInputFieldMinWidth; // 宽度最小为 textInputFieldMinWidth

        textInputField.setText(getName()); // 设置文本
        textInputField.setBounds(x1, y1, inputFieldWidth, textInputFieldHeight); // 设置位置

        // 注册事件处理，是不是可以有用重复
        textInputField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                completeTextInput(component);// 按回车后，完成文字输入
                component.paint(component.getGraphics()); // 重画
            }
        });

        // TODO 没有在组件中重绘！最小化后再打开，就看不到了，但还能输入。
        component.add(textInputField); // 添加在绘图区中，让用户输入文字
        textInputField.requestFocus(); // 获得焦点
    }

    /**
     * 完成文本输入，记下文本，并隐藏文本输入框
     * @param component
     */
    public void completeTextInput(JComponent component) {
        // 记下文本
        setName(textInputField.getText().trim());

        // TODO 移除文本输入框，也可以设为隐藏？！
        component.remove(textInputField);
    }

    /**
     * 显示文本（上下是居中对齐，左右是左对齐），文本是name属性的值
     * @param g
     */
    public void drawText(Graphics g) {
        if (getName() != null) {
            g.setColor(Color.BLACK);

            int x1 = x + 10; // 左右是左对齐，左边距8px
            int y1 = y + (height / 2) + 5; // 上下大约是居中对齐
            g.drawString(getName(), x1, y1);
        }
    }

    @Override
    public void drawMe(Graphics g) {
        // TODO 一定要有父类的调用
        super.drawMe(g);
        // TODO 再画文本
        drawText(g);
    }


}
