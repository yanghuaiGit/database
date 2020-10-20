package com.example.demo.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopFileReader {


    public static void main(String[] args) throws Exception {
        ArrayList<handlerline> handlerlines = new ArrayList<>();
        handlerlines.add(new timeTotal("02:41:00", "02:51:59"));
        handlerlines.add(new loadTotal());
        handlerlines.add(new cpuTotal());
        handlerlines.add(new memTotal());
        readFileByLinesTopWriteToExcel("/Users/yanghuai/fsdownload/ceshi_1000_1.txt", handlerlines);
//        readFileByLinesTop("/Users/yanghuai/fsdownload/ceshi_1000_1.txt", handlerlines, new Total());
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public static void readFileByLinesTop(String fileName, List<handlerline> handlerl, Total total) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int index = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            int t = 0;
            while ((tempString = reader.readLine()) != null) {
                t++;
                int i =0;
                boolean flag =false;
                for (handlerline handlerline : handlerl) {
                    if(i ==0){
                        if (!handlerline.isHadnler(tempString)){
                            flag =true;
                            break;
                        }
                    }
                    if (handlerline.isHadnler(tempString)) {
                        handlerline.handlerLine(tempString, total);
                    }
                    i++;
                }
                if(flag){break;}
//                if(t%17==0 ){
                if(tempString.equals("--") ){
                    index++;
                }
            }

            System.out.println("统计数-》" + index);
            System.out.println("load1:" + total.cpuload_1.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("load5:" + total.cpuload_5.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("load15:" + total.cpuload_15.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));


            System.out.println("idle:" + total.id.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("iowait:" + total.wa.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("sys:" + total.sy.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("user:" + total.us.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP));


//            System.out.println("total:" + total.total.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"),4,BigDecimal.ROUND_HALF_UP));
            System.out.println("free:" + total.free.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"),4,BigDecimal.ROUND_HALF_UP));
            System.out.println("used:" + total.used.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"),4,BigDecimal.ROUND_HALF_UP));
            System.out.println("buff_cache:" + total.buff_cache.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"),4,BigDecimal.ROUND_HALF_UP));
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public static void readFileByLinesTopWriteToExcel(String fileName, List<handlerline> handlerl) {
        List<Total> data = new ArrayList<>(180);
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if(tempString.contains("load average:") &&  handlerl.get(0).isHadnler(tempString)){
                    Total total = new Total();
                    boolean flag =false;
                    for (handlerline handlerline : handlerl) {
                        if (handlerline.isHadnler(tempString)) {
                            handlerline.handlerLine(tempString, total);
                        }
                    }
                    while((tempString = reader.readLine()) != null){
                        for (handlerline handlerline : handlerl) {
                            if (handlerline.isHadnler(tempString)) {
                                handlerline.handlerLine(tempString, total);
                            }
                        }
                        if(tempString.equals("--") ){
                            flag =true;
                            break;
                        }
                    }
                    if(flag){
                        data.add(total);
                    }
                }else{
                    continue;
                }
            }
//            EasyExcel.write("/Users/yanghuai/Desktop/1.xlsx", Total.class).sheet("写入方法一").doWrite(data);
            ExcelWriter excelWriter = EasyExcel.write("/Users/yanghuai/Desktop/1.xlsx", Total.class).build();

            WriteSheet writeSheet = EasyExcel.writerSheet("写入方法二").build();

            excelWriter.write(data, writeSheet);

            /// 千万别忘记finish 会帮忙关闭流

            excelWriter.finish();
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    interface handlerline {
        void handlerLine(String line, Total total);

        boolean isHadnler(String line);
    }

    static class loadTotal implements handlerline {

        @Override
        public void handlerLine(String line, Total total) {
            String cpuLoad = line.split("load average:")[1];
            String[] split = cpuLoad.split(",");
            total.cpuload_1 = total.cpuload_1.add(new BigDecimal(split[0].trim()));
            total.cpuload_5 = total.cpuload_5.add(new BigDecimal(split[1].trim()));
            total.cpuload_15 = total.cpuload_15.add(new BigDecimal(split[2].trim()));

        }

        @Override
        public boolean isHadnler(String line) {
            return line.contains("load average:");
        }
    }

    static class cpuTotal implements handlerline {

        @Override
        public void handlerLine(String line, Total total) {
            String KiBMem = line.split(":")[1];
            String[] split1 = KiBMem.split(",");
            total.us = total.us.add(new BigDecimal(split1[0].trim().split(" ")[0].trim()));
            total.sy = total.sy.add(new BigDecimal(split1[1].trim().split(" ")[0].trim()));
            total.id = total.id.add(new BigDecimal(split1[3].trim().split(" ")[0].trim()));
            total.wa = total.wa.add(new BigDecimal(split1[4].trim().split(" ")[0].trim()));
        }

        @Override
        public boolean isHadnler(String line) {
            return line.contains("%Cpu(s):");
        }
    }

    static class memTotal implements handlerline {

        @Override
        public void handlerLine(String line, Total total) {
            String KiBMem = line.split("KiB Mem :")[1];
            String[] split1 = KiBMem.split(",");
            total.total = total.total.add(new BigDecimal(split1[0].trim().split(" ")[0].trim()));
            total.free = total.free.add(new BigDecimal(split1[1].trim().split(" ")[0].trim()));
            total.used = total.used.add(new BigDecimal(split1[2].trim().split(" ")[0].trim()));
            total.buff_cache = total.buff_cache.add(new BigDecimal(split1[3].trim().split(" ")[0].trim()));
        }

        @Override
        public boolean isHadnler(String line) {
            return line.contains("KiB Mem :");
        }
    }

    static class timeTotal implements handlerline {
        private Date startTime;
        private Date endTime;
        private Pattern r = Pattern.compile("(?i)(?<host>([0-9]{2}:){2}[0-9]{2})");
        private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        public timeTotal(String startTime, String endTime) throws ParseException {
            this.startTime = simpleDateFormat.parse(startTime);
            this.endTime = simpleDateFormat.parse(endTime);
        }

        @Override
        public void handlerLine(String line, Total total) {

        }

        @Override
        public boolean isHadnler(String line) {
            if (line.contains("top -")) {
                Matcher m = r.matcher(line);
                if (m.find()) {
                    String group = m.group("host");
                    try {
                        Date parse = simpleDateFormat.parse(group);
                       return parse.compareTo(startTime) >= 0 && parse.compareTo(endTime) <= 0;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                return true;
            }
            throw new RuntimeException("错误的数据" + line);
        }
    }


}
