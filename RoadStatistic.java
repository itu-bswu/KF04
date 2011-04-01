import graphlib.Graph;

import java.io.File;
import java.io.IOException;
import java.util.Set;


public class RoadStatistic {
	private final File dataDir = new File(".", "data"); //Where control needs to look for the nodeFile and edgeFile
	private final String nodeFile = "kdv_node_unload.txt"; //The nodes used to construct the graph
	private final String edgeFile = "kdv_unload.txt"; //The edges used to construct the graph
	private Graph<KrakEdge, KrakNode> graph;

	public RoadStatistic(){
		try {
			graph = KrakLoader.graphFromFiles(new File(dataDir, nodeFile).getAbsolutePath(), new File(dataDir, edgeFile).getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<KrakEdge> krakEdges =  graph.getAllEdges();
		int[] counter = new int[100];
		for (KrakEdge krakEdge : krakEdges) {
			counter[krakEdge.type]++;
			System.out.println(krakEdge.roadname);
		}
		int i = -1;
		for(int roadNumbers : counter){
			i++;
			if(roadNumbers==0)continue;
			System.out.println("Vejtype: "+(i)+" , Antal: "+roadNumbers);
		}
		/*
		Vejtype: 0 , Antal: 5
		1 – Motorveje
		Vejtype: 1 , Antal: 3225
		2 – Motortrafikvej
		Vejtype: 2 , Antal: 1709
		3 – Primærrute > 6 meter
		Vejtype: 3 , Antal: 15790
		4 – Sekundærrute > 6 meter
		Vejtype: 4 , Antal: 46704
		5 – Vej 3 - 6 meter
		Vejtype: 5 , Antal: 199268
		6 – Anden vej
		Vejtype: 6 , Antal: 479263
		8 – sti
		Vejtype: 8 , Antal: 61126
		10 – markvej 
		Vejtype: 10 , Antal: 2035
		11 – gågader 
		Vejtype: 11 , Antal: 956
		21 – proj. motorvej
		Vejtype: 21 , Antal: 16
		22 – proj. motortrafikvej
		Vejtype: 22 , Antal: 1
		23 – proj. primærvej
		Vejtype: 23 , Antal: 4
		24 – proj. sekundærvej
		Vejtype: 24 , Antal: 28
		25 – Proj. vej 3-6 m
		Vejtype: 25 , Antal: 64
		26 – Proj. vej < 3 m
		Vejtype: 26 , Antal: 95
		28 – Proj. sti
		Vejtype: 28 , Antal: 13
		31 – Motorvejsafkørsel
		Vejtype: 31 , Antal: 1368
		32 – Motortrafikvejsafkørsel
		Vejtype: 32 , Antal: 132
		33 – Primærvejsafkørsel
		Vejtype: 33 , Antal: 4
		34 – Sekundærvejsafkørsel
		Vejtype: 34 , Antal: 31
		35 – Anden vejafkørsel
		41 – Motorvejstunnel
		Vejtype: 41 , Antal: 7
		42 – Motortrafikvejstunnel
		Vejtype: 42 , Antal: 2
		43 – Primærvejstunnel
		44 – Sekundærvejstunnel
		45 – Anden vejtunnel
		46 – Mindre vejtunnel
		48 – Stitunnel
		80 – Færgeforbindelse
		Vejtype: 80 , Antal: 82
		Vejtype: 95 , Antal: 2
		99 – Stednavn eksakt beliggenhed ukendt
		Vejtype: 99 , Antal: 371

	*/
	}
	public static void main(String[] args){
		new RoadStatistic();
	}
}
