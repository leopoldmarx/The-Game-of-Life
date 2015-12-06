package com.leopoldmarx.thegameoflife.driver;

import com.leopoldmarx.thegameoflife.view.ViewMain;

import javafx.application.Application;

public class Program {

	private static Program instance;
	
	public static Program getInstance()
	{
		if (instance == null) instance = new Program();
		return instance;
	}
	
	private Program() {
		
		Application.launch(ViewMain.class, "");
	}
}
