/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Database {

    private static volatile Database instance = null;
    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=sab_test;encrypt=true;trustServerCertificate=true";
    private final String usrname = "kosta";
    private final String pass = "123";
    private boolean created = false;

    private Database() {
    }

    public synchronized static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        instance.created = true;
        return instance;
    }

    public int post(String sql) {
        if (!this.created) {
            throw new IllegalStateException("Database instance not created");
        }
        try (
                Connection connection = DriverManager.getConnection(url, usrname, pass);
                Statement statement = connection.createStatement();) {
            connection.setAutoCommit(false);
            int affected = statement.executeUpdate(sql);
            if (affected == 0) {
                connection.rollback();
            }
            else connection.commit();
            return affected;

        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging instead of printing stack trace
            return -1;
        }
    }
    public int postPK(String sql) {
        if (!this.created) {
            throw new IllegalStateException("Database instance not created");
        }
        try (
                Connection connection = DriverManager.getConnection(url, usrname, pass);
                Statement statement = connection.createStatement();) {
            connection.setAutoCommit(false);
            int affected = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            if (affected == 0) {
                connection.rollback();
		return -1;
            }
            else connection.commit();
	    ResultSet pks = statement.getGeneratedKeys();
	     if (pks.next()) { 
                return pks.getInt(1); 
            } else {
                System.err.println("No generated keys found.");
                return -1; 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
  }

	static class Context {

		String cols = "";
	}

	public List get(String sql,Integer...indexes) {

        if (!this.created) {
            throw new IllegalStateException("Database instance not created");
        }
        try (
                Connection connection = DriverManager.getConnection(url, usrname, pass);
                Statement statement = connection.createStatement();
                ResultSet res = statement.executeQuery(sql);) {

            ArrayList<String> list = new ArrayList<String>();
            List<Integer> inds = (List<Integer>) Arrays.asList(indexes);
            while (res != null && res.next()) {
		    Context lambdaContext = new Context();
                inds.forEach(index -> {
                    try {
                        lambdaContext.cols += res.getString(index);
                        if(indexes.length > 1) lambdaContext.cols += "&";
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                list.add(lambdaContext.cols);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging instead of printing stack trace
            return null;
        }
    }

	public float execute(int idP) {
		String sql = "{call CenaJedneIsporuke(?,?)}";
		try (
			Connection connection = DriverManager.getConnection(url, usrname, pass);
			CallableStatement statement = connection.prepareCall(sql);) {
			statement.setInt(1, idP);
			statement.registerOutParameter(2, Types.FLOAT);
			statement.execute();
			return statement.getFloat(2);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
