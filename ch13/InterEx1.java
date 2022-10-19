package ch13;

interface A{
	int i = 10; //자동상수가 된다. static final
	void prn(); //메소드는 선언부만. 구현부는 없음.
}

interface Calcu{
	void plus(int i, int j);
}

class Function extends Object implements Calcu{

	@Override
	public void plus(int i, int j) {
	}
	
}

class Graphic implements Calcu{

	@Override
	public void plus(int i, int j) {
	}
}

public class InterEx1 {

	public static void main(String[] args) {

	}

}
