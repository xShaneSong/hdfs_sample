package com.simple.model;

import java.sql.*;

public class UserDAO {
	
	private Statement sm = null;
	private Connection ct = null;
	private ResultSet rs = null;
	
	public void close() {
		try {
			if (sm != null)
			{
				sm.close();
				sm = null;
			}
			if (ct != null)
			{
				ct.close();
				ct = null;
			}
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean checkUser(String user, String password) {
		boolean b = false;
		try {
			
			ct = new ConnDB().getConn();
			sm = ct.createStatement();
			String sql = "select * from student where name=\"" + user +"\"";
			rs = sm.executeQuery(sql);
			if (rs.next())
			{
				String pwd = rs.getString(3);
				if (password.equals(pwd))
				{
					b = true;
				} else {
					b = false;
				}
			} else {
				b = false;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally {
			this.close();
		}
		return b;
	}
	
	public void insert(String name, String password) throws SQLException {
		int i = 0;
		ct = new ConnDB().getConn();
		sm = ct.createStatement();
		String sql = "insert into student (name, password) values ('" + name + "','" + password + "')";
		System.out.println(sql);
		i = sm.executeUpdate(sql);
	}

}
