package com.fibonacci.model.dao;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.parser.DatabasePropertiesParser;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Service("CalculatedFibonacciValuesDao")
public class CalculatedFibonacciValuesDao {
	private static DatabasePropertiesParser propertiesParser;
	private static ReentrantLock mutex;
	private static Connection connection;

	public CalculatedFibonacciValuesDao() throws ClassNotFoundException, SQLClientInfoException {
		mutex = new ReentrantLock();
		Class.forName("org.sqlite.JDBC");
		propertiesParser = DatabasePropertiesParser.getInstance();
		try {
			connection = DriverManager.getConnection(propertiesParser.getProperty("db.url"));
			Statement statement = connection.createStatement();
			statement.execute(propertiesParser.getProperty("db.setUpTableQuery"));
		} catch (SQLException e) {
			destroyConnection();
			throw new SQLClientInfoException("Connection to database was failed...", null);
		}
	}

	public void add(Fibonacci fibonacci) {
		try (PreparedStatement statement = connection.prepareStatement(propertiesParser.getProperty("db.insertQuery"))) {
			mutex.lock();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			statement.setInt(1, fibonacci.getIndex());
			statement.setString(2, fibonacci.getValue());
			statement.setString(3, timestamp.toString());

			statement.executeUpdate();
		} catch (Exception e) {
			//NOP
		} finally {
			mutex.unlock();
		}
	}

	public ArrayList<Fibonacci> getAllThatLessOrEqual(Integer quantity) {
		return get(propertiesParser.getProperty("db.selectAllThatLessOrEqualQuery") + quantity);
	}

	public ArrayList<Fibonacci> getAll() {
		return get(propertiesParser.getProperty("db.selectAllQuery"));
	}

	private ArrayList<Fibonacci> get(String query) {
		try (
				Statement statement  = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)
		) {
			ArrayList<Fibonacci> result = new ArrayList<>();

			while (resultSet.next()) {
				result.add(new Fibonacci(resultSet.getInt("id"),
						resultSet.getString("result")
				));
			}

			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@PreDestroy
	public void destroyConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (Exception e) {
				//NOP
			}
		}
	}
}
