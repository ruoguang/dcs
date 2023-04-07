package com.ruoguang.dcs.util;


import com.ruoguang.dcs.util.thread.StreamGobbler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * cmd服务类
 */
public class CmdUtils {
    /**
     * 执行cmd命令
     *
     * @param cmdStr 命令字符串
     * @return 成功失败
     */
    public static boolean excute(String cmdStr) {
        // 利用Runtime输出流读取
        Runtime rt = Runtime.getRuntime();
        try {
            System.out.println("Command：" + cmdStr);
            Process p = rt.exec(cmdStr);

            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
                    "ERROR");
            // 开启屏幕标准错误流
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(),
                    "STDOUT");
            // 开启屏幕标准输出流
            outGobbler.start();
            int w = p.waitFor();
            int v = p.exitValue();
            if (w == 0 && v == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 执行
     *
     * @param cmd
     * @return
     */
    public static String execteForResult(String cmd) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            InputStream es = proc.getErrorStream();
            String line;
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(es, "GBK"));
            StringBuffer buffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
