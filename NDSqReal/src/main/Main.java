package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class Main {
	protected static int LEN = 2; //Length of the input sequences
	protected static int N = 11; //Number of FSMs per block
	protected static int EXP = 1; //Number of blocks
	protected static int ND = 3; //Number of 

	public static void main(String[] args) {

		//Initialization
		IOHandler IOH = new IOHandler();
		Operations Ops = new Operations();

		//File names.
		String Ifile = "binary.fst";
		String Ofile = "PearsonResults.txt";
		String Ofile2 = "SpearmanResults.txt";

		//File variables
		File folder;
		FileWriter OFile;
		FileWriter OFile2;


		//Needed variables
		Graph G;
		Vals vals = new Vals();
		double[] FEP = new double[N];
		double[] NDSq = new double[N];
		double Ps = 0.0;
		double Ss = 0.0;
//		double MeanPs = 0.0;
//		double MeanSs = 0.0;
		
		for (int K = 1; K <= 1; K++) {
			//Initialization of output file names
			//Open output files
			//Write head of output files
			try {
				Ofile = "Pearson.txt";
				OFile = new FileWriter(Ofile, false);
//				OFile.write("| #Test | Pearson correlation Sq1 | Pearson correlation Sq2 | Pearson correlation Sq3 | Pearson correlation Sq4 | Pearson correlation Sq5 | Pearson correlation Sq6 | Pearson correlation Sq7 | Pearson correlation Sq8 | Pearson correlation Sq9 | Pearson correlation Sq10 |\n");
//				OFile.flush();
				Ofile2 = "Spearman.txt";
				OFile2 = new FileWriter(Ofile2, false);
//				OFile2.write("| #Test | Spearman correlation Sq1 | Spearman correlation Sq2 | Spearman correlation Sq3 | Spearman correlation Sq4 | Spearman correlation Sq5 | Spearman correlation Sq6 | Spearman correlation Sq7 | Spearman correlation Sq8 | Spearman correlation Sq9 | Spearman correlation Sq10 |\n");
//				OFile2.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			//Main loop
			//INI = first test block
			//EX = number of test blocks to test
//			MeanPs = 0.0;
//			MeanSs = 0.0;
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
							OFile2.close();
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
					OFile.write(String.valueOf(Ps) + "\n");
//						OFile.write("\\hline\n");
					OFile.flush();
					OFile2.write(String.valueOf(Ss) + "\n");
//						OFile2.write("\\hline\n");
					OFile2.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
//				MeanPs += Ps;
//				MeanSs += Ss;
			}

			//Write final means
			try {
//				OFile.write("mean & " + String.valueOf(MeanPs/EXP) + " \\\\\n");
//				OFile.write("\\hline\n");
//				OFile.flush();
//				OFile2.write("mean & " + String.valueOf(MeanSs/EXP) + " \\\\\n");
//				OFile2.write("\\hline\n");
//				OFile2.flush();

				//Close output files
				OFile.close();
				OFile2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//End process
		return;
	}
}
