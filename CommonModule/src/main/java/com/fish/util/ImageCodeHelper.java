package com.fish.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 图片验证码生成器
 */
public class ImageCodeHelper {
	// 图片区域宽度
	private int width = 160;
	// 图片区域高度
	private int height = 40;
	// 图片验证码个数
	private int codeCount = 4;

	private int lineCount = 20;

	private String code = null;

	private BufferedImage buffImg = null;
	Random random = new Random();

	public ImageCodeHelper() {
		this(160, 40);
	}

	public ImageCodeHelper(int width, int height) {
		this(width, height, 4);
	}

	public ImageCodeHelper(int width, int height, int codeCount) {
		this(width, height, codeCount, 20);
	}

	public ImageCodeHelper(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		creatImage();
	}

	private void creatImage() {
		int fontWidth = this.width / this.codeCount;
		int fontHeight = this.height - 5;
		int codeY = this.height - 8;

		this.buffImg = new BufferedImage(this.width, this.height, 1);
		Graphics g = this.buffImg.getGraphics();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, this.width, this.height);

		Font font = new Font("Fixedsys", 1, fontHeight);
		g.setFont(font);

		for (int i = 0; i < this.lineCount; ++i) {
			int xs = this.random.nextInt(this.width);
			int ys = this.random.nextInt(this.height);
			int xe = xs + this.random.nextInt(this.width);
			int ye = ys + this.random.nextInt(this.height);
			g.setColor(getRandColor(1, 255));
			g.drawLine(xs, ys, xe, ye);
		}

		float yawpRate = 0.01F;
		int area = (int) (yawpRate * this.width * this.height);
		for (int i = 0; i < area; ++i) {
			int x = this.random.nextInt(this.width);
			int y = this.random.nextInt(this.height);

			this.buffImg.setRGB(x, y, this.random.nextInt(255));
		}

		String str1 = randomStr(this.codeCount);
		this.code = str1;
		for (int i = 0; i < this.codeCount; ++i) {
			String strRand = str1.substring(i, i + 1);
			g.setColor(getRandColor(1, 255));

			g.drawString(strRand, i * fontWidth + 3, codeY);
		}
	}

	private String randomStr(int n) {
		String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		String str2 = "";
		int len = str1.length() - 1;

		for (int i = 0; i < n; ++i) {
			double r = Math.random() * len;
			str2 = str2 + str1.charAt((int) r);
		}
		return str2;
	}

	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + this.random.nextInt(bc - fc);
		int g = fc + this.random.nextInt(bc - fc);
		int b = fc + this.random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private Font getFont(int size) {
		Random random = new Random();
		Font[] font = new Font[5];
		font[0] = new Font("Ravie", 0, size);
		font[1] = new Font("Antique Olive Compact", 0, size);
		font[2] = new Font("Fixedsys", 0, size);
		font[3] = new Font("Wide Latin", 0, size);
		font[4] = new Font("Gill Sans Ultra Bold", 0, size);
		return font[random.nextInt(5)];
	}

	private void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	private void shearX(Graphics g, int w1, int h1, Color color) {
		int period = this.random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = this.random.nextInt(2);

		for (int i = 0; i < h1; ++i) {
			double d = (period >> 1) * Math.sin(i / period + 6.283185307179586D * phase / frames);

			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}
	}

	private void shearY(Graphics g, int w1, int h1, Color color) {
		int period = this.random.nextInt(40) + 10;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; ++i) {
			double d = (period >> 1) * Math.sin(i / period + 6.283185307179586D * phase / frames);

			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}
		}
	}

	public void write(OutputStream sos) throws IOException {
		ImageIO.write(this.buffImg, "png", sos);
		sos.close();
	}

	public BufferedImage getBuffImg() {
		return this.buffImg;
	}

	public String getCode() {
		return this.code.toLowerCase();
	}

	public static void main(String[] args) {
		ImageCodeHelper imageCodeHelper =	new ImageCodeHelper();

		try {
			imageCodeHelper.write(new FileOutputStream(new File("D://data/logs//test.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
