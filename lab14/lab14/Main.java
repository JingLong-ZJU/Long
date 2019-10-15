package lab14;
import java.util.ArrayList;

import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;
import lab14lib.GeneratorDrawer;
import lab14lib.GeneratorPlayer;
import lab14lib.MultiGenerator;

public class Main {
	public static void main(String[] args) {
	    Generator generator = new StrangeBitwiseGenerator(1024);
	    GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
	    gav.drawAndPlay(128000, 1000000);
	} 
}