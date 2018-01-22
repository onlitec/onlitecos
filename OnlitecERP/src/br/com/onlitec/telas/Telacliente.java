/*
 * Created by JFormDesigner on Wed Jan 03 13:27:25 BRST 2018
 */

package br.com.onlitec.telas;

import java.awt.*;
import javax.swing.*;


/**
 * @author unknown
 */
public class Telacliente extends JFrame {
    public Telacliente() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Alessandro Freire
        desktopPane1 = new JDesktopPane();

        //======== this ========
        setBackground(new Color(255, 102, 102));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== desktopPane1 ========
        {
            desktopPane1.setPreferredSize(new Dimension(800, 32));
        }
        contentPane.add(desktopPane1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Alessandro Freire
    private JDesktopPane desktopPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
