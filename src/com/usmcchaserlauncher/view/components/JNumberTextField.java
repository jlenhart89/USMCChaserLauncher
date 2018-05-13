package com.usmcchaserlauncher.view.components;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * A {@link JTextField} that skips all non-digit keys. The user is only able to enter numbers.
 * 
 * @author Michi Gysel <michi@scythe.ch>
 * 
 */
public class JNumberTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    @Override
    public void processKeyEvent(KeyEvent ev) {
        if (!Character.isLetter(ev.getKeyChar())) {
            super.processKeyEvent(ev);
        }
        ev.consume();
        return;
    }
}