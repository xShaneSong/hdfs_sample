package com.simple.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsDAO {

	private static String hdfsPath = "hdfs://localhost/clouddisk";
	
	Configuration conf = new Configuration();
	
	public void copyFile(String local) throws IOException
	{
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.copyFromLocalFile(new Path(local), new Path(hdfsPath));
		fs.close();
	}
	
	public void download(String remote, String local) throws IOException
	{
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.copyToLocalFile(false, new Path(remote), new Path(local));
		fs.close();
	}
	
	public static void deleteFromHdfs(String deletePath) throws FileNotFoundException, IOException
	{
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.deleteOnExit(new Path(deletePath));
		fs.close();
	}
	
	public static FileStatus[] getDirectoryFromHdfs() throws FileNotFoundException, IOException
	{
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FileStatus[] list = fs.listStatus(new Path(hdfsPath));
		if (list != null)
		{
			for (FileStatus f : list)
			{
				System.out.printf("name: %s, folder: %s, size: %d\n",
						f.getPath().getName(), f.isDir(), f.getLen());
			}
		}
		fs.close();
		return list;
	}
}
