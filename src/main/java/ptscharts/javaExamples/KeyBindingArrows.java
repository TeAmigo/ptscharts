/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscharts.javaExamples;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class KeyBindingArrows extends JFrame {

  public KeyBindingArrows() {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(100, 100));
    add(panel);

    InputMap im = panel.getInputMap();
    ActionMap am = panel.getActionMap();

    Action action = new SimpleAction("Left");
    Object key = action.getValue(Action.NAME);
    KeyStroke keyStroke = KeyStroke.getKeyStroke("LEFT");
    im.put(keyStroke, key);
    am.put(key, action);

    action = new SimpleAction("Right");
    key = action.getValue(Action.NAME);
    keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
    im.put(keyStroke, key);
    am.put(key, action);
  }

  class SimpleAction extends AbstractAction {

    public SimpleAction(String name) {
      putValue(Action.NAME, name);
    }

    public void actionPerformed(ActionEvent e) {
      System.out.println(getValue(Action.NAME));
    }
  }

  public static void main(String[] args) {
    KeyBindingArrows frame = new KeyBindingArrows();
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
