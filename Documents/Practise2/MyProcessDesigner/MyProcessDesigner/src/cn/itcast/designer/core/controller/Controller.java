/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.itcast.designer.core.controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

/**
 *
 * @author tyg
 */
public interface Controller {

    /**
     * 默认的光标
     */
    Cursor CURSOR_DEFAULT = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

    void doWhenMousePressed(ExecutionContext ec, MouseEvent evt);

    void doWhenMouseDragged(ExecutionContext ec, MouseEvent evt);

    void doWhenMouseReleased(ExecutionContext ec, MouseEvent evt);

    void doWhenMouseDoubleClickButton1(ExecutionContext ec, MouseEvent evt);

    void doWhenMouseMoved(ExecutionContext ec, MouseEvent evt);
}
