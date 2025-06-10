package com.example.forum.controller.form;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReportForm {
	private int id;
	private String content;
	private Date created_date;
	private Date updated_date;
}
