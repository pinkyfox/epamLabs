package com.fibonacci.model.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabasePropertiesParser {

	private static DatabasePropertiesParser instance;

	public static synchronized DatabasePropertiesParser getInstance() {
		if (instance == null) {
			instance = new DatabasePropertiesParser();
		}
		return instance;
	}

	public String getProperty(String propertyName) {
		try (FileInputStream fio = new FileInputStream("src/main/resources/database/databaseConfig.properties")) {
			Properties properties = new Properties();
			properties.load(fio);
			return properties.getProperty(propertyName);
		} catch (IOException e) {
			return null;
		}
	}

	private DatabasePropertiesParser() {
		//NOP
	}
}
