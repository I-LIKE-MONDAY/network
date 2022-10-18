package ch13;

import java.io.File;

public class FileEx1 {

	public static void main(String[] args) {
		File f = new File("ch13/FileEx1.java");
		if(f.exists()) {
			System.out.println("파일이름 : " + f.getName());
			System.out.println("상대경로 : " + f.getPath());
			System.out.println("절대경로 : " + f.getAbsolutePath());
			System.out.println("쓰기가능 : " + f.canWrite());
			System.out.println("읽기가능 : " + f.canRead());
			System.out.println("파일크기 : " + f.length() + "byte");
			
		} else {
			System.out.println("파일이 존재하지 않습니다.");
		}
	}
}
