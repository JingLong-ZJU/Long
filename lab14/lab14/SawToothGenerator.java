package lab14;

import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;

public class SawToothGenerator implements Generator {
	private int period;
	private int state;	

	public SawToothGenerator(int period) {
		state = 0;
		this.period = period;
		if(period == 0) {
			throw new RuntimeException("can`t divided by zero, or 0 period isn`t accept");
		}
	}
	
	
	@Override
	public double next() {
		state = (state + 1);
		return ((double)state%period)*2/period - 1;	
	}

}
