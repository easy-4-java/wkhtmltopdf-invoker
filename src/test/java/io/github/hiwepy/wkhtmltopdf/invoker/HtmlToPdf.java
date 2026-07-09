package io.github.hiwepy.wkhtmltopdf.invoker;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlToPdf {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlToPdf.class);

    private static final String TOPDFTOOL = "/root/wkhtmltox/bin/Calibre";

    /**
     * wkhtmltox = 环境变量
     * C:\Program Files\Calibre\bin
     * 
     * 
     * html转pdf
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String srcPath, String destPath) {

        File file = new File(destPath);
        File parent = file.getParentFile();
        // 如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }

        StringBuilder cmd = new StringBuilder();
        cmd.append(TOPDFTOOL);
        cmd.append(" ");
        cmd.append("--page-size A2");// 参数
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);

        boolean result = true;
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(
                    proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(
                    proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
            LOG.info("HTML2PDF成功，参数---html路径：{},pdf保存路径 ：{}", new Object[] {srcPath, destPath });
        } catch (Exception e) {
            LOG.error("HTML2PDF失败，srcPath地址：{},错误信息：{}", new Object[]{srcPath, e.getMessage()});
            result = false;
        }
        return result;
    }
}