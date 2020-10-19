package com.example.demo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author huzipeng
 * @version 1.0
 */
public class MyImageDemo {
    public static void main(String[] args) throws Exception {
        BufferedImage dt = ImageIO.read(new File("/Users/huzipeng/Desktop/dt.png"));
        BufferedImage sku = ImageIO.read(new File("/Users/huzipeng/Desktop/sku.png"));
        BufferedImage ewm = ImageIO.read(new File("/Users/huzipeng/Desktop/ewm.png"));

        Graphics2D graphics2D = dt.createGraphics();
        graphics2D.drawImage(sku, 0, 0, sku.getWidth(), sku.getHeight(), null);
        graphics2D.drawImage(ewm, sku.getWidth() - ewm.getWidth() - 30, sku.getHeight() + 30, ewm.getWidth(), ewm.getHeight(), null);
        Font f1 = new Font("Helvetica", Font.PLAIN, 18);
        graphics2D.setFont(f1);
//        graphics2D.setColor(Color.black);
        graphics2D.setPaint(Color.black);
        graphics2D.drawString("张应得 天九共享平台投资集团", 60,  30);
        graphics2D.drawString("电话： 15810616126", 60,  60);
        graphics2D.dispose();
        dt = dt.getSubimage(0, 0, sku.getWidth(), sku.getHeight() + ewm.getWidth() + 60);
        ImageIO.write(dt, "png", new File("/Users/huzipeng/Desktop/3.png"));
    }
}
