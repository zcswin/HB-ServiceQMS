package com.ww.boengongye;

import com.ww.boengongye.service.impl.DfAoiSlServiceImpl;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author liwei
 * @create 2023-09-06 7:16
 */
//@SpringBootTest
public class Test {

	@Autowired
	private DfAoiSlServiceImpl dfAoiSlService;

	@org.junit.Test
	public void tset1() throws Exception {
		dfAoiSlService.importSL(null);
	}


	@org.junit.Test
	public void test1() throws ParseException {
		String dateTimeString = "2023-10-10 00:13:32";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(dateTimeString);

		SimpleDateFormat hourFormat = new SimpleDateFormat("H");
		String hour = hourFormat.format(date);

		int hourValue = Integer.parseInt(hour);
		System.out.println("小时数: " + hour);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

		LocalDateTime newDateTime = dateTime.minusHours(1);

		String formattedNewDateTime = newDateTime.format(formatter);
		System.out.println("减去一个小时后的时间: " + formattedNewDateTime);
	}


	@org.junit.Test
	public void getTimeByPieceName(){
		String pieceName = "G0MGVC0063E00006T9+9+0707516570012004912953319J";
		String keywords = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
		String str4 = pieceName.substring(3,4);
		String str5 = pieceName.substring(4,5);
		String str6 = pieceName.substring(5,6);

		Integer str4Index = keywords.indexOf(str4);
		Integer str5Index = keywords.indexOf(str5);
		Integer str6Index = keywords.indexOf(str6);

		Double str4Double = str4Index*(Math.pow(34,2));
		Double str5Double = str5Index*(Math.pow(34,1));
		Double str6Double = str6Index*(Math.pow(34,0));

		Double timeDouble = 25569+str4Double+str5Double+str6Double;

		// 将时间序列号转换为Java中的Date对象
		Date timeDate = DateUtil.getJavaDate(timeDouble);

		// 格式化Date对象为字符串
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String time  = sdf.format(timeDate);

		Integer week = TimeUtil.getWeekByTimeStr(time);
		System.out.println(week);

	}




}
