package io.github.kingstefan26.stefans_util.module.debug.test;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javax.swing.*;

public class Display{

    private JFrame frame;
    private Canvas canvas;

    private int screenWidth;
    private int screenHeight;

    public Display() {
//        this.screenHeight = height;
//        this.screenWidth = width;
//        makeDisplay();
        Color myColor = new Color(150,250,150);



        JFrame frame = new JFrame();

        frame.setSize(400, 400);

        JLabel label = new JLabel("Hello, World!");

        //  ImageIcon image = new ImageIcon("home/kokoniara/Documents/java/stefans_utils/src/main/resources/cock.png");

        ImageIcon image = new ImageIcon(getClass().getResource("/assets/stefan_util/textures/rando/cock.png"));

        JLabel imageLabel = new JLabel(image);

        frame.setTitle("ssssssssssss");

        label.setOpaque(true);

        label.setBackground(myColor);

        frame.add(imageLabel, BorderLayout.CENTER);

        frame.add(label, BorderLayout.WEST);

        frame.setResizable(true);




        frame.setVisible(true);
    }

    public void makeDisplay() {
//        frame = new JFrame();
//        frame.setVisible(true);
//        frame.setSize(screenWidth, screenHeight);
////        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//
//        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cock.png")));
//
//        canvas = new Canvas();
//        canvas.setPreferredSize(new Dimension(screenWidth, screenHeight));
//        canvas.setMinimumSize(new Dimension(screenWidth, screenHeight));
//        canvas.setMaximumSize(new Dimension(screenWidth, screenHeight));
//        canvas.setFocusable(false);
//
//
//
//        JLabel label = new JLabel("Hello, World!");
//
//        ImageIcon image = new ImageIcon("/home/kokoniara/Documents/java/stefans_utils/src/main/resources/cock.png");
//
//        JLabel imageLabel = new JLabel(image);
//
//        label.setOpaque(true);
//
//
//        frame.add(imageLabel);
//
//        frame.add(label);
//
//
//        frame.add(canvas);
//        frame.pack();
    }


    public JFrame getFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }

}


