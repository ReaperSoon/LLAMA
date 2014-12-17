/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.view;

import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 *
 * @author tyg
 */
public interface MainFrame {

    /**
     *
     * @return 属性配置的按钮
     */
    JButton getBtnProperties();

    /**
     *
     * @return 选择功能按钮
     */
    JToggleButton getBtnSelect();

    void setToggleButtonsStatus(JToggleButton btnSelect, boolean b);
}
