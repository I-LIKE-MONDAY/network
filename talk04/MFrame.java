package talk04;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MFrame extends Frame{
	public MFrame() {
		this(300,300,new Color(243,97,220),false);
	}
	public MFrame(int w, int h) {
		this(w,h,new Color(243,97,220),false);
	}
	public MFrame(Color c) {
		this(300,300,c,false);
	}
	public MFrame(int w, int h,Color c ) {
		this(w,h,c,false);
	}
	public MFrame(int w, int h, Color c, boolean flag) {
		setSize(w, h);
		setBackground(c);
		addWindowListener(new WindowAdapter() {
			@Override // 익명클래스
			public void windowClosing(WindowEvent e) {
				System.exit(0); // 정상적인 종료
			}
		});
		setVisible(true);
		setResizable(flag);
	}

}
