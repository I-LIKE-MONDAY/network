package ch13;

import java.awt.Button;
import java.awt.Color;
import java.awt.Label;

abstract class Shape{
	abstract void draw(); // 추상메소드 : 몸체는 없고 선언만 있음.
}

class Rectangle extends Shape{
	@Override
	void draw() {
		
	}
}

class Circle extends Shape{
	@Override
	void draw() {
		
	}
}

public class AbstractEx extends Object {  //extends object가 생략되어있다.

	public AbstractEx(){
		super(); //super()는 상위클래스 생성자 Object. SUPER는 Object를 나타낸다
	}
	
	public static void main(String[] args) {
		AbstractEx ae = new AbstractEx();
		//java.awt;
		Button btn = new Button("버튼"); //버튼이름 넣어줌
		btn.setBackground(Color.red);
		Label label = new Label("라벨");
		
		
		try {
	    int arr[] = new int[3];
	    arr[3] = 22; //배열 번호 0 1 2 인데 3으로 하니.
	  } catch(Exception e) {
		System.out.println("배열 예외");    
	}
  }
}










