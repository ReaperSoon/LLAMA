/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.model.base;

import cn.itcast.designer.core.dialog.PropertiesPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Properties;

/**
 * 可配置的流程元素。
 * 
 * 可配置的元素，可以有很多配置，是key-value对的形式，并且key与value都是String型的。
 * 有一个公共的配置：name，这个属性也是一些元素用于显示的文本。
 * 每个具体的元素都对应一种 PropertiesPanel，用于为当前元素进行属性配置。
 * @author tyg
 */
public abstract class ProcessElement extends Rectangle  {

    /**
     * 显示自己
     * @param g
     */
    public abstract void drawMe(Graphics g);

    /**
     * 生成本节点对应的xml元素，包含所拥有的leavingTransitions
     * @return
     */
    public abstract String toXml();
    /**
     * TODO 解析xml得到并配置相应的元素对象
     * @param xml
     * @return
     */
//    ProcessElement parseXml(String xml);
    // =========================================================================
    // 所有的配置
    private Properties props = new Properties();

    /**
     * 设置参数
     * @param key
     * @param value 不能为null，使用这个值的toString()结果作为value
     */
    public void setProperty(String key, Object objValue) {
        props.setProperty(key, objValue.toString());
    }

    /**
     *  获取String型的配置
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * 获取int型的参数
     * @param key
     * @return
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(props.getProperty(key));
    }

    /**
     * 获取所有配置
     * @return
     */
    public Properties getProperties() {
        return props;
    }

    /**
     *  获取名称（元素中显示的文本）
     * @return
     */
    public String getName() {
        return getProperty("name");
    }

    /**
     * 设置名称
     * @param name
     */
    public void setName(String name) {
        setProperty("name", name);
    }

    /**
     * 获取用于配置属性的面板对象
     * @return
     */
    public abstract PropertiesPanel getPropertiesPanel();
}
