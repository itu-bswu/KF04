package loader;
import java.io.IOException;


/**
 * Represents the raw data from a line in kdv_unload.txt, representing an edge
 */
public class EdgeData {
	public int FNODE; // NB: Written by InstanceCreator
	public int TNODE; // NB: Written by InstanceCreator
	public final double LENGTH;
	public final int DAV_DK;
	public final int DAV_DK_ID;
	public final int TYP;
	public final String VEJNAVN;
	public final int FROMLEFT;
	public final int TOLEFT;
	public final int FROMRIGHT;
	public final int TORIGHT;
	public final String FROMLEFT_BOGSTAV;
	public final String TOLEFT_BOGSTAV;
	public final String FROMRIGHT_BOGSTAV;
	public final String TORIGHT_BOGSTAV;
	public final int V_SOGNENR;
	public final int H_SOGNENR;
	public final int V_POSTNR;
	public final int H_POSTNR;
	public final int KOMMUNENR;
	public final int VEJKODE;
	public final int SUBNET;
	public final String RUTENR;
	public final String FRAKOERSEL;
	public final int ZONE;
	public final int SPEED;
	public final double DRIVETIME;
	public final String ONE_WAY;
	public final String F_TURN;
	public final String T_TURN;
	public final String VEJNR;
	public final String AENDR_DATO;
	public final int TJEK_ID;

	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(FNODE + ",");
		sbuf.append(TNODE + ",");
		sbuf.append(LENGTH + ",");
		sbuf.append(DAV_DK + ",");
		sbuf.append(DAV_DK_ID + ",");
		sbuf.append(TYP + ",");
		sbuf.append("'" + VEJNAVN + "'" + ",");
		sbuf.append(FROMLEFT + ",");
		sbuf.append(TOLEFT + ",");
		sbuf.append(FROMRIGHT + ",");
		sbuf.append(TORIGHT + ",");
		sbuf.append(FROMLEFT_BOGSTAV + ",");
		sbuf.append(TOLEFT_BOGSTAV + ",");
		sbuf.append(FROMRIGHT_BOGSTAV + ",");
		sbuf.append(TORIGHT_BOGSTAV + ",");
		sbuf.append(V_SOGNENR + ",");
		sbuf.append(H_SOGNENR + ",");
		sbuf.append(V_POSTNR + ",");
		sbuf.append(H_POSTNR + ",");
		sbuf.append(KOMMUNENR + ",");
		sbuf.append(VEJKODE + ",");
		sbuf.append(SUBNET + ",");
		sbuf.append(RUTENR + ",");
		sbuf.append(FRAKOERSEL + ",");
		sbuf.append(ZONE + ",");
		sbuf.append(SPEED + ",");
		sbuf.append(DRIVETIME + ",");
		sbuf.append(ONE_WAY + ",");
		sbuf.append(F_TURN + ",");
		sbuf.append(T_TURN + ",");
		sbuf.append(VEJNR + ",");
		sbuf.append(AENDR_DATO + ",");
		sbuf.append(TJEK_ID);
		return sbuf.toString();
	}

	public EdgeData(String line) throws IOException {
		DataLine dl = new DataLine(line);

		try {
			FNODE = dl.getPositiveInt();
			TNODE = dl.getPositiveInt();
			LENGTH = dl.getPositiveDouble();
			DAV_DK = dl.getPositiveInt();
			DAV_DK_ID = dl.getPositiveInt();
			TYP = dl.getPositiveInt();
			VEJNAVN = dl.getString();
			FROMLEFT = dl.getPositiveInt();
			TOLEFT = dl.getPositiveInt();
			FROMRIGHT = dl.getPositiveInt();
			TORIGHT = dl.getPositiveInt();
			FROMLEFT_BOGSTAV = dl.getString();
			TOLEFT_BOGSTAV = dl.getString();
			FROMRIGHT_BOGSTAV = dl.getString();
			TORIGHT_BOGSTAV = dl.getString();
			V_SOGNENR = dl.getPositiveInt();
			H_SOGNENR = dl.getPositiveInt();
			V_POSTNR = dl.getPositiveInt();
			H_POSTNR = dl.getPositiveInt();
			KOMMUNENR = dl.getPositiveInt();
			VEJKODE = dl.getPositiveInt();
			SUBNET = dl.getPositiveInt();
			RUTENR = dl.getString();
			FRAKOERSEL = dl.getString();
			ZONE = dl.getPositiveInt();
			SPEED = dl.getPositiveInt();
			DRIVETIME = dl.getPositiveDouble();
			ONE_WAY = dl.getString();
			F_TURN = dl.getString();
			T_TURN = dl.getString();
			VEJNR = dl.getString();
			// dl.discard();
			AENDR_DATO = dl.getString();
			TJEK_ID = dl.getPositiveInt();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}
}
