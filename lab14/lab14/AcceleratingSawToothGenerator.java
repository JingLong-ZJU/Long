package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator{
	private int period;
	private int state;	
	private double rate;
	public AcceleratingSawToothGenerator(int period, double rate) {
		state = 0;
		this.period = period;
		this.rate = rate;
		if(period == 0) {
			throw new RuntimeException("can`t divided by zero, or 0 period isn`t accept");
		}
	}
	
	
	@Override
	public double next() {
		state = (state + 1) % period;
		if(state == 0)
			this.period = (int) (this.period * this.rate);
		return (double)state*2/period - 1;	
	}
}
