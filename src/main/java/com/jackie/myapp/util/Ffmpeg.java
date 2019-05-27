package com.jackie.myapp.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Ffmpeg implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(Ffmpeg.class);
    public static final String FFMPEG_PATH = "D:/develop/ffmpeg/bin/ffmpeg.exe";
    private static Process process;

    private String rtspInPath;
    private String rtspOutPaht;


    public Ffmpeg() {
    }


    public Ffmpeg(String rtspInPath, String rtspOutPaht) {
        this.rtspInPath = rtspInPath;
        this.rtspOutPaht = rtspOutPaht;
    }

    @Override
    public void run() {
        convertCommand(rtspInPath, rtspOutPaht);
    }

    /**
     * 视频转换
     *
     * @param video_path
     * @return
     */


    public static String convertCommand(String video_path, String video_OutPath) {
        if (StringUtils.isEmpty(video_path)) {
            return null;
        }


        logger.info("old vodeo path：" + video_path);
        logger.info("new video path" + video_OutPath);

        List<String> commands = new java.util.ArrayList<String>();
        commands.add(FFMPEG_PATH);
        commands.add("-i");
        commands.add(video_path);
        commands.add(" ");
        commands.add(video_OutPath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //process.waitFor();//等待进程执行完毕
        //防止ffmpeg进程塞满缓存造成死锁
        InputStream error = process.getErrorStream();
        InputStream is = process.getInputStream();
        byte[] b = new byte[1024];
        int readbytes = -1;
        try {
            while ((readbytes = error.read(b)) != -1) {
                logger.info("FFMPEG视频转换进程错误信息：" + new String(b, 0, readbytes));
            }
            while ((readbytes = is.read(b)) != -1) {
                logger.info("FFMPEG视频转换进程输出内容为：" + new String(b, 0, readbytes));
            }
        } catch (IOException e2) {

        } finally {
            try {
                error.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("视频格式转换：" + video_OutPath);

        return video_OutPath;
    }






}
