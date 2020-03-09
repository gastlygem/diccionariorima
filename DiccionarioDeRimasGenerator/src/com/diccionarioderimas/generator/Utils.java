/***************************************************************************
 *   Copyright (C) 2009 by Eduardo Rodriguez Lorenzo                       *
 *   edu.tabula@gmail.com                                                  *
 *   http://www.cronopista.com                                             *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
package com.diccionarioderimas.generator;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {

	public static String getApplicationPath() {

		 try {
		    	URL url = Utils.class.getProtectionDomain()
		        .getCodeSource().getLocation();
				
				String szUrl = url.toString();
			
			    szUrl = szUrl.substring(0, szUrl.lastIndexOf("/"));
			    URI uri = new URI(szUrl);
			    return new File(uri).getAbsolutePath()+File.separator;
				 
				
			} catch (URISyntaxException e) {
				
			}
		    
			return new File("").getAbsolutePath()+File.separator;
	}
	
	

	public static void copyFile(InputStream fis, File out) throws Exception{

		FileOutputStream fos = null;
		
			fos = new FileOutputStream(out);
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
			fis.close();
			fos.close();
		

	}
	
	public static void copyFileToPlatformCharacterEncoding(File inFile,String encoding, File outFile) throws Exception{

		
			BufferedWriter out=new BufferedWriter(new FileWriter(outFile));
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(inFile),encoding));
			String line=null;
			while ((line = in.readLine()) != null) {
				out.write(line);
				out.write('\n');
			}
			in.close();
			out.close();
		

	}

	@SuppressWarnings("rawtypes")
	public static void unzip(File fileFrom,File fileTo) throws Exception {
		
		Enumeration entries;
		ZipFile zipFile;

		
			zipFile = new ZipFile(fileFrom);

			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				InputStream in = zipFile.getInputStream(entry);
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(fileTo));
				byte[] buffer = new byte[1024];
				int len;

				while ((len = in.read(buffer)) >= 0)
					out.write(buffer, 0, len);

				in.close();
				out.close();
			}

			zipFile.close();
		
		
		
	}

	public static void main(String[] args) {
		System.out.println(getApplicationPath());
	}
}
