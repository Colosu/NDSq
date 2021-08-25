package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class Main {
	protected static int LEN = 2; //Length of the input sequences
	protected static int N = 16; //Number of FSMs per block
	protected static int EXP = 1; //Number of blocks

	public static void main(String[] args) {

		//Initialization
		IOHandler IOH = new IOHandler();
		Operations Ops = new Operations();

		//File names.
		String Ifile = "binary.fst";
		String Ofile = "PearsonResults.txt";

		//File variables
		File folder;
		FileWriter OFile;


		//Needed variables
		Graph G;
		Vals vals = new Vals();
		double[] FEP = new double[N];
		double[] NDSq = new double[N];
    	double SqTime = 0.0;
    	double OpSqTime = 0.0;
    	double ExpTime = 0.0;
		
		double Ps = 0.0;
		double Ss = 0.0;
		
		for (int K = 1; K <= 1; K++) {
			//Initialization of output file names
			//Open output files
			//Write head of output files
			try {
				Ofile = "Results.txt";
				OFile = new FileWriter(Ofile, false);
				OFile.write("| Pearson correlation | Spearman correlation | NDSq Time | Sq Time | Exploration Time |\n");
				OFile.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			//Main loop
			//INI = first test block
			//EX = number of test blocks to test
			for (int J = 0; J < EXP; J++) {

				//Initialize arrays to 0
				for (int i = 0; i < N; i++) {
					FEP[i] = 0;
					NDSq[i] = 0;
				}

				//Initialization of input file name
				folder = new File("Dataset/Dot");
				
				//Block loop
				//N = number of tests per block
				int I = 0;
				for (File IFile : folder.listFiles()) {
					
					G = IOH.readGraph(IFile.toString());
//					G = IOH.readGraph("./Dot/train4.dot");

					if (G == null) {
						System.err.println(Ifile.toString() + ": Failled to load the automaton.");

						//Close output files
						try {
							OFile.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}

					//Main computation
					try {
						Ops.SqueezinessAndPColl(G, LEN, vals);
						FEP[I] = vals.getPColl();
						NDSq[I] = vals.getNDSq();
						SqTime += vals.getSqTime();
						OpSqTime += vals.getOpSqTime();
						ExpTime += vals.getExpTime();
						//Test control (in order to know where we are during computation)
						System.out.println("FSM " + String.valueOf(I+1) + "\n");
						System.out.flush();

					} catch (Exception e) {
						e.printStackTrace();
					}
					I++;
				}

				//Test control (in order to know where we are during computation)
				System.out.println("test " + String.valueOf(J+1) + "\n");
				System.out.flush();

				//Compute correlations
				PearsonsCorrelation PC = new PearsonsCorrelation();
				SpearmansCorrelation SC = new SpearmansCorrelation();
				Ps = PC.correlation(FEP, NDSq);
				Ss = SC.correlation(FEP, NDSq);

				//Write final results
				try {
					OFile.write(String.valueOf(Ps) + " & " + String.valueOf(Ss) + " & " + String.valueOf(ExpTime+SqTime+OpSqTime) + " & " + String.valueOf(ExpTime+SqTime) + " & " + String.valueOf(ExpTime) + "\n");
					OFile.write("\\hline\n");
					OFile.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}

			//Write final means
			try {

				//Close output files
				OFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//End process
		return;
	}
}
