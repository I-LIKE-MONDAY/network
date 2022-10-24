package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLEx {

	public static void main(String[] args) {
    String spec = "https://search.naver.com:80/search.naver?where=nexearch&"
    	+"sm=top_hty&fbm=1&ie=utf8&query=java#top";
    try {
		URL url = new URL(spec);
		System.out.println("Protocol: " + url.getProtocol());
		System.out.println("Host: " + url.getHost());
		System.out.println("Port: " + url.getPort());
		System.out.println("Path: " + url.getPath());
		System.out.println("Query : " + url.getQuery());
		System.out.println("FileName: " + url.getFile());
		System.out.println("Ref: " + url.getRef());
		url = new URL("http://jspstudy.co.kr/main/main.jsp");
		BufferedReader br = new BufferedReader(   //자바와 네이버 연결
				new InputStreamReader(url.openStream(),"UTF-8"));
		String line = "";
		while(true) {
			line = br.readLine();
			if(line == null) break;
			System.out.println(line);
		}
		br.close();
		System.out.println("=========");
		System.out.println("END~~");
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
}
