import java.awt.Color;


public enum RoadColor {
	Red(0xf05a5a),
	Yellow(0xf6f64d),
	Orange(0xfba819),
	Grey(0xbcbcbc),
	Blue(0x90e7dc);
	
	private Color color;

	RoadColor(int rgb){
		color = new Color(rgb);
	}
	
	public Color getColor(){
		return color;
	}
}
