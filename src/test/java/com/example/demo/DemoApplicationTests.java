package com.example.demo;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

@SpringBootTest
class DemoApplicationTests {
    /**
     * 所有城市编码表-0724.xlsx
     * 对应各个结果
     */
    @Test
    public void test10() {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/huzipeng/Desktop/所有城市编码表-0724.xlsx"));

            Map<String, String> shengMap = new HashMap<>();
            Map<String, String> shiMap = new HashMap<>();
            Map<String, String> quMap = new HashMap<>();
            ArrayList<ArrayList<String>> shengshiquList = getDataList(getList(workbook.getSheetAt(1)));//编码关系
            for (ArrayList<String> itemList : shengshiquList) {
                shengMap.put(itemList.get(0), itemList.get(1));
                shiMap.put(itemList.get(2), itemList.get(3));
                quMap.put(itemList.get(2) + "-" + itemList.get(4), itemList.get(5));
            }

            ArrayList<ArrayList<String>> shengList = getDataList(getList(workbook.getSheetAt(2)));
            ArrayList<ArrayList<String>> shiList = getDataList(getList(workbook.getSheetAt(3)));
            ArrayList<ArrayList<String>> qyList2 = getDataList(getListBy0(workbook.getSheetAt(4)));
            List<ArrayList<String>> qyList = qyList2.subList(1, qyList2.size());
            ArrayList<ArrayList<String>> qyList1 = getDataList(getList(workbook.getSheetAt(5)));

            Map<String, ArrayList<String>> shengDataMap = new HashMap<>();
            Map<String, ArrayList<String>> shiDataMap = new HashMap<>();
            Map<String, ArrayList<String>> quDataMap = new HashMap<>();
            Map<String, ArrayList<String>> jingkaiquDataMap = new HashMap<>();
            for (ArrayList<String> item : shengList) {
                shengDataMap.put(item.get(0), item);
            }
            for (ArrayList<String> item : shiList) {
                shiDataMap.put(item.get(1), item);
            }
            for (ArrayList<String> item : qyList) {
                quDataMap.put(item.get(1) + "-" + item.get(2), item);
            }
            for (ArrayList<String> item : qyList1) {
                jingkaiquDataMap.put(item.get(1), item);
            }
            XSSFSheet sheet = workbook.getSheetAt(0);//结果
            ArrayList<ArrayList<String>> dataList = getDataList(getListBy0(sheet));
            List<List<String>> excelList = new ArrayList<>();
            excelList.add(dataList.get(0));
            for (int i = 1; i < dataList.size(); i++) {
                ArrayList<String> item = dataList.get(i);
                String sheng = item.get(1);
                String shi = item.get(3);
                String qu = item.get(5);
                String tiaoshu = "";
                String jibie = "";
                String wancheng = "";
                String fenpei = "";

                if (!sheng.equals("")) {
                    item.set(0, shengMap.get(sheng));
                }
                if (!shi.equals("")) {
                    item.set(2, shiMap.get(shi));
                }
                if (!qu.equals("")) {
                    item.set(4, quMap.get(shi + "-" + qu));
                }
                if (shi.equals("")) {
                    ArrayList<String> item1 = shengDataMap.get(sheng);
                    tiaoshu = item1.get(1);
                    jibie = item1.get(2);
                    wancheng = item1.get(3);
                    item.set(6, tiaoshu);//条数
                    item.set(7, jibie);//级别
                    item.set(8, wancheng);//完成状态
                    item.set(9, fenpei);//分配状态
                    excelList.add(item);
                    continue;
                }
                if (qu.equals("")) {
                    ArrayList<String> item1 = shiDataMap.get(shi);
                    tiaoshu = item1.get(2);
                    jibie = item1.get(3);
                    wancheng = item1.get(4);
                    item.set(6, tiaoshu);//条数
                    item.set(7, jibie);//级别
                    item.set(8, wancheng);//完成状态
                    item.set(9, fenpei);//分配状态
                    excelList.add(item);
                    continue;
                }
                ArrayList<String> item1 = quDataMap.get(shi + "-" + qu);
                if (item1 != null) {
                    tiaoshu = item1.get(3);
                    jibie = item1.get(4);
                    wancheng = item1.get(5);
                    fenpei = item1.get(6);
                } else {
                    item1 = jingkaiquDataMap.get(qu);
                    tiaoshu = item1.get(4);
                    jibie = item1.get(5);
                }

                item.set(6, tiaoshu);//条数
                item.set(7, jibie);//级别
                item.set(8, wancheng);//完成状态
                if (!fenpei.equals("")) {
                    System.out.println(fenpei);
                }
                item.set(9, fenpei);//分配状态
                excelList.add(item);
            }

            workbook = new XSSFWorkbook();
            FileOutputStream os = new FileOutputStream("/Users/huzipeng/Desktop/所有城市编码表-0724-结果.xlsx");
            for (List<String> item : excelList) {
                System.out.println(item.toString());
            }
            toExcelSheet(excelList, workbook.createSheet("结果"));
            workbook.write(os);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PEname 按-分割 前缀唯一
     * overview 不要待定和空
     * ssupport 不要待定和空
     * materials 不要待定和空
     * 城市 按比例
     * 政策结构化列表数据0722.xls
     */
    @Test
    public void test9() {
        try {
            int dataCount = 3000;
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/huzipeng/Desktop/政策结构化列表数据0722.xlsx"));
            XSSFSheet sheet = workbook.getSheetAt(0);//数据
            ArrayList<ArrayList<String>> dataList = getDataList(getList(sheet));
            List<List<String>> qitList = new ArrayList<>();
            List<List<String>> okList = new ArrayList<>();//清洗后的数据
            Set<String> setTitil = new HashSet<>();

            for (ArrayList<String> itemList : dataList) {
                //按城市分配
                String c1 = itemList.get(6);
                String c2 = itemList.get(8);
                String c3 = itemList.get(9);
                String c4 = itemList.get(5);
                String key = ".*待定.*";
                if (!StringUtils.hasText(c1) || c1.matches(key)) {
                    qitList.add(itemList);
                    continue;
                }
                if (!StringUtils.hasText(c2) || c2.matches(key)) {
                    qitList.add(itemList);
                    continue;
                }

                if (!StringUtils.hasText(c3) || c3.matches(key)) {
                    qitList.add(itemList);
                    continue;
                }
                if (!StringUtils.hasText(c4) || c4.matches(key)) {
                    qitList.add(itemList);
                    continue;
                }
                String PEName = itemList.get(1);
                String[] split = PEName.split("-");
                if (setTitil.add(split[0])) {
                    okList.add(itemList);
                } else {
                    qitList.add(itemList);
                }
            }

            //每个城市下的数据
            LinkedHashMap<String, List<List<String>>> map = new LinkedHashMap<>();
            for (List<String> itemList : okList) {
                //按城市分配
                String chengshisi = itemList.get(2);
                List<List<String>> list = map.get(chengshisi);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(chengshisi, list);
                }
                list.add(itemList);
            }
            //实际需要的数据
            List<List<String>> findList = new ArrayList<>();

            int sj = 0;
            for (String chengshi : map.keySet()) {
                sj += map.get(chengshi).size();
            }
            for (String chengshi : map.keySet()) {
                System.out.println(chengshi + "：开始匹配");
                List<List<String>> list = map.get(chengshi);
                int chengshiDataSize = map.get(chengshi).size();
                float divide = (float) chengshiDataSize / (float) sj * 100;

                Float v = (float) dataCount / 100 * divide;
                int count = v.intValue();//实际需要多少条数据
                if (count == 0) {
                    count = list.size();
                }
                if (findList.size() > dataCount) {
                    qitList.addAll(list);
                } else {
                    HashSet<Integer> set = new HashSet<>();
                    if (chengshiDataSize - count > 10) {
                        count += 5;
                    }
                    randomSet(0, chengshiDataSize, count, set);
                    for (int i = 0; i < list.size(); i++) {
                        boolean flag = false;
                        List<String> strings = list.get(i);
                        for (Integer index : set) {
                            if (i == index) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            findList.add(strings);
                        } else {
                            qitList.add(strings);
                        }
                    }
                }
                System.out.println(chengshi + "：匹配结束");
            }

            XSSFSheet sheetNew;
            OutputStream os;

            workbook = new XSSFWorkbook();
            sheetNew = workbook.createSheet("3000数据");
            os = new FileOutputStream("/Users/huzipeng/Desktop/政策结构化列表数据0722-3000数据.xlsx");
            toExcelSheet(findList, sheetNew);
            workbook.write(os);

            workbook = new XSSFWorkbook();
            sheetNew = workbook.createSheet("3000外的数据");
            os = new FileOutputStream("/Users/huzipeng/Desktop/政策结构化列表数据0722-其他数据.xlsx");
            toExcelSheet(qitList, sheetNew);
            workbook.write(os);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test8() {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/huzipeng/WeDrive/天九共享/我的文件/待处理表格/5万条政策列表20200708-地域产业评分.xlsx"));

            OutputStream os = new FileOutputStream("/Users/huzipeng/WeDrive/天九共享/我的文件/已处理表格/5万条政策列表20200708-地域产业评分_匹配后.xlsx");

            //数据源
            XSSFSheet sheet = workbook.getSheetAt(0);//数据
            //初始化关系
            LinkedHashMap<String, LinkedHashMap<String, Integer>> map = new LinkedHashMap<>();
            //12
            //7
            LinkedHashSet<String> chengshiSet = new LinkedHashSet<>();

            LinkedHashSet<String> hangyeSet = new LinkedHashSet<>();
            ArrayList<ArrayList<String>> dataList1 = getDataList(getList(sheet));
            for (ArrayList<String> itemList : dataList1) {
                String chengshi = itemList.get(12);//获取城市
                chengshiSet.add(chengshi);
                String[] hangyeList = itemList.get(7).split(",");
                hangyeSet.addAll(Arrays.asList(hangyeList));
            }

            for (String chengshi : chengshiSet) {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                map.put(chengshi, itemMap);
                for (String hangye : hangyeSet) {
                    if (StringUtils.hasText(hangye)) {
                        itemMap.put(hangye, 0);
                    }
                }
            }
            for (ArrayList<String> itemList : dataList1) {
                String chengshi = itemList.get(12);//获取城市
                String[] hangyeList = itemList.get(7).split(",");
                for (String hangye : hangyeList) {
                    if (map.get(chengshi) != null) {
                        Integer count = map.get(chengshi).get(hangye);
                        if (count != null) {
                            count++;
                            map.get(chengshi).put(hangye, count);
                        }
                    }
                }
            }


            XSSFSheet sheetNew = workbook.createSheet("统计后的数据");
            List<List<String>> qitList = new ArrayList<>();
            qitList.add(new ArrayList<>());
            qitList.get(0).add("");
            for (String hangye : hangyeSet) {
                qitList.get(0).add(hangye);
            }
            for (String chengshi : map.keySet()) {
                List<String> itemList = new ArrayList<>();
                qitList.add(itemList);
                itemList.add(chengshi);
                LinkedHashMap<String, Integer> stringIntegerLinkedHashMap = map.get(chengshi);
                for (Integer count : stringIntegerLinkedHashMap.values()) {
                    itemList.add(count.toString());
                }
            }
            toExcelSheet(qitList, sheetNew);
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test///Users/huzipeng/Desktop/政策列表（嘉实300）(1).xlsx
    public void test7() {
        try {
            int dataCount = 15000;
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/huzipeng/Desktop/policy_info.xlsx"));

            OutputStream os;

            //省市区归逻辑
            XSSFSheet sheet = workbook.getSheetAt(0);//数据
            ArrayList<ArrayList<String>> dataList = getDataList(getList(sheet));
            List<List<String>> qitList = new ArrayList<>();
            //每个城市下的数据
            LinkedHashMap<String, List<List<String>>> map = new LinkedHashMap<>();
            for (ArrayList<String> itemList : dataList) {
                //按城市分配
                String chengshisi = itemList.get(2);
                String title = itemList.get(5);
                if (chengshisi.equals("")) {
                    qitList.add(itemList);
                    continue;
                }
                if (title.length() < 5) {
                    qitList.add(itemList);
                    continue;
                }
                List<List<String>> list = map.get(chengshisi);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(chengshisi, list);
                }
                list.add(itemList);
            }
            //实际的三百条数据
            List<List<String>> list300 = new ArrayList<>();

            int sj = 0;
            for (String chengshi : map.keySet()) {
                sj += map.get(chengshi).size();
            }
            for (String chengshi : map.keySet()) {
                System.out.println(chengshi + "：开始匹配");
                List<List<String>> list = map.get(chengshi);
                int chengshiDataSize = map.get(chengshi).size();
                float divide = (float) chengshiDataSize / (float) sj * 100;

                Float v = (float) dataCount / 100 * divide;
                int count = v.intValue();//实际需要多少条数据
                if (count == 0) {
                    count = list.size();
                }
                if (list300.size() > dataCount) {
                    qitList.addAll(list);
                } else {
                    HashSet<Integer> set = new HashSet<>();
                    if (chengshiDataSize - count > 10) {
                        count += 5;
                    }
                    randomSet(0, chengshiDataSize, count, set);
                    for (int i = 0; i < list.size(); i++) {
                        boolean flag = false;
                        List<String> strings = list.get(i);
                        for (Integer index : set) {
                            if (i == index) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            list300.add(strings);
                        } else {
                            qitList.add(strings);
                        }
                    }
                }
                System.out.println(chengshi + "：匹配结束");
            }

            workbook = new XSSFWorkbook();
            XSSFSheet sheetNew = workbook.createSheet("随机15000");
//            os = new FileOutputStream("/Users/huzipeng/Desktop/policy_info-随机15000.xlsx");
//            toExcelSheet(list300, sheetNew);
//            workbook.write(os);


            workbook = new XSSFWorkbook();
            sheetNew = workbook.createSheet("15000外的数据");
            os = new FileOutputStream("/Users/huzipeng/Desktop/policy_info-15000外的数据1.xlsx");
            toExcelSheet(qitList.subList(0, 50000), sheetNew);
            workbook.write(os);
            workbook = new XSSFWorkbook();
            sheetNew = workbook.createSheet("15000外的数据");
            os = new FileOutputStream("/Users/huzipeng/Desktop/policy_info-15000外的数据2.xlsx");
            toExcelSheet(qitList.subList(50000, 100000), sheetNew);
            workbook.write(os);
            workbook = new XSSFWorkbook();
            sheetNew = workbook.createSheet("15000外的数据");
            os = new FileOutputStream("/Users/huzipeng/Desktop/policy_info-15000外的数据3.xlsx");
            toExcelSheet(qitList.subList(100000, qitList.size()), sheetNew);
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toExcelSheet(List<List<String>> list300, XSSFSheet sheetNew) {
        for (int i = 0; i < list300.size(); i++) {
            List<String> itemList = list300.get(i);
            XSSFRow row = sheetNew.createRow(i);
            for (int j = 0; j < itemList.size(); j++) {
                row.createCell(j).setCellValue(itemList.get(j));
            }
        }
    }

    /**
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     * @param set 随机数结果集
     */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }

    @Test///Users/huzipeng/Desktop/省市区对应表-程序0714.xlsx
    public void test6() {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/huzipeng/WeDrive/天九共享/我的文件/待处理表格/全国省市区县对应关系-包含开发区.xlsx"));

            OutputStream os = new FileOutputStream("/Users/huzipeng/WeDrive/天九共享/我的文件/待处理表格/全国省市区县对应关系-包含开发区-匹配后.xlsx");
            ArrayList<ArrayList<String>> dataList;
//            XSSFSheet sheet2 = workbook.getSheetAt(0);//省市
//
//            ArrayList<ArrayList<String>> dataList = getDataList(getList(sheet2));
//            LinkedHashMap<String, List<String>> ssMap = new LinkedHashMap<>();
//            for (ArrayList<String> itemList : dataList) {
//                String sheng = itemList.get(0);
//                List<String> list = ssMap.get(sheng);
//                if (list == null) {
//                    list = new ArrayList<>();
//                    list.add(sheng);
//                    ssMap.put(sheng, list);
//                }
//                String shi = itemList.get(1);
//                list.add(shi);
//            }
//
//            ArrayList<ArrayList<String>> dataListNew = getExcelList(ssMap);
//
//            toExcelSheet(workbook, dataListNew, "省市归类");

            //省市区归逻辑
            XSSFSheet sheet = workbook.getSheetAt(1);//省市区
            dataList = getDataList(getList(sheet));
            LinkedHashMap<String, LinkedHashMap<String, List<String>>> ssqMap = new LinkedHashMap<>();
            for (ArrayList<String> itemList : dataList) {
                String sheng = itemList.get(0);
                String shi = itemList.get(1);
                String qu = itemList.get(2);
                LinkedHashMap<String, List<String>> itemMap = ssqMap.get(sheng);
                if (itemMap == null) {
                    itemMap = new LinkedHashMap<>();
                    ssqMap.put(sheng, itemMap);
                }
                List<String> quList = itemMap.get(shi);
                if (quList == null) {
                    quList = new ArrayList<>();
                    quList.add(shi);
                    itemMap.put(shi, quList);
                }
                quList.add(qu);
            }

            ArrayList<ArrayList<String>> excelList = new ArrayList<>();
            for (String sheng : ssqMap.keySet()) {
                //一个省的数据
                ArrayList<String> fenge = new ArrayList<>();
                fenge.add("------");
                fenge.add(sheng);
                excelList.add(fenge);
                ArrayList<String> fenge1 = new ArrayList<>();
                fenge1.add("------");
                fenge1.add("");
                excelList.add(fenge1);
                excelList.addAll(getExcelList(ssqMap.get(sheng)));
            }

            XSSFSheet sheetNew = workbook.createSheet("省市区归类");
            for (int i = 0; i < excelList.size(); i++) {
                ArrayList<String> itemList = excelList.get(i);
                XSSFRow row = sheetNew.createRow(i);
                String s = itemList.get(0);
                if (s.equals("-----")) {
                    row.createCell(0).setCellValue(itemList.get(1));
                } else {
                    for (int j = 0; j < itemList.size(); j++) {
                        row.createCell(j).setCellValue(itemList.get(j));
                    }
                }

            }

            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<String>> getExcelList(LinkedHashMap<String, List<String>> ssMap) {
        int lieN = ssMap.size();
        int hangN = 0;
        List<List<String>> shengList = new ArrayList<>(ssMap.values());
        for (List<String> list : shengList) {
            if (list.size() > hangN) {
                hangN = list.size();
            }
        }

        ArrayList<ArrayList<String>> dataListNew = new ArrayList<>();//导出的数据
        for (int i = 0; i < hangN; i++) {
            ArrayList<String> list = new ArrayList<>();
            dataListNew.add(list);
            for (int j = 0; j < lieN; j++) {
                list.add("");
            }
        }

        for (int i = 0; i < shengList.size(); i++) {//遍历省
            List<String> shiList = shengList.get(i);
            for (int j = 0; j < shiList.size(); j++) {
                String shi = shiList.get(j);
                //每次从行开始
                ArrayList<String> hang = dataListNew.get(j);
                hang.set(i, shi);
            }
        }
        return dataListNew;
    }

    private void toExcelSheet(XSSFWorkbook workbook, ArrayList<ArrayList<String>> dataListNew, String 省市归类) {
        XSSFSheet sheetNew = workbook.createSheet(省市归类);
        for (int i = 0; i < dataListNew.size(); i++) {
            ArrayList<String> itemList = dataListNew.get(i);
            XSSFRow row = sheetNew.createRow(i);
            for (int j = 0; j < itemList.size(); j++) {
                row.createCell(j).setCellValue(itemList.get(j));
            }
        }
    }

    /**
     * 匹配省市区是否在查册网有
     */
    @Test
    public void test1() {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\84099\\Desktop\\需要对比省、市、区县表0709.xlsx"));

            OutputStream os = new FileOutputStream("C:\\Users\\84099\\Desktop\\需要对比省、市、区县表0709_匹配后.xlsx");

            Map<String, String> ccbShiMap = new HashMap<>();
            Map<String, String> ccbquMap = new HashMap<>();
            XSSFSheet sheet1 = workbook.getSheetAt(1);
            for (int i = 1; i <= sheet1.getLastRowNum(); i++) {
                XSSFRow row = sheet1.getRow(i);
                if (row == null) break;
                XSSFCell shi = row.getCell(0);
                XSSFCell qu = row.getCell(1);
                if (shi.getStringCellValue() != null) {
                    ccbShiMap.put(shi.getStringCellValue(), shi.getStringCellValue());
                }
                if (qu.getStringCellValue() != null) {
                    ccbquMap.put(qu.getStringCellValue(), qu.getStringCellValue());
                }
            }

            XSSFSheet sheet0 = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet0.getLastRowNum(); i++) {
                XSSFRow row = sheet0.getRow(i);
                if (row == null) break;
                XSSFCell shi = row.getCell(1);
                XSSFCell qu = row.getCell(2);
                String ss = shi.getStringCellValue().replaceAll("市", "");
                boolean flag = false;
                if (ccbShiMap.get(ss) == null) {
                    flag = true;

                }
                if (ccbquMap.get(qu.getStringCellValue()) == null) {
                    flag = true;
                }
                if (flag) {
                    row.createCell(3).setCellValue("不在查策宝");
                }
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理查册网--项目来源字段的网址信息
     */
    @Test
    public void test2() {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\84099\\Desktop\\5万条政策列表20200708.xlsx"));

            OutputStream os = new FileOutputStream("C:\\Users\\84099\\Desktop\\5万条政策列表20200708_匹配后.xlsx");

            XSSFSheet sheet0 = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet0.getLastRowNum(); i++) {
                XSSFRow row = sheet0.getRow(i);
                if (row == null) break;
                if (row.getCell(10) == null) {
                    continue;
                }
                String content = row.getCell(10).getStringCellValue();
                if (content != null) {
                    String[] split = content.split("\n");
                    StringBuilder sb = new StringBuilder();
                    for (String s : split) {
                        int httpIndex = s.indexOf("http");
                        if (httpIndex >= 0) {
                            sb.append(s, 0, httpIndex);
                        } else {
                            sb.append(s);
                        }
                    }
                    row.getCell(10).setCellValue(sb.toString());
                }
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分解出url网址,多行的分出多条数据
     */
    @Test
    public void test5() {

        jiexiHtml("C:\\Users\\84099\\Desktop\\需解读的政策（5万条中筛选）.xlsx", "C:\\Users\\84099\\Desktop\\需解读的政策（5万条中筛选）_筛选的匹配后.xlsx", 10);
    }

    private void jiexiHtml(String url1, String url2, int lie) {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(url1));

            OutputStream os = new FileOutputStream(url2);

            XSSFSheet sheet0 = workbook.getSheetAt(1);//8

            List<XSSFRow> list = getList(sheet0);
            ArrayList<ArrayList<String>> dataList = getDataList(list);
            workbook.close();

            ArrayList<ArrayList<String>> dataListNew = new ArrayList<>();
            for (ArrayList<String> row : dataList) {
                String content = row.get(lie);
                String[] split = content.split("\n");
                boolean flag = false;
                dataListNew.add(row);
                for (String s : split) {
                    int httpIndex = s.indexOf("http");
                    if (httpIndex >= 0) {//发现有http
                        if (httpIndex > 0) {
                            s = s.substring(httpIndex);
                        }
                        if (flag) {//发现是第二条了
                            ArrayList<String> newRow = new ArrayList<>(row);
                            newRow.set(newRow.size() - 1, s);
                            dataListNew.add(newRow);
                        } else {
                            row.add(s);
                        }
                        flag = true;
                    }
                }
            }
            XSSFWorkbook workbookNew = new XSSFWorkbook();
            toExcelSheet(workbookNew, dataListNew, "url拆分");
            workbookNew.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<String>> getDataList(List<XSSFRow> list) {
        short lastCellNum = list.get(0).getLastCellNum();
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        for (XSSFRow row : list) {
            ArrayList<String> itemList = new ArrayList<>();
            dataList.add(itemList);
            for (int i = 0; i < lastCellNum; i++) {
                XSSFCell cell = row.getCell(i);
                String item;
                if (cell == null) {
                    item = "";
                } else if (cell.getCellType() == CellType.STRING) {
                    item = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    item = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                } else {
                    item = "";
                }
                itemList.add(item);
            }
        }
        return dataList;
    }

    /**
     * 正则测试
     */
    @Test
    public void test4() {
        String key = ".*关于申报.*认定与资助申报.*";
        System.out.println("111关于申报3333认定与资助申报4444".matches(key));
    }

    String feiyange = "征集\n" +
            "申报\n" +
            "开展\n" +
            "启动\n" +
            "认定\n" +
            "资助\n" +
            "项目\n" +
            "奖励\n" +
            "资金\n" +
            "资金支持\n" +
            "专项资金\n" +
            "培育\n" +
            "支持政策\n" +
            "课题研究\n" +
            "补贴\n";
    String yange_tou = "关于征集\n" +
            "关于申报\n" +
            "关于开展\n" +
            "关于启动\n" +
            "组织开展\n";
    String yange_wei = "认定\n" +
            "认定与资助申报\n" +
            "资助申报\n" +
            "奖励申报\n" +
            "引导资金项目\n" +
            "资金支持项目\n" +
            "专项资金申报\n" +
            "培育工作\n" +
            "申报工作\n" +
            "支持政策申报\n" +
            "专项资金项目\n" +
            "资助资金\n" +
            "课题研究\n" +
            "支持资金\n";

    /**
     * 清理 严格筛选和非严格筛选 的表格逻辑
     * 机器清理脏数据测试0710.xlsx
     */
    @Test
    public void test3() {
        try {
            //获取excel
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\84099\\Desktop\\7-11爬虫数据（未覆盖区域优先）.xlsx"));
            OutputStream os = new FileOutputStream("C:\\Users\\84099\\Desktop\\7-11爬虫数据（未覆盖区域优先）（清洗）.xlsx");
            int cellNum = 5;
            XSSFSheet sheet1 = workbook.getSheetAt(0);//爬虫数据

            List<String> 严格筛选规则正则 = new ArrayList<>();

            for (String 规则开头 : yange_tou.split("\n")) {
                for (String 规则结尾 : yange_wei.split("\n")) {
                    严格筛选规则正则.add(".*" + 规则开头 + ".*" + 规则结尾 + ".*");
                }
            }
            List<String> 非严格筛选规则正则 = new ArrayList<>();
            for (String item : feiyange.split("\n")) {
                非严格筛选规则正则.add(".*" + item + ".*");
            }


            List<XSSFRow> all = getList(sheet1);//全量爬虫的
            //清洗严格筛选
            List<XSSFRow> listMatching = new ArrayList<>();//严格筛选到的
            List<XSSFRow> listMismatching = new ArrayList<>();//严格筛选不到的
            pipei(all, cellNum, listMatching, listMismatching, 严格筛选规则正则);
            //清洗非严格筛选
            List<XSSFRow> listLikeMatching = new ArrayList<>();//非严格筛选到的
            List<XSSFRow> listLikeMismatching = new ArrayList<>();//非严格筛选不到的
            pipei(all, cellNum, listLikeMatching, listLikeMismatching, 非严格筛选规则正则);

            //生成表格
            workbook = new XSSFWorkbook();
            XSSFSheet 爬虫严格筛选到的 = workbook.createSheet("爬虫严格筛选到的");
            writeSheet(爬虫严格筛选到的, listMatching);
            XSSFSheet 爬虫严格筛选不到的 = workbook.createSheet("爬虫严格筛选不到的");
            writeSheet(爬虫严格筛选不到的, listMismatching);
            XSSFSheet 爬虫非严格筛选到的 = workbook.createSheet("爬虫非严格筛选到的");
            writeSheet(爬虫非严格筛选到的, listLikeMatching);
            XSSFSheet 爬虫非严格筛选不到的 = workbook.createSheet("爬虫非严格筛选不到的");
            writeSheet(爬虫非严格筛选不到的, listLikeMismatching);

            workbook.write(os);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void pipei(List<XSSFRow> all, int cellNum, List<XSSFRow> matching, List<XSSFRow> mismatching, List<String> zhengze) {
        for (XSSFRow row : all) {
            XSSFCell cell = row.getCell(cellNum);
            if (cell == null) {//没有内容则是匹配不到
                mismatching.add(row);
                continue;
            }
            boolean flag = false;
            for (String key : zhengze) {
                String content = cell.getStringCellValue();
                if (content.matches(key)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {//配到到的
                matching.add(row);
            } else {
                mismatching.add(row);
            }
        }
    }

    void writeSheet(XSSFSheet sheet, List<XSSFRow> list) {
        for (int i = 0; i < list.size(); i++) {
            XSSFRow row1 = list.get(i);
            XSSFRow row = sheet.createRow(i);
            for (int j = 0; j < row1.getLastCellNum(); j++) {
                XSSFCell cell = row1.getCell(j);
                if (cell == null) {
                    continue;
                }
                XSSFCell newCell = row.createCell(j);
                if (cell.getCellType() == CellType.STRING) {
                    newCell.setCellValue(cell.getStringCellValue());
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    newCell.setCellValue(cell.getNumericCellValue());
                } else {
                    System.out.println(cell.getCellType());
                }
            }
        }
    }

    private List<XSSFRow> getList(XSSFSheet sheet) {
        List<XSSFRow> list = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) continue;
            list.add(row);
        }
        return list;
    }

    /**
     * 获取有标题的
     *
     * @param sheet
     * @return
     */
    private List<XSSFRow> getListBy0(XSSFSheet sheet) {
        List<XSSFRow> list = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) continue;
            list.add(row);
        }
        return list;
    }

}
