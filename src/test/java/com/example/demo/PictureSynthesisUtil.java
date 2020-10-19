package com.example.demo;

/**
 * @author huzipeng
 * @version 1.0
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * 图片合成工具类
 *
 */
public class PictureSynthesisUtil {

    public static void main(String[] args) throws Exception {
        Boolean aBoolean = signatureSynthesis("/Users/huzipeng/Desktop/1.png", "/Users/huzipeng/Desktop/2.png"
                , "/Users/huzipeng/Desktop/3.png", 0, 0
        );
        System.out.println(aBoolean);
    }
    /**
     * 签名合成，图片坐标系左上角为（0，0）
     *
     * @param imgPath       图片路径
     * @param signaturePath 签名图片路径
     * @param outFilePath   合成后图片路径
     * @param xPlace        签名图片放置的x坐标
     * @param yPlace        签名图片放置的y坐标
     * @return 是否合成成功
     * @throws IOException
     */
    public static Boolean signatureSynthesis(String imgPath, String signaturePath, String outFilePath, int xPlace,
                                             int yPlace) throws Exception {
        BufferedImage backImg = ImageIO.read(new File(imgPath));
        BufferedImage signatureImg = ImageIO.read(new File(signaturePath));

        File outFile = new File(outFilePath);
        // 假如目标路径不存在,则新建该路径
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        // 假如目标文件不存在,则新建该文件
        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        Graphics2D backG = backImg.createGraphics();
        backG.drawImage(signatureImg, xPlace, yPlace, signatureImg.getWidth(), signatureImg.getHeight(), null);
        backG.dispose();
        ImageIO.write(backImg, "png", new File(outFilePath));
        return true;
    }

    /**
     * 签名合成，图片坐标系左上角为（0，0）
     *
     * @param imgPath       图片路径
     * @param signaturePath 签名图片路径
     * @param outFilePath   合成后图片路径
     * @param xPlace        签名图片放置的x坐标
     * @param yPlace        签名图片放置的y坐标
     * @param signatureWidth        签名图片的宽
     * @param signatureHeight        签名图片的高
     * @return 是否合成成功
     * @throws IOException
     */
    public static Boolean signatureSynthesis(String imgPath, String signaturePath, String outFilePath, int xPlace,
                                             int yPlace,int signatureWidth,int signatureHeight ) throws Exception {
        BufferedImage backImg = ImageIO.read(new File(imgPath));
        BufferedImage signatureImg = ImageIO.read(new File(signaturePath));

        File outFile = new File(outFilePath);
        // 假如目标路径不存在,则新建该路径
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        // 假如目标文件不存在,则新建该文件
        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        Graphics2D backG = backImg.createGraphics();
        backG.drawImage(signatureImg, xPlace, yPlace, signatureWidth, signatureHeight, null);
        backG.dispose();
        ImageIO.write(backImg, "png", new File(outFilePath));
        return true;
    }

    /**
     * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"
     *
     * @param inputFilePath
     * @return
     */
    public static String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }

}
