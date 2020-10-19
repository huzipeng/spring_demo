package com.example.demo;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WenPic1 {
    private BufferedImage image;
    private int imageWidth = 300;  //图片的宽度
    private int imageHeight = 300; //图片的高度

    //生成图片文件
    public void createImage(String fileLocation) {
        BufferedOutputStream bos = null;
        if (image != null) {
            try {
                FileOutputStream fos = new FileOutputStream(fileLocation);
                bos = new BufferedOutputStream(fos);

                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
                encoder.encode(image);
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {//关闭输出流
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param imglogo logo图片
     * @param imgcode 二维码图片
     * @param words   文字信息
     * @param outurl  输出的Url路径
     */
    public void graphicsGeneration(String imglogo, String imgcode, String imgsign, String words, String outurl) {
        //定义长度变量
        int W_logoPic = 50;
        int H_logoPic = 50;
        int W_titleRight = 250;
        int H_Company = 20;
        int W_CodeImg = 150;
        int H_CodeImg = 150;
        int H_Words = 60;

        //创建主模板图片
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D main = image.createGraphics();
        //设置图片的背景色
        main.setColor(Color.white); //白色
        main.fillRect(0, 0, imageWidth, imageHeight);

        //***********************插入logo****************
        Graphics mainPic = image.getGraphics();
        BufferedImage bimg = null;
        try {
            bimg = javax.imageio.ImageIO.read(new java.io.File(imglogo));
        } catch (Exception e) {
        }

        if (bimg != null) {
            mainPic.drawImage(bimg, 30, 0, W_logoPic, H_logoPic, null);
            mainPic.dispose();
        }

        //***********************页面头部 文字****************
        Graphics titleRight = image.createGraphics();
        //设置区域颜色
        titleRight.setColor(new Color(255, 255, 255));
        //填充区域并确定区域大小位置，向右偏移
        titleRight.fillRect(81, 0, W_titleRight, H_logoPic);
        //设置字体颜色，先设置颜色，再填充内容 black黑 white白
        titleRight.setColor(Color.black);
        //设置字体
        Font titleFontReght = new Font("宋体", Font.BOLD, 25);
        titleRight.setFont(titleFontReght);
        ((Graphics2D) titleRight).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        titleRight.drawString("NB-IOT模块", W_titleRight / 2 + 5, (H_logoPic) / 2 + 10);

        //**********************底部公司名字*********
        Graphics typeLeft = image.createGraphics();
        //设置区域颜色
        typeLeft.setColor(new Color(255, 255, 255));
        //填充区域并确定区域大小位置，向右偏移,向下偏移
        typeLeft.fillRect(0, 280, imageWidth, H_Company);
        //设置字体颜色，先设置颜色，再填充内容
        typeLeft.setColor(Color.black);
        //设置字体
        Font titleFontleft = new Font("宋体", Font.PLAIN, 10);
        typeLeft.setFont(titleFontleft);
        ((Graphics2D) typeLeft).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        typeLeft.drawString("张大人科技有限公司", imageWidth / 2 - 50, imageHeight - 5);

        //***************插入二维码图片***********************************************
        Graphics codePic = image.getGraphics();
        BufferedImage codeimg = null;
        try {
            codeimg = javax.imageio.ImageIO.read(new java.io.File(imgcode));
        } catch (Exception e) {
        }

        if (codeimg != null) {
            codePic.drawImage(codeimg, 150, 130, W_CodeImg, H_CodeImg, null);
            codePic.dispose();
        }

        //***************插入标志图片***********************************************
        Graphics signPic = image.getGraphics();
        BufferedImage signImg = null;
        try {
            signImg = javax.imageio.ImageIO.read(new java.io.File(imgsign));
        } catch (Exception e) {
        }

        if (signImg != null) {
            signPic.drawImage(signImg, 0, 130, W_CodeImg, H_CodeImg, null);
            signPic.dispose();
        }
        //**********************中间文字部分*********
        Graphics centerword = image.createGraphics();
        //设置区域颜色
        centerword.setColor(new Color(255, 255, 255));
        //填充区域并确定区域大小位置，向右偏移,向下偏移
        centerword.fillRect(0, 70, imageWidth, H_Words);
        //设置字体颜色，先设置颜色，再填充内容
        centerword.setColor(Color.black);
        //设置字体
        Font FontCenterword = new Font("宋体", Font.PLAIN, 15);
        centerword.setFont(FontCenterword);
        ((Graphics2D) centerword).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        String[] info = words.split("&");
        for (int i = 0; i < info.length; i++) {
            centerword.drawString("M5310-A MCMHOS03", 10, 65);
            centerword.drawString(info[0], 10, 85);
            centerword.drawString(info[1], 10, 105);
            centerword.drawString(info[2], 10, 125);
        }
        //图片输出存放的位置
        createImage(outurl + ".jpg");
    }


    public static void main(String[] args) {
        WenPic1 cg = new WenPic1();
        /**
         *
         * @param imglogo logo图片
         * @param imgcode 二维码图片
         * @param words 文字信息
         * @param outurl 输出的Url路径
         */
        String words = "IMEI:467890765432136&KEY:WEIRIIRCMDSJSPVLDFDGE457349024&YIME:2019CP20311";
        try {
            cg.graphicsGeneration("/Users/huzipeng/Desktop/logo.png", "/Users/huzipeng/Desktop/二维码.png", "/Users/huzipeng/Desktop/二维码.png", words, "/Users/huzipeng/Desktop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


