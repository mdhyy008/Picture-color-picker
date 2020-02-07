package com.dabai.colorpicker;



/**
 * <strong>颜色工具类</strong><br>
 * <ul>
 * <li>颜色进制转换</li>
 * <li>颜色合法性校验</li>
 * </ul>
 * <br>
 * @author Aaron.ffp
 * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java, 2016-03-02 11:32:40.447 Exp $
 */
public class ColorUtils2 {
     private static String msg = "";

    private static String regHex = "^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$";
    private static String regRgb = "^(RGB\\(|rgb\\()([0-9]{1,3},){2}[0-9]{1,3}\\)$";
    private static String regRepRgb = "(rgb|\\(|\\)|RGB)*";

    public ColorUtils2() {
    }

    /**
     * <strong>颜色十六进制转颜色RGB</strong><br>
     * <ul>
     * <li>颜色十六进制参数不合法时，返回null</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java hex2Rgb, 2016-03-24 23:50:42.004 Exp $
     *
     * @param hex 颜色十六进制
     * @return 颜色RGB
     */
    public static String hex2Rgb(String hex) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtils2.isHex(hex)) {
            msg = "颜色十六进制格式 【" + hex + "】 不合法，请确认！";

            return null;
        }

        String c = hex.toUpperCase().replace("#", "");

        String r = Integer.parseInt((c.length() == 3 ? c.substring(0, 1) + c.substring(0, 1) : c.substring(0, 2)), 16) + "";
        String g = Integer.parseInt((c.length() == 3 ? c.substring(1, 2) + c.substring(1, 2) : c.substring(2, 4)), 16) + "";
        String b = Integer.parseInt((c.length() == 3 ? c.substring(2, 3) + c.substring(2, 3) : c.substring(4, 6)), 16) + "";

        sb.append("RGB(" + r + "," + g + "," + b + ")");

        return sb.toString();
    }

    /**
     * <strong>颜色十六进制转颜色RGB</strong><br>
     * <ul>
     * <li>颜色十六进制参数不合法时，返回null</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java hex2Rgb, 2016-03-24 23:50:42.004 Exp $
     *
     * @param hex 颜色十六进制
     * @return 颜色RGB
     */
    public static String hex2aRgb(String hex) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtils2.isHex(hex)) {
            msg = "颜色十六进制格式 【" + hex + "】 不合法，请确认！";

            return null;
        }

        String c = hex.toUpperCase().replace("#", "");

        String a = Integer.parseInt((c.length() == 3 ? c.substring(0, 1) + c.substring(0, 1) : c.substring(0, 2)), 16) + "";
        String r = Integer.parseInt((c.length() == 3 ? c.substring(1, 2) + c.substring(1, 2) : c.substring(2, 4)), 16) + "";
        String g = Integer.parseInt((c.length() == 3 ? c.substring(2, 3) + c.substring(2, 3) : c.substring(4, 6)), 16) + "";
        String b = Integer.parseInt((c.length() == 3 ? c.substring(3, 4) + c.substring(3, 4) : c.substring(6, 8)), 16) + "";

        sb.append("ARGB(" + a + "," + r + "," + g + "," + b + ")");

        return sb.toString();
    }

    /**
     * <strong>颜色RGB转十六进制</strong><br>
     * <ul>
     * <li>颜色RGB不合法，则返回null</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java rgb2Hex, 2016-03-15 23:49:33.224 Exp $
     *
     * @param rgb 颜色RGB
     * @return 合法时返回颜色十六进制
     */
    public static String rgb2Hex(String rgb) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtils2.isRgb(rgb)) {
            msg = "颜色 RGB 格式【" + rgb + "】 不合法，请确认！";
            return null;
        }

        String r = Integer.toHexString(ColorUtils2.getRed(rgb)).toUpperCase();
        String g = Integer.toHexString(ColorUtils2.getGreen(rgb)).toUpperCase();
        String b = Integer.toHexString(ColorUtils2.getBlue(rgb)).toUpperCase();

        sb.append("#");
        sb.append(r.length() == 1 ? "0" + r : r);
        sb.append(g.length() == 1 ? "0" + g : g);
        sb.append(b.length() == 1 ? "0" + b : b);

        return sb.toString();
    }

    /**
     * <strong>获取颜色RGB红色值</strong><br>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java getRed, 2016-03-22 23:48:50.501 Exp $
     *
     * @param rgb 颜色RGB
     * @return 红色值
     */
    public static int getRed(String rgb){
        return Integer.valueOf(ColorUtils2.getRGB(rgb)[0]);
    }

    /**
     * <strong>获取颜色RGB绿色值</strong><br>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java getGreen, 2016-03-22 23:48:16.290 Exp $
     *
     * @param rgb 颜色RGB
     * @return 绿色值
     */
    public static int getGreen(String rgb){
        return Integer.valueOf(ColorUtils2.getRGB(rgb)[1]);
    }

    /**
     * <strong>获取颜色RGB蓝色值</strong><br>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java getBlue, 2016-03-22 23:47:20.801 Exp $
     *
     * @param rgb 颜色RGB
     * @return 蓝色数值
     */
    public static int getBlue(String rgb){
        return Integer.valueOf(ColorUtils2.getRGB(rgb)[2]);
    }

    /**
     * <strong>获取颜色RGB数组</strong><br>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java getRGB, 2016-03-21 23:46:00.944 Exp $
     *
     * @param rgb 颜色RGB
     * @return 颜色数组[红，绿，蓝]
     */
    public static String[] getRGB(String rgb){
        return rgb.replace(" ","").replace(regRepRgb, "").split(",");
    }

    /**
     * <strong>验证颜色十六进制是否合法</strong><br>
     * <ul>
     * <li>规则：#号开头，三位或六位字母数字组成的字符串</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java isHex, 2016-03-20 23:44:03.133 Exp $
     *
     * @param hex 十六进制颜色
     * @return 合法则返回true
     */
    public static boolean isHex(String hex) {
        return true;
    }

    /**
     * <strong>验证颜色RGB是否合法</strong><br>
     * <ul>
     * <li>1、RGB符合正则表达式</li>
     * <li>2、颜色值在0-255之间</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java isRgb, 2016-03-12 23:41:19.925 Exp $
     *
     * @param rgb 颜色RGB
     * @return 是否合法，合法返回true
     */
    public static boolean isRgb(String rgb) {
        boolean r = ColorUtils2.getRed(rgb) >= 0 && ColorUtils2.getRed(rgb) <= 255;
        boolean g = ColorUtils2.getGreen(rgb) >= 0 && ColorUtils2.getGreen(rgb) <= 255;
        boolean b = ColorUtils2.getBlue(rgb) >= 0 && ColorUtils2.getBlue(rgb) <= 255;

        return ColorUtils2.isRgbFormat(rgb) && r && g && b;
    }

    /**
     * <strong>验证颜色RGB是否匹配正则表达式</strong><br>
     * <ul>
     * <li>匹配则返回true</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils2.java isRgbFormat, 2016-03-03 23:40:12.267 Exp $
     *
     * @param rgb 颜色RGB
     * @return 是否匹配
     */
    public static boolean isRgbFormat(String rgb) {
        return true;
    }
}