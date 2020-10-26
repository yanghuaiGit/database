package com.example.demo.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TopFileReader {


    public static void main(String[] args) throws Exception {


        ArrayList<handlerline> handlerlines = new ArrayList<>();
        handlerlines.add(new loadTotal());
        handlerlines.add(new cpuTotal());
        handlerlines.add(new memTotal());
        readFileByLinesTopWriteToExcel("/Users/yanghuai/fsdownload/8.txt", handlerlines,"/Users/yanghuai/Desktop/0.xlsx");
    }


    /**
     * @param fileName top文件路径
     * @param handlerl
     * @param xlsxName 写入xlsx路径
     */
    public static void readFileByLinesTopWriteToExcel(String fileName, List<handlerline> handlerl,String xlsxName) {
        List<Total> data = new ArrayList<>(180);
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            int i =0;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("load average:")) {
                    Total total = new Total();

                    boolean flag = false;
                    for (handlerline handlerline : handlerl) {
                        if (handlerline.isHadnler(tempString)) {
                            handlerline.handlerLine(tempString, total);
                        }
                    }
                    while ((tempString = reader.readLine()) != null) {
                        for (handlerline handlerline : handlerl) {
                            if (handlerline.isHadnler(tempString)) {
                                handlerline.handlerLine(tempString, total);
                            }
                        }
                        if (tempString.equals("--")) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        data.add(total);
                    }
                } else {
                    continue;
                }
            }
            ExcelWriter excelWriter = EasyExcel.write(xlsxName, Total.class).build();

            WriteSheet writeSheet = EasyExcel.writerSheet("test").build();

            excelWriter.write(data, writeSheet);

            /// 千万别忘记finish 会帮忙关闭流

            excelWriter.finish();


            BigDecimal cpuload_1 = BigDecimal.ZERO;

            BigDecimal cpuload_5 = BigDecimal.ZERO;

            BigDecimal cpuload_15 = BigDecimal.ZERO;

            BigDecimal total = BigDecimal.ZERO;

            BigDecimal buff_cache = BigDecimal.ZERO;

            BigDecimal used = BigDecimal.ZERO;

            BigDecimal free = BigDecimal.ZERO;

            BigDecimal us = BigDecimal.ZERO;

            BigDecimal sy = BigDecimal.ZERO;

            BigDecimal id = BigDecimal.ZERO;

            BigDecimal wa = BigDecimal.ZERO;

            for (Total v : data) {
                cpuload_1 = cpuload_1.add(v.getCpuload_1());
                cpuload_5 = cpuload_5.add(v.getCpuload_5());
                cpuload_15 = cpuload_15.add(v.getCpuload_15());
                total = total.add(v.getTotal());
                buff_cache = buff_cache.add(v.getBuff_cache());
                used = used.add(v.getUsed());
                free = free.add(v.getFree());
                us = us.add(v.getUs());
                sy = sy.add(v.getSy());
                id = id.add(v.getId());
                wa = wa.add(v.getWa());
            }
             // 平均值计算
            System.out.println("统计数-》" + data.size());
            System.out.println("load1:" + cpuload_1.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("load5:" + cpuload_5.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("load15:" + cpuload_15.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));


            System.out.println("idle:" + id.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("iowait:" + wa.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("sys:" + sy.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("user:" + us.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP));


//            System.out.println("total:" + total.total.divide(new BigDecimal(index + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"),4,BigDecimal.ROUND_HALF_UP));
            System.out.println("free:" + free.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("used:" + used.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"), 4, BigDecimal.ROUND_HALF_UP));
            System.out.println("buff_cache:" + buff_cache.divide(new BigDecimal(data.size() + ""), 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("1024"), 4, BigDecimal.ROUND_HALF_UP));


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


            BigDecimal subtract =new BigDecimal("1").subtract(
                    ( (new BigDecimal(split1[1].trim().split(" ")[0].trim()))
                    .add(new BigDecimal(split1[3].trim().split(" ")[0].trim()))).divide(new BigDecimal(split1[0].trim().split(" ")[0].trim()),4,BigDecimal.ROUND_HALF_UP))
                    ;
            total.setCacheUsed(subtract);
        }

        @Override
        public boolean isHadnler(String line) {
            return line.contains("KiB Mem :");
        }
    }



}
