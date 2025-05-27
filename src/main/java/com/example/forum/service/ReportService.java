package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
public class ReportService {
	@Autowired
	ReportRepository reportRepository;

	/*
	 * レコード全件取得処理（日付絞り込み実装）
	 */
	public List<ReportForm> findByCreatedDateBetweenOrderByUpdatedDateDesc(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			if(hasText(start)){
				startDate =  sdFormat.parse(start + " 00:00:00.000");
			} else {
				startDate = sdFormat.parse("2020-01-01 00:00:00.000");
			}

			if(hasText(end)){
				endDate = sdFormat.parse(end + " 23:59:59.999");
			} else {
				Date date = new Date();
				String dateTime = sdFormat.format(date);
				endDate = sdFormat.parse(dateTime +".999");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Report> results = reportRepository.findByCreatedDateBetweenOrderByUpdatedDateDesc(startDate, endDate);
		List<ReportForm> reports = setReportForm(results);
		return reports;
	}

	/*
	 * DBから取得したデータをFormに設定
	 */
	private List<ReportForm> setReportForm(List<Report> results) {
		List<ReportForm> reports = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			ReportForm report = new ReportForm();
			Report result = results.get(i);
			report.setId(result.getId());
			report.setContent(result.getContent());
			report.setCreated_date(result.getCreatedDate());
			report.setUpdated_date(result.getUpdatedDate());
			reports.add(report);
		}
		return reports;
	}

	/*
	 * レコード追加
	 */
	public void saveReport(ReportForm reqReport) {
		Report saveReport = setReportEntity(reqReport);
		reportRepository.save(saveReport);
	}

	/*
	 * リクエストから取得した情報をEntityに設定
	 */
	private Report setReportEntity(ReportForm reqReport) {
		Report report = new Report();
		report.setId(reqReport.getId());
		report.setContent(reqReport.getContent());
		Date nowDate = null;
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try{
			Date date = new Date();
			String dateTime = sdFormat.format(date);
			nowDate = sdFormat.parse(dateTime +".999");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		report.setUpdatedDate(nowDate);
		return report;
	}

	public void deleteReport(Integer id) {
		reportRepository.deleteById(id);
	}

	/*
	 * レコード1件取得
	 */
	public ReportForm editReport(Integer id) {
		List<Report> results = new ArrayList<>();
		results.add((Report) reportRepository.findById(id).orElse(null));
		List<ReportForm> reports = setReportForm(results);
		return reports.get(0);
	}
}