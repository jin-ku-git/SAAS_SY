package com.youwu.shouyinsaas.util;

import android.graphics.Color;

import java.util.Random;


public class ColorUtil {
    /**
     * 根据RGB色值获取整型颜色
     *
     * @param rgb rgb色值,空代表获取随机颜色
     * @return int型色值
     */
    public static int getColorByRgb(String rgb) {
        int color = Color.WHITE;
        try {
            if (rgb != null) {
                color = Color.parseColor(rgb);
            } else {
                color = Color.parseColor(getRanDomColor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return color;
    }


    /**
     * 获取随机颜色
     *
     * @return 随机六位色值
     */
    public static String getRanDomColor() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("#");
        for (int i = 0; i < 6; i++) {
            stringBuffer.append(getRandomBeen());
        }
        return String.valueOf(stringBuffer);
    }

    /**
     * 获取色值单元
     *
     * @return 单个色值单元值
     */
    public static String getRandomBeen() {
        String been = "";
        int random = getRandom(16);
        if (random > 9) {
            switch (random) {
                case 10:
                    been = "a";
                    break;
                case 11:
                    been = "b";
                    break;
                case 12:
                    been = "c";
                    break;
                case 13:
                    been = "d";
                    break;
                case 14:
                    been = "e";
                    break;
                case 15:
                    been = "f";
                    break;
            }
        } else {
            been = String.valueOf(random);
        }
        return been;
    }

    /**
     * 获取随机整形数字
     *
     * @return 随机数
     */
    public static int getRandom(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }

    public static int[] getColors(int i) {

        // have as many colors as stack-values per entry
        int[] colors = new int[i];

//        System.arraycopy(ColorTemplate.COLORFUL_COLORS, 0, colors, 0, i);
        System.arraycopy(MATERIAL_COLORS, 0, colors, 0, i);

        return colors;
    }

    public static final int[] MATERIAL_COLORS = {
            rgb("#FF9900"), rgb("#10AEFF"), rgb("#07C160"), rgb("#F2BD42"), rgb("#EE752F")
    };
    /**
     * 销售额
     */
    public static final int[] SALE_COLORS = {
            rgb("#5087EC"), rgb("#FF9900"), rgb("#58A55C")
    };

    public static final int[] MAIN_COLORS = {
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6"),
            rgb("#72CDD7"), rgb("#5087EC"), rgb("#58A55C"), rgb("#F2BD42"), rgb("#EE752F"),
            rgb("#FF6C44"), rgb("#FF2D55"), rgb("#5856D6"), rgb("#FF8C00"), rgb("#919193"),
            rgb("#ec407a"), rgb("#9c27b0"), rgb("#673ab7"), rgb("#03a9f4"), rgb("#1de9b6")
    };

    public static final String[] MAIN_COLORS_STRING = {
            "#FF9900", "#10AEFF", "#07C160", "#F2BD42", "#EE752F",
            "#FF6C44", "#FF2D55", "#5856D6", "#FF8C00", "#919193",
            "#ec407a", "#9c27b0", "#673ab7", "#03a9f4", "#1de9b6"
    };

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

}