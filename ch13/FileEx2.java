package ch13;
import java.io.File;
public class FileEx2 {
	// 가변인수 
	// 배열과 for문은 함께
	public static void plus(int...arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		System.out.println("sum: " + sum);
	}

	public static void main(String[] args) {
		
		plus(1,2); //가변적이다.
		plus(1,2,3);
		plus(1,2,3,4);
		plus(1,2,3,4,5);
		
		//int  <-> Integer
		int i = 22;  //autoboxing 자동으로 넘어간다.
		Integer it = i;
		
		int j = new Integer(23);  //autounboxing
//		System.out.println("정수 i : " + i + " 정수 j : "+ j);
//		System.out.printf("정수 i : %d  정수 j :%d", i, j);
		
		
		String dir = "C:/Windows";
		File fdir = new File(dir);
		if(fdir.isDirectory()) {
			System.out.println("검색 디렉토리 " +dir);
			System.out.println("===========");
			String list[] = fdir.list();
			//디렉토리인 것은 대괄호, 일반파일은 대괄호 없이 출력
			for (int k = 0; k < list.length; k++) { 
				File f = new File(dir + File.separator + list[k]);
				if(f.isDirectory()) {
					System.out.println("["+list[k] + "]");
			}else {
				System.out.println(list[k]);
			  }
				System.out.println(list[k]);
			 }
		} else {
			System.out.printf("지정한 %s는 디렉토리가 아닙니다.", dir);
		}
	}
}
	
