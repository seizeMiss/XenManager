package main.java.dragon.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DBUtil {
	private final static String DRIVER_NAME="com.mysql.jdbc.Driver";
	private final static String URL="jdbc:mysql://localhost:3306/vmmanager";
	private final static String USERNAME="root";
	private final static String PASSWORD="root";
	public static Connection getConnection(){
		Connection con=null;
		try{
			Class.forName(DRIVER_NAME);
			con=DriverManager.getConnection(URL,USERNAME,PASSWORD);
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	public static void closeConnection(Connection con){
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void closeStatement(Statement statement){
		try{
			statement.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void closePrepareStatement(PreparedStatement pStatement){
		try{
			pStatement.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void closeResultSet(ResultSet res){
		try{
			res.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void close(PreparedStatement pStatement ,ResultSet rs,Connection con){
		if(rs!=null){
			try{
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				rs=null;
			}
		}
		
		if(pStatement!=null){
			try{
				pStatement.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				pStatement=null;
			}
		}
		if(con!=null){
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con=null;
			}
		}
	}
	public static void main(String[] args) {
		Connection con=getConnection();
		try {
			PreparedStatement preparedStatement = con.prepareStatement("select * from user");
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				System.out.println(resultSet.getString("username"));
				System.out.println(resultSet.getString("password"));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
